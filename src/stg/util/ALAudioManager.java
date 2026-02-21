package stg.util;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.stb.STBVorbisInfo;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import stg.service.audio.IAudioManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;
import java.util.Map;

public class ALAudioManager implements IAudioManager {
    private static ALAudioManager instance;
    private long device;
    private long context;
    private boolean initialized;
    
    private final Map<String, Integer> musicBuffers = new HashMap<>();
    private final Map<String, Integer> soundBuffers = new HashMap<>();
    private final Map<String, Integer> musicSources = new HashMap<>();
    private final Map<String, Integer> soundSources = new HashMap<>();
    private final Map<String, String> musicPaths = new HashMap<>();
    private final Map<String, String> soundPaths = new HashMap<>();
    
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
            AL10.alSourcei(sourceId, AL10.AL_BUFFER, bufferId);
            AL10.alSourcef(sourceId, AL10.AL_GAIN, musicVolume);
            
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
        if (!initialized) {
            init();
        }
        
        try {
            // 如果已经加载过，直接返回
            if (soundBuffers.containsKey(name)) {
                return;
            }
            
            // 加载音频文件
            int bufferId = loadAudioFile(path);
            if (bufferId == -1) {
                return;
            }
            
            // 创建源
            int sourceId = AL10.alGenSources();
            AL10.alSourcei(sourceId, AL10.AL_BUFFER, bufferId);
            AL10.alSourcef(sourceId, AL10.AL_GAIN, soundVolume);
            
            // 存储
            soundBuffers.put(name, bufferId);
            soundSources.put(name, sourceId);
            soundPaths.put(name, path);
            
            LogUtil.info("ALAudioManager", "音效加载成功: " + name);
        } catch (Exception e) {
            LogUtil.error("ALAudioManager", "加载音效失败: " + name, e);
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
            AL10.alSourceRewind(sourceId);
            
            // 设置循环
            AL10.alSourcei(sourceId, AL10.AL_LOOPING, loop ? 1 : 0);
            
            // 播放
            AL10.alSourcePlay(sourceId);
            
            LogUtil.info("ALAudioManager", "播放音乐: " + name + (loop ? " (循环)" : ""));
        } catch (Exception e) {
            LogUtil.error("ALAudioManager", "播放音乐失败: " + name, e);
        }
    }
    
    @Override
    public void playSound(String name) {
        if (!initialized || !soundSources.containsKey(name)) {
            return;
        }
        
        try {
            int sourceId = soundSources.get(name);
            
            // 停止当前播放
            AL10.alSourceStop(sourceId);
            AL10.alSourceRewind(sourceId);
            
            // 播放
            AL10.alSourcePlay(sourceId);
            
            LogUtil.info("ALAudioManager", "播放音效: " + name);
        } catch (Exception e) {
            LogUtil.error("ALAudioManager", "播放音效失败: " + name, e);
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
            AL10.alSourceRewind(sourceId);
            
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
            for (Integer sourceId : soundSources.values()) {
                AL10.alSourceStop(sourceId);
                AL10.alSourceRewind(sourceId);
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
            for (Integer sourceId : soundSources.values()) {
                AL10.alSourcef(sourceId, AL10.AL_GAIN, soundVolume);
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
            
            // 删除缓冲区
            int bufferId = musicBuffers.get(name);
            AL10.alDeleteBuffers(bufferId);
            
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
            // 停止播放
            int sourceId = soundSources.get(name);
            AL10.alSourceStop(sourceId);
            AL10.alSourceRewind(sourceId);
            
            // 删除源
            AL10.alDeleteSources(sourceId);
            
            // 删除缓冲区
            int bufferId = soundBuffers.get(name);
            AL10.alDeleteBuffers(bufferId);
            
            // 清理映射
            soundSources.remove(name);
            soundBuffers.remove(name);
            soundPaths.remove(name);
            
            LogUtil.info("ALAudioManager", "卸载音效: " + name);
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
        try (MemoryStack stack = MemoryStack.stackPush()) {
            // 读取文件
            ByteBuffer data = readFileToBuffer(path);
            if (data == null) {
                return -1;
            }
            
            // 检查文件类型并加载
            if (path.toLowerCase().endsWith(".ogg")) {
                return loadOggFile(data, stack);
            } else if (path.toLowerCase().endsWith(".wav")) {
                return loadWavFile(data, stack);
            } else {
                LogUtil.error("ALAudioManager", "不支持的音频格式: " + path);
                return -1;
            }
        } catch (Exception e) {
            LogUtil.error("ALAudioManager", "加载音频文件失败: " + path, e);
            return -1;
        }
    }
    
    /**
     * 读取文件到ByteBuffer
     */
    private ByteBuffer readFileToBuffer(String path) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
            if (inputStream == null) {
                LogUtil.error("ALAudioManager", "音频文件不存在: " + path);
                return null;
            }
            
            byte[] bytes = inputStream.readAllBytes();
            inputStream.close();
            
            ByteBuffer buffer = MemoryUtil.memAlloc(bytes.length);
            buffer.put(bytes);
            buffer.flip();
            
            return buffer;
        } catch (IOException e) {
            LogUtil.error("ALAudioManager", "读取音频文件失败: " + path, e);
            return null;
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
        
        try {
            STBVorbisInfo info = STBVorbisInfo.malloc(stack);
            STBVorbis.stb_vorbis_get_info(decoder, info);
            int channels = info.channels();
            int sampleRate = info.sample_rate();
            
            // 计算总样本数
            int totalSamples = STBVorbis.stb_vorbis_stream_length_in_samples(decoder);
            
            // 分配缓冲区
            ShortBuffer pcm = stack.mallocShort(totalSamples * channels);
            
            // 解码
            int samplesDecoded = STBVorbis.stb_vorbis_get_samples_short_interleaved(decoder, channels, pcm);
            
            // 创建OpenAL缓冲区
            int bufferId = AL10.alGenBuffers();
            
            // 设置格式
            int format = channels == 1 ? AL10.AL_FORMAT_MONO16 : AL10.AL_FORMAT_STEREO16;
            
            // 上传数据
            AL10.alBufferData(bufferId, format, pcm, sampleRate);
            
            return bufferId;
        } finally {
            STBVorbis.stb_vorbis_close(decoder);
        }
    }
    
    /**
     * 加载WAV文件
     */
    private int loadWavFile(ByteBuffer data, MemoryStack stack) {
        // 简单的WAV文件解析
        // 这里只处理PCM格式的WAV文件
        try {
            // 检查RIFF头
            if (data.getInt() != 0x52494646) { // RIFF
                LogUtil.error("ALAudioManager", "无效的WAV文件: 缺少RIFF头");
                return -1;
            }
            
            data.getInt(); // 文件大小
            
            // 检查WAVE头
            if (data.getInt() != 0x57415645) { // WAVE
                LogUtil.error("ALAudioManager", "无效的WAV文件: 缺少WAVE头");
                return -1;
            }
            
            // 寻找fmt chunk
            while (data.getInt() != 0x666d7420) { // fmt
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
            while (data.getInt() != 0x64617461) { // data
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
            
            // 设置格式
            int audioFormat;
            if (channels == 1) {
                audioFormat = bitsPerSample == 16 ? AL10.AL_FORMAT_MONO16 : AL10.AL_FORMAT_MONO8;
            } else {
                audioFormat = bitsPerSample == 16 ? AL10.AL_FORMAT_STEREO16 : AL10.AL_FORMAT_STEREO8;
            }
            
            // 上传数据
            AL10.alBufferData(bufferId, audioFormat, pcmData, sampleRate);
            
            return bufferId;
        } catch (Exception e) {
            LogUtil.error("ALAudioManager", "解析WAV文件失败", e);
            return -1;
        }
    }
}
