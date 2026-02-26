package stg.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.stb.STBVorbisInfo;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import stg.service.audio.IAudioManager;

public class ALAudioManager implements IAudioManager {
    private static ALAudioManager instance;
    private long device;
    private long context;
    private boolean initialized;
    
    private final Map<String, Integer> musicBuffers = new HashMap<>();
    private final Map<String, Integer> soundBuffers = new HashMap<>();
    private final Map<String, Integer> musicSources = new HashMap<>();
    private final Map<String, ConcurrentLinkedQueue<Integer>> soundSources = new HashMap<>();
    private final Map<String, String> musicPaths = new HashMap<>();
    private final Map<String, String> soundPaths = new HashMap<>();
    
    // 每个音效的默认源数量
    private static final int DEFAULT_SOUND_SOURCES_PER_SOUND = 5;
    
    private float musicVolume = 0.7f;
    private float soundVolume = 1.0f;
    
    private ALAudioManager() {
        this.initialized = false;
    }
    
    public static synchronized ALAudioManager getInstance() {
        if (instance == null) {
            instance = new ALAudioManager();
        }
        return instance;
    }
    
    @Override
    public void init() {
        if (initialized) {
            return;
        }
        
        try {
            // 初始化OpenAL设备
            device = ALC10.alcOpenDevice((ByteBuffer) null);
            if (device == MemoryUtil.NULL) {
                throw new IllegalStateException("无法打开OpenAL设备");
            }
            
            // 创建OpenAL上下文
            context = ALC10.alcCreateContext(device, (IntBuffer) null);
            if (context == MemoryUtil.NULL) {
                ALC10.alcCloseDevice(device);
                throw new IllegalStateException("无法创建OpenAL上下文");
            }
            
            // 激活上下文
            ALC10.alcMakeContextCurrent(context);
            
            // 初始化AL
            AL.createCapabilities(ALC.createCapabilities(device));
            
            // 检查OpenAL错误
            ALErrorChecker.checkError("ALAudioManager.init() - After initialization");
            
            initialized = true;
            LogUtil.info("ALAudioManager", "OpenAL环境初始化成功");
        } catch (Exception e) {
            LogUtil.error("ALAudioManager", "OpenAL环境初始化失败", e);
            cleanup();
        }
    }
    
    @Override
    public void loadMusic(String name, String path) {
        if (!initialized) {
            init();
        }
        
        try {
            // 如果已经加载过，直接返回
            if (musicBuffers.containsKey(name)) {
                return;
            }
            
            // 加载音频文件
            int bufferId = loadAudioFile(path);
            if (bufferId == -1) {
                return;
            }
            
            // 创建源
            int sourceId = AL10.alGenSources();
            ALErrorChecker.checkError("ALAudioManager.loadMusic() - After generating source");
            AL10.alSourcei(sourceId, AL10.AL_BUFFER, bufferId);
            ALErrorChecker.checkError("ALAudioManager.loadMusic() - After setting buffer");
            AL10.alSourcef(sourceId, AL10.AL_GAIN, musicVolume);
            ALErrorChecker.checkError("ALAudioManager.loadMusic() - After setting gain");
            
            // 存储
            musicBuffers.put(name, bufferId);
            musicSources.put(name, sourceId);
            musicPaths.put(name, path);
            
            LogUtil.info("ALAudioManager", "音乐加载成功: " + name);
        } catch (Exception e) {
            LogUtil.error("ALAudioManager", "加载音乐失败: " + name, e);
        }
    }
    
    @Override
    public void loadSound(String name, String path) {
        System.out.println("开始加载音效: " + name + " 路径: " + path);
        if (!initialized) {
            System.out.println("音频管理器未初始化，正在初始化...");
            init();
            System.out.println("音频管理器初始化完成: " + initialized);
        }
        
        try {
            // 如果已经加载过，直接返回
            if (soundBuffers.containsKey(name)) {
                System.out.println("音效已加载: " + name);
                return;
            }
            
            // 加载音频文件
            System.out.println("加载音频文件: " + path);
            int bufferId = loadAudioFile(path);
            if (bufferId == -1) {
                System.out.println("加载音频文件失败: " + path);
                return;
            }
            System.out.println("音频文件加载成功，缓冲区ID: " + bufferId);
            
            // 创建源池
            ConcurrentLinkedQueue<Integer> sourcePool = new ConcurrentLinkedQueue<>();
            
            // 为每个音效创建多个源
            System.out.println("创建源池，大小: " + DEFAULT_SOUND_SOURCES_PER_SOUND);
            for (int i = 0; i < DEFAULT_SOUND_SOURCES_PER_SOUND; i++) {
                int sourceId = AL10.alGenSources();
                System.out.println("创建源 " + i + ": " + sourceId);
                ALErrorChecker.checkError("ALAudioManager.loadSound() - After generating source");
                AL10.alSourcei(sourceId, AL10.AL_BUFFER, bufferId);
                ALErrorChecker.checkError("ALAudioManager.loadSound() - After setting buffer");
                AL10.alSourcef(sourceId, AL10.AL_GAIN, soundVolume);
                ALErrorChecker.checkError("ALAudioManager.loadSound() - After setting gain");
                sourcePool.offer(sourceId);
                System.out.println("源 " + sourceId + " 添加到池");
            }
            
            // 存储
            soundBuffers.put(name, bufferId);
            soundSources.put(name, sourcePool);
            soundPaths.put(name, path);
            
            System.out.println("音效加载成功: " + name + " (" + DEFAULT_SOUND_SOURCES_PER_SOUND + "个源)");
        } catch (Exception e) {
            System.out.println("加载音效失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void playMusic(String name, boolean loop) {
        if (!initialized || !musicSources.containsKey(name)) {
            return;
        }
        
        try {
            int sourceId = musicSources.get(name);
            
            // 停止当前播放
            AL10.alSourceStop(sourceId);
            ALErrorChecker.checkError("ALAudioManager.playMusic() - After stopping source");
            AL10.alSourceRewind(sourceId);
            ALErrorChecker.checkError("ALAudioManager.playMusic() - After rewinding source");
            
            // 设置循环
            AL10.alSourcei(sourceId, AL10.AL_LOOPING, loop ? 1 : 0);
            ALErrorChecker.checkError("ALAudioManager.playMusic() - After setting looping");
            
            // 播放
            AL10.alSourcePlay(sourceId);
            ALErrorChecker.checkError("ALAudioManager.playMusic() - After playing source");
            
            LogUtil.info("ALAudioManager", "播放音乐: " + name + (loop ? " (循环)" : ""));
        } catch (Exception e) {
            LogUtil.error("ALAudioManager", "播放音乐失败: " + name, e);
        }
    }
    
    @Override
    public void playSound(String name) {
        System.out.println("开始播放音效: " + name);
        if (!initialized) {
            System.out.println("音频管理器未初始化");
            return;
        }
        
        if (!soundSources.containsKey(name)) {
            System.out.println("音效不存在: " + name);
            return;
        }
        
        try {
            ConcurrentLinkedQueue<Integer> sourcePool = soundSources.get(name);
            System.out.println("音效源池大小: " + sourcePool.size());
            
            Integer sourceId = null;
            
            // 查找可用的源
            for (Integer id : sourcePool) {
                int state = AL10.alGetSourcei(id, AL10.AL_SOURCE_STATE);
                System.out.println("源ID: " + id + " 状态: " + state);
                if (state != AL10.AL_PLAYING) {
                    sourceId = id;
                    System.out.println("找到可用源: " + id);
                    break;
                }
            }
            
            // 如果没有可用源，创建新的源
            if (sourceId == null) {
                System.out.println("没有可用源，创建新源");
                int bufferId = soundBuffers.get(name);
                System.out.println("缓冲区ID: " + bufferId);
                sourceId = AL10.alGenSources();
                System.out.println("新源ID: " + sourceId);
                ALErrorChecker.checkError("ALAudioManager.playSound() - After generating source");
                AL10.alSourcei(sourceId, AL10.AL_BUFFER, bufferId);
                ALErrorChecker.checkError("ALAudioManager.playSound() - After setting buffer");
                AL10.alSourcef(sourceId, AL10.AL_GAIN, soundVolume);
                ALErrorChecker.checkError("ALAudioManager.playSound() - After setting gain");
                sourcePool.offer(sourceId);
                System.out.println("新源添加到池: " + sourceId);
            }
            
            // 重置并播放
            System.out.println("重置源: " + sourceId);
            AL10.alSourceStop(sourceId);
            ALErrorChecker.checkError("ALAudioManager.playSound() - After stopping source");
            AL10.alSourceRewind(sourceId);
            ALErrorChecker.checkError("ALAudioManager.playSound() - After rewinding source");
            
            // 播放
            System.out.println("开始播放: " + sourceId);
            AL10.alSourcePlay(sourceId);
            ALErrorChecker.checkError("ALAudioManager.playSound() - After playing source");
            
            int state = AL10.alGetSourcei(sourceId, AL10.AL_SOURCE_STATE);
            System.out.println("播放后状态: " + state);
            System.out.println("音效播放完成: " + name);
        } catch (Exception e) {
            System.out.println("播放音效失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void stopMusic(String name) {
        if (!initialized || !musicSources.containsKey(name)) {
            return;
        }
        
        try {
            int sourceId = musicSources.get(name);
            AL10.alSourceStop(sourceId);
            ALErrorChecker.checkError("ALAudioManager.stopMusic() - After stopping source");
            AL10.alSourceRewind(sourceId);
            ALErrorChecker.checkError("ALAudioManager.stopMusic() - After rewinding source");
            
            LogUtil.info("ALAudioManager", "停止音乐: " + name);
        } catch (Exception e) {
            LogUtil.error("ALAudioManager", "停止音乐失败: " + name, e);
        }
    }
    
    @Override
    public void stopAllSounds() {
        if (!initialized) {
            return;
        }
        
        try {
            for (ConcurrentLinkedQueue<Integer> sourcePool : soundSources.values()) {
                for (Integer sourceId : sourcePool) {
                    AL10.alSourceStop(sourceId);
                    ALErrorChecker.checkError("ALAudioManager.stopAllSounds() - After stopping source");
                    AL10.alSourceRewind(sourceId);
                    ALErrorChecker.checkError("ALAudioManager.stopAllSounds() - After rewinding source");
                }
            }
            
            LogUtil.info("ALAudioManager", "停止所有音效");
        } catch (Exception e) {
            LogUtil.error("ALAudioManager", "停止所有音效失败", e);
        }
    }
    
    @Override
    public void setMusicVolume(float volume) {
        musicVolume = Math.max(0.0f, Math.min(1.0f, volume));
        
        if (!initialized) {
            return;
        }
        
        try {
            for (Integer sourceId : musicSources.values()) {
                AL10.alSourcef(sourceId, AL10.AL_GAIN, musicVolume);
                ALErrorChecker.checkError("ALAudioManager.setMusicVolume() - After setting gain");
            }
            
            LogUtil.info("ALAudioManager", "设置音乐音量: " + musicVolume);
        } catch (Exception e) {
            LogUtil.error("ALAudioManager", "设置音乐音量失败", e);
        }
    }
    
    @Override
    public void setSoundVolume(float volume) {
        soundVolume = Math.max(0.0f, Math.min(1.0f, volume));
        
        if (!initialized) {
            return;
        }
        
        try {
            for (ConcurrentLinkedQueue<Integer> sourcePool : soundSources.values()) {
                for (Integer sourceId : sourcePool) {
                    AL10.alSourcef(sourceId, AL10.AL_GAIN, soundVolume);
                    ALErrorChecker.checkError("ALAudioManager.setSoundVolume() - After setting gain");
                }
            }
            
            LogUtil.info("ALAudioManager", "设置音效音量: " + soundVolume);
        } catch (Exception e) {
            LogUtil.error("ALAudioManager", "设置音效音量失败", e);
        }
    }
    
    @Override
    public void pauseMusic(String name) {
        if (!initialized || !musicSources.containsKey(name)) {
            return;
        }
        
        try {
            int sourceId = musicSources.get(name);
            AL10.alSourcePause(sourceId);
            ALErrorChecker.checkError("ALAudioManager.pauseMusic() - After pausing source");
            
            LogUtil.info("ALAudioManager", "暂停音乐: " + name);
        } catch (Exception e) {
            LogUtil.error("ALAudioManager", "暂停音乐失败: " + name, e);
        }
    }
    
    @Override
    public void resumeMusic(String name) {
        if (!initialized || !musicSources.containsKey(name)) {
            return;
        }
        
        try {
            int sourceId = musicSources.get(name);
            AL10.alSourcePlay(sourceId);
            ALErrorChecker.checkError("ALAudioManager.resumeMusic() - After playing source");
            
            LogUtil.info("ALAudioManager", "恢复音乐: " + name);
        } catch (Exception e) {
            LogUtil.error("ALAudioManager", "恢复音乐失败: " + name, e);
        }
    }
    
    @Override
    public void unloadMusic(String name) {
        if (!initialized || !musicBuffers.containsKey(name)) {
            return;
        }
        
        try {
            // 停止播放
            stopMusic(name);
            
            // 删除源
            int sourceId = musicSources.get(name);
            AL10.alDeleteSources(sourceId);
            ALErrorChecker.checkError("ALAudioManager.unloadMusic() - After deleting source");
            
            // 删除缓冲区
            int bufferId = musicBuffers.get(name);
            AL10.alDeleteBuffers(bufferId);
            ALErrorChecker.checkError("ALAudioManager.unloadMusic() - After deleting buffer");
            
            // 清理映射
            musicSources.remove(name);
            musicBuffers.remove(name);
            musicPaths.remove(name);
            
            LogUtil.info("ALAudioManager", "卸载音乐: " + name);
        } catch (Exception e) {
            LogUtil.error("ALAudioManager", "卸载音乐失败: " + name, e);
        }
    }
    
    @Override
    public void unloadSound(String name) {
        if (!initialized || !soundBuffers.containsKey(name)) {
            return;
        }
        
        try {
            // 停止并删除所有源
            ConcurrentLinkedQueue<Integer> sourcePool = soundSources.get(name);
            for (Integer sourceId : sourcePool) {
                AL10.alSourceStop(sourceId);
                ALErrorChecker.checkError("ALAudioManager.unloadSound() - After stopping source");
                AL10.alSourceRewind(sourceId);
                ALErrorChecker.checkError("ALAudioManager.unloadSound() - After rewinding source");
                AL10.alDeleteSources(sourceId);
                ALErrorChecker.checkError("ALAudioManager.unloadSound() - After deleting source");
            }
            
            // 删除缓冲区
            int bufferId = soundBuffers.get(name);
            AL10.alDeleteBuffers(bufferId);
            ALErrorChecker.checkError("ALAudioManager.unloadSound() - After deleting buffer");
            
            // 清理映射
            soundSources.remove(name);
            soundBuffers.remove(name);
            soundPaths.remove(name);
            
            LogUtil.info("ALAudioManager", "卸载音效: " + name + " (" + sourcePool.size() + "个源)");
        } catch (Exception e) {
            LogUtil.error("ALAudioManager", "卸载音效失败: " + name, e);
        }
    }
    
    @Override
    public void unloadAll() {
        if (!initialized) {
            return;
        }
        
        try {
            // 卸载所有音乐
            for (String name : new HashMap<>(musicBuffers).keySet()) {
                unloadMusic(name);
            }
            
            // 卸载所有音效
            for (String name : new HashMap<>(soundBuffers).keySet()) {
                unloadSound(name);
            }
            
            LogUtil.info("ALAudioManager", "卸载所有音频资源");
        } catch (Exception e) {
            LogUtil.error("ALAudioManager", "卸载所有音频资源失败", e);
        }
    }
    
    @Override
    public void cleanup() {
        if (!initialized) {
            return;
        }
        
        try {
            // 卸载所有资源
            unloadAll();
            
            // 清理上下文
            ALC10.alcMakeContextCurrent(MemoryUtil.NULL);
            if (context != MemoryUtil.NULL) {
                ALC10.alcDestroyContext(context);
                context = MemoryUtil.NULL;
            }
            
            // 关闭设备
            if (device != MemoryUtil.NULL) {
                ALC10.alcCloseDevice(device);
                device = MemoryUtil.NULL;
            }
            
            initialized = false;
            LogUtil.info("ALAudioManager", "OpenAL环境清理成功");
        } catch (Exception e) {
            LogUtil.error("ALAudioManager", "OpenAL环境清理失败", e);
        }
    }
    
    @Override
    public boolean isInitialized() {
        return initialized;
    }
    
    /**
     * 加载音频文件
     */
    private int loadAudioFile(String path) {
        ByteBuffer data = null;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            // 读取文件
            data = readFileToBuffer(path);
            if (data == null) {
                return -1;
            }
            
            // 检查文件类型并加载
            if (path.toLowerCase().endsWith(".ogg")) {
                int bufferId = loadOggFile(data, stack);
                ALErrorChecker.checkError("ALAudioManager.loadAudioFile() - After loading OGG file");
                return bufferId;
            } else if (path.toLowerCase().endsWith(".wav")) {
                int bufferId = loadWavFile(data, stack);
                ALErrorChecker.checkError("ALAudioManager.loadAudioFile() - After loading WAV file");
                return bufferId;
            } else {
                LogUtil.error("ALAudioManager", "不支持的音频格式: " + path);
                return -1;
            }
        } catch (Exception e) {
            LogUtil.error("ALAudioManager", "加载音频文件失败: " + path, e);
            return -1;
        } finally {
            if (data != null) {
                MemoryUtil.memFree(data);
            }
        }
    }
    
    /**
     * 读取文件到ByteBuffer
     */
    private ByteBuffer readFileToBuffer(String path) {
        ByteBuffer buffer = null;
        boolean success = false;
        try {
            // 尝试从文件系统直接读取
            java.nio.file.Path filePath = java.nio.file.Paths.get(path);
            System.out.println("读取文件: " + path);
            System.out.println("文件存在: " + java.nio.file.Files.exists(filePath));
            if (!java.nio.file.Files.exists(filePath)) {
                // 如果文件不存在，尝试从类路径读取
                System.out.println("文件系统中找不到音频文件，尝试从类路径读取: " + path);
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
                if (inputStream == null) {
                    System.out.println("音频文件不存在: " + path);
                    return null;
                }
                
                byte[] bytes = inputStream.readAllBytes();
                inputStream.close();
                System.out.println("从类路径读取音频文件: " + path + " (" + bytes.length + " bytes)");
                
                buffer = MemoryUtil.memAlloc(bytes.length);
                buffer.put(bytes);
                buffer.flip();
                System.out.println("缓冲区大小: " + buffer.limit());
            } else {
                // 从文件系统直接读取
                byte[] bytes = java.nio.file.Files.readAllBytes(filePath);
                System.out.println("从文件系统读取音频文件: " + path + " (" + bytes.length + " bytes)");
                
                buffer = MemoryUtil.memAlloc(bytes.length);
                buffer.put(bytes);
                buffer.flip();
                System.out.println("缓冲区大小: " + buffer.limit());
                // 打印文件头信息
                if (buffer.limit() >= 4) {
                    int header = buffer.getInt(0);
                    System.out.println("文件头: 0x" + Integer.toHexString(header));
                    buffer.position(0); // 重置位置
                }
            }
            
            success = true;
            return buffer;
        } catch (IOException e) {
            System.out.println("读取音频文件失败: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            if (!success && buffer != null) {
                MemoryUtil.memFree(buffer);
            }
        }
    }
    
    /**
     * 加载OGG文件
     */
    private int loadOggFile(ByteBuffer data, MemoryStack stack) {
        IntBuffer error = stack.mallocInt(1);
        long decoder = STBVorbis.stb_vorbis_open_memory(data, error, null);
        
        if (decoder == MemoryUtil.NULL) {
            LogUtil.error("ALAudioManager", "解码OGG文件失败，错误码: " + error.get(0));
            return -1;
        }
        
        ShortBuffer pcm = null;
        try {
            STBVorbisInfo info = STBVorbisInfo.malloc(stack);
            STBVorbis.stb_vorbis_get_info(decoder, info);
            int channels = info.channels();
            int sampleRate = info.sample_rate();
            LogUtil.info("ALAudioManager", "音频信息: channels=" + channels + ", sampleRate=" + sampleRate);
            
            // 计算总样本数
            int totalSamples = STBVorbis.stb_vorbis_stream_length_in_samples(decoder);
            LogUtil.info("ALAudioManager", "总样本数: " + totalSamples);
            
            // 为了避免内存溢出，限制最大样本数
            int maxSamples = 44100 * 60; // 最多1分钟
            int samplesToRead = Math.min(totalSamples, maxSamples);
            LogUtil.info("ALAudioManager", "实际读取样本数: " + samplesToRead);
            
            // 分配缓冲区（使用堆内存而不是栈内存）
            pcm = MemoryUtil.memAllocShort(samplesToRead * channels);
            
            // 解码
            int samplesDecoded = STBVorbis.stb_vorbis_get_samples_short_interleaved(decoder, channels, pcm);
            LogUtil.info("ALAudioManager", "成功读取样本数: " + samplesDecoded);
            
            if (samplesDecoded <= 0) {
                LogUtil.error("ALAudioManager", "无法读取音频样本");
                return -1;
            }
            
            // 手动设置缓冲区的位置和限制
            pcm.position(0);
            pcm.limit(samplesDecoded * channels);
            LogUtil.debug("ALAudioManager", "手动设置后 - PCM缓冲区位置: " + pcm.position());
            LogUtil.debug("ALAudioManager", "手动设置后 - PCM缓冲区限制: " + pcm.limit());
            
            // 创建OpenAL缓冲区
            int bufferId = AL10.alGenBuffers();
            ALErrorChecker.checkError("ALAudioManager.loadOggFile() - After generating buffer");
            LogUtil.info("ALAudioManager", "创建的缓冲区ID: " + bufferId);
            
            // 设置格式
            int format = channels == 1 ? AL10.AL_FORMAT_MONO16 : AL10.AL_FORMAT_STEREO16;
            LogUtil.info("ALAudioManager", "使用的音频格式: " + format);
            LogUtil.debug("ALAudioManager", "PCM缓冲区容量: " + pcm.capacity());
            LogUtil.debug("ALAudioManager", "PCM缓冲区位置: " + pcm.position());
            LogUtil.debug("ALAudioManager", "PCM缓冲区限制: " + pcm.limit());
            
            // 上传数据
            AL10.alBufferData(bufferId, format, pcm, sampleRate);
            ALErrorChecker.checkError("ALAudioManager.loadOggFile() - After uploading buffer data");
            
            // 检查缓冲区填充错误
            int bufferError = AL10.alGetError();
            if (bufferError != AL10.AL_NO_ERROR) {
                LogUtil.error("ALAudioManager", "填充缓冲区错误: " + bufferError);
                AL10.alDeleteBuffers(bufferId);
                return -1;
            }
            
            // 检查缓冲区信息
            int[] bufferSize = new int[1];
            int[] bufferFrequency = new int[1];
            AL10.alGetBufferi(bufferId, AL10.AL_SIZE, bufferSize);
            AL10.alGetBufferi(bufferId, AL10.AL_FREQUENCY, bufferFrequency);
            LogUtil.info("ALAudioManager", "缓冲区信息: 大小=" + bufferSize[0] + ", 频率=" + bufferFrequency[0]);
            
            return bufferId;
        } catch (Exception e) {
            LogUtil.error("ALAudioManager", "处理OGG文件失败: " + e.getMessage(), e);
            return -1;
        } finally {
            STBVorbis.stb_vorbis_close(decoder);
            // 释放缓冲区
            if (pcm != null) {
                MemoryUtil.memFree(pcm);
            }
        }
    }
    
    /**
     * 加载WAV文件
     */
    private int loadWavFile(ByteBuffer data, MemoryStack stack) {
        // 简单的WAV文件解析
        // 这里只处理PCM格式的WAV文件
        try {
            // 检查RIFF头（考虑小端字节序）
            int riffHeader = data.getInt();
            // 小端字节序转换
            riffHeader = ((riffHeader & 0xFF) << 24) | ((riffHeader & 0xFF00) << 8) | ((riffHeader & 0xFF0000) >> 8) | ((riffHeader & 0xFF000000) >>> 24);
            if (riffHeader != 0x52494646) { // RIFF
                LogUtil.error("ALAudioManager", "无效的WAV文件: 缺少RIFF头");
                return -1;
            }
            
            data.getInt(); // 文件大小
            
            // 检查WAVE头（考虑小端字节序）
            int waveHeader = data.getInt();
            // 小端字节序转换
            waveHeader = ((waveHeader & 0xFF) << 24) | ((waveHeader & 0xFF00) << 8) | ((waveHeader & 0xFF0000) >> 8) | ((waveHeader & 0xFF000000) >>> 24);
            if (waveHeader != 0x57415645) { // WAVE
                LogUtil.error("ALAudioManager", "无效的WAV文件: 缺少WAVE头");
                return -1;
            }
            
            // 寻找fmt chunk
            while (true) {
                int chunkHeader = data.getInt();
                // 小端字节序转换
                chunkHeader = ((chunkHeader & 0xFF) << 24) | ((chunkHeader & 0xFF00) << 8) | ((chunkHeader & 0xFF0000) >> 8) | ((chunkHeader & 0xFF000000) >>> 24);
                if (chunkHeader == 0x666d7420) { // fmt
                    break;
                }
                int chunkSize = data.getInt();
                data.position(data.position() + chunkSize);
                if (data.position() >= data.limit()) {
                    LogUtil.error("ALAudioManager", "无效的WAV文件: 缺少fmt chunk");
                    return -1;
                }
            }
            
            // 读取fmt chunk
            int fmtChunkSize = data.getInt();
            short format = data.getShort();
            short channels = data.getShort();
            int sampleRate = data.getInt();
            data.getInt(); // 字节率
            data.getShort(); // 块对齐
            short bitsPerSample = data.getShort();
            
            // 只支持PCM格式
            if (format != 1) {
                LogUtil.error("ALAudioManager", "不支持的WAV格式: " + format);
                return -1;
            }
            
            // 寻找data chunk
            while (true) {
                int chunkHeader = data.getInt();
                // 小端字节序转换
                chunkHeader = ((chunkHeader & 0xFF) << 24) | ((chunkHeader & 0xFF00) << 8) | ((chunkHeader & 0xFF0000) >> 8) | ((chunkHeader & 0xFF000000) >>> 24);
                if (chunkHeader == 0x64617461) { // data
                    break;
                }
                int chunkSize = data.getInt();
                data.position(data.position() + chunkSize);
                if (data.position() >= data.limit()) {
                    LogUtil.error("ALAudioManager", "无效的WAV文件: 缺少data chunk");
                    return -1;
                }
            }
            
            // 读取数据
            int dataSize = data.getInt();
            ByteBuffer pcmData = data.slice();
            pcmData.limit(dataSize);
            
            // 创建OpenAL缓冲区
            int bufferId = AL10.alGenBuffers();
            ALErrorChecker.checkError("ALAudioManager.loadWavFile() - After generating buffer");
            
            // 设置格式
            int audioFormat;
            if (channels == 1) {
                audioFormat = bitsPerSample == 16 ? AL10.AL_FORMAT_MONO16 : AL10.AL_FORMAT_MONO8;
            } else {
                audioFormat = bitsPerSample == 16 ? AL10.AL_FORMAT_STEREO16 : AL10.AL_FORMAT_STEREO8;
            }
            
            // 上传数据
            AL10.alBufferData(bufferId, audioFormat, pcmData, sampleRate);
            ALErrorChecker.checkError("ALAudioManager.loadWavFile() - After uploading buffer data");
            
            return bufferId;
        } catch (Exception e) {
            LogUtil.error("ALAudioManager", "解析WAV文件失败", e);
            return -1;
        }
    }
}
