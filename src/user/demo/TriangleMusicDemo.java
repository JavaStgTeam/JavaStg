package user.demo;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.stb.STBVorbisInfo;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TriangleMusicDemo {
    private long window;
    private int width = 800;
    private int height = 600;
    
    private long alDevice;
    private long alContext;
    private int musicSource;
    private int musicBuffer;
    private float musicVolume = 0.5f;
    private boolean musicPlaying = false;
    private String musicPath = "e:\\Myproject\\Game\\jstg_Team\\JavaStg\\resources\\audio\\music\\luastg 0.08.540 - 1.27.800.ogg";

    public static void main(String[] args) {
        TriangleMusicDemo demo = new TriangleMusicDemo();
        demo.run();
    }

    public void run() {
        init();
        loop();
        cleanup();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("无法初始化GLFW");
        }

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);

        window = GLFW.glfwCreateWindow(width, height, "三角形音乐演示", 0, 0);
        if (window == 0) {
            throw new RuntimeException("无法创建GLFW窗口");
        }

        GLFW.glfwSetKeyCallback(window, (win, key, scancode, action, mods) -> {
            if (action == GLFW.GLFW_PRESS) {
                switch (key) {
                    case GLFW.GLFW_KEY_ESCAPE -> GLFW.glfwSetWindowShouldClose(window, true);
                    case GLFW.GLFW_KEY_M -> toggleMusic();
                    case GLFW.GLFW_KEY_COMMA -> adjustVolume(-0.1f);
                    case GLFW.GLFW_KEY_PERIOD -> adjustVolume(0.1f);
                    case GLFW.GLFW_KEY_H -> showHelp();
                }
            }
        });

        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();

        GLFW.glfwSwapInterval(1);

        initAudio();

        long monitor = GLFW.glfwGetPrimaryMonitor();
        if (monitor != 0) {
            org.lwjgl.glfw.GLFWVidMode vidMode = GLFW.glfwGetVideoMode(monitor);
            if (vidMode != null) {
                GLFW.glfwSetWindowPos(window, 
                    (vidMode.width() - width) / 2, 
                    (vidMode.height() - height) / 2);
            }
        }

        GLFW.glfwShowWindow(window);
        
        System.out.println("========================================");
        System.out.println("三角形音乐演示");
        System.out.println("========================================");
        System.out.println("按 H 键查看帮助信息");
        System.out.println("========================================");
    }
    
    private void initAudio() {
        try {
            System.out.println("初始化 OpenAL...");
            alDevice = ALC10.alcOpenDevice((ByteBuffer) null);
            if (alDevice == MemoryUtil.NULL) {
                System.err.println("无法打开OpenAL设备");
                return;
            }
            
            alContext = ALC10.alcCreateContext(alDevice, (IntBuffer) null);
            if (alContext == MemoryUtil.NULL) {
                ALC10.alcCloseDevice(alDevice);
                System.err.println("无法创建OpenAL上下文");
                return;
            }
            
            ALC10.alcMakeContextCurrent(alContext);
            AL.createCapabilities(ALC.createCapabilities(alDevice));
            System.out.println("OpenAL 初始化完成");
            
            // 加载并播放OGG文件
            loadAndPlayOGG();
            
        } catch (Exception e) {
            System.err.println("初始化音频失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void loadAndPlayOGG() {
        try {
            System.out.println("加载OGG文件: " + musicPath);
            byte[] bytes = Files.readAllBytes(Paths.get(musicPath));
            System.out.println("文件大小: " + bytes.length + " bytes");
            
            ByteBuffer vorbisData = MemoryUtil.memAlloc(bytes.length);
            vorbisData.put(bytes);
            vorbisData.flip();
            
            try (MemoryStack stack = MemoryStack.stackPush()) {
                IntBuffer error = stack.mallocInt(1);
                long vorbisDecoder = STBVorbis.stb_vorbis_open_memory(vorbisData, error, null);
                
                if (vorbisDecoder == MemoryUtil.NULL) {
                    System.err.println("解码OGG文件失败，错误码: " + error.get(0));
                    MemoryUtil.memFree(vorbisData);
                    return;
                }
                
                STBVorbisInfo info = STBVorbisInfo.malloc(stack);
                STBVorbis.stb_vorbis_get_info(vorbisDecoder, info);
                int channels = info.channels();
                int sampleRate = info.sample_rate();
                System.out.println("音频信息: channels=" + channels + ", sampleRate=" + sampleRate);
                
                // 计算总样本数
                int totalSamples = STBVorbis.stb_vorbis_stream_length_in_samples(vorbisDecoder);
                System.out.println("总样本数: " + totalSamples);
                
                // 为了避免内存溢出，限制最大样本数
                int maxSamples = 44100 * 60; // 最多1分钟
                int samplesToRead = Math.min(totalSamples, maxSamples);
                System.out.println("实际读取样本数: " + samplesToRead);
                
                // 创建缓冲区
                ShortBuffer pcm = MemoryUtil.memAllocShort(samplesToRead * channels);
                
                // 读取所有样本
                int samplesRead = STBVorbis.stb_vorbis_get_samples_short_interleaved(
                    vorbisDecoder, channels, pcm);
                
                System.out.println("成功读取样本数: " + samplesRead);
                
                if (samplesRead <= 0) {
                    System.err.println("无法读取音频样本");
                    MemoryUtil.memFree(pcm);
                    STBVorbis.stb_vorbis_close(vorbisDecoder);
                    MemoryUtil.memFree(vorbisData);
                    return;
                }
                
                // 手动设置缓冲区的位置和限制
                pcm.position(0);
                pcm.limit(samplesRead * channels);
                System.out.println("手动设置后 - PCM缓冲区位置: " + pcm.position());
                System.out.println("手动设置后 - PCM缓冲区限制: " + pcm.limit());
                
                // 创建OpenAL缓冲区和源
                musicBuffer = AL10.alGenBuffers();
                musicSource = AL10.alGenSources();
                
                System.out.println("创建的缓冲区ID: " + musicBuffer);
                System.out.println("创建的源ID: " + musicSource);
                
                // 填充缓冲区
                int format = channels == 1 ? AL10.AL_FORMAT_MONO16 : AL10.AL_FORMAT_STEREO16;
                System.out.println("使用的音频格式: " + format);
                System.out.println("PCM缓冲区容量: " + pcm.capacity());
                System.out.println("PCM缓冲区位置: " + pcm.position());
                System.out.println("PCM缓冲区限制: " + pcm.limit());
                
                AL10.alBufferData(musicBuffer, format, pcm, sampleRate);
                
                // 检查缓冲区填充错误
                int bufferError = AL10.alGetError();
                if (bufferError != AL10.AL_NO_ERROR) {
                    System.err.println("填充缓冲区错误: " + bufferError);
                    return;
                }
                
                // 检查缓冲区信息
                int[] bufferSize = new int[1];
                int[] bufferFrequency = new int[1];
                AL10.alGetBufferi(musicBuffer, AL10.AL_SIZE, bufferSize);
                AL10.alGetBufferi(musicBuffer, AL10.AL_FREQUENCY, bufferFrequency);
                System.out.println("缓冲区信息: 大小=" + bufferSize[0] + ", 频率=" + bufferFrequency[0]);
                
                // 设置源属性
                AL10.alSourcei(musicSource, AL10.AL_BUFFER, musicBuffer);
                AL10.alSourcef(musicSource, AL10.AL_GAIN, musicVolume);
                AL10.alSourcei(musicSource, AL10.AL_LOOPING, AL10.AL_TRUE);
                
                // 检查源属性设置错误
                int sourceError = AL10.alGetError();
                if (sourceError != AL10.AL_NO_ERROR) {
                    System.err.println("设置源属性错误: " + sourceError);
                    return;
                }
                
                // 开始播放
                System.out.println("尝试播放音频...");
                AL10.alSourcePlay(musicSource);
                
                // 检查播放错误
                int playError = AL10.alGetError();
                if (playError != AL10.AL_NO_ERROR) {
                    System.err.println("播放音频错误: " + playError);
                    return;
                }
                
                // 检查音频状态
                int state = AL10.alGetSourcei(musicSource, AL10.AL_SOURCE_STATE);
                System.out.println("音频初始状态: " + getSourceStateString(state));
                
                // 检查源的其他信息
                int[] sourceBuffersQueued = new int[1];
                int[] sourceBuffersProcessed = new int[1];
                AL10.alGetSourcei(musicSource, AL10.AL_BUFFERS_QUEUED, sourceBuffersQueued);
                AL10.alGetSourcei(musicSource, AL10.AL_BUFFERS_PROCESSED, sourceBuffersProcessed);
                System.out.println("源信息: 队列缓冲区数=" + sourceBuffersQueued[0] + ", 处理缓冲区数=" + sourceBuffersProcessed[0]);
                
                musicPlaying = true;
                System.out.println("OGG文件播放已启动");
                
                // 清理资源
                MemoryUtil.memFree(pcm);
                STBVorbis.stb_vorbis_close(vorbisDecoder);
                MemoryUtil.memFree(vorbisData);
                
            } catch (Exception e) {
                System.err.println("处理OGG文件失败: " + e.getMessage());
                e.printStackTrace();
            }
            
        } catch (Exception e) {
            System.err.println("加载OGG文件失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void toggleMusic() {
        if (musicSource == 0) return;
        
        if (musicPlaying) {
            AL10.alSourcePause(musicSource);
            musicPlaying = false;
            System.out.println("音乐已暂停");
        } else {
            AL10.alSourcePlay(musicSource);
            musicPlaying = true;
            System.out.println("音乐已恢复");
        }
    }
    
    private void adjustVolume(float delta) {
        musicVolume = Math.max(0.0f, Math.min(1.0f, musicVolume + delta));
        if (musicSource != 0) {
            AL10.alSourcef(musicSource, AL10.AL_GAIN, musicVolume);
        }
        System.out.printf("音量: %.0f%%%n", musicVolume * 100);
    }
    
    private void showHelp() {
        System.out.println("\n========================================");
        System.out.println("快捷键帮助");
        System.out.println("========================================");
        System.out.println("M - 暂停/恢复音乐");
        System.out.println(", / . - 降低/提高音量");
        System.out.println("ESC - 退出程序");
        System.out.println("H - 显示此帮助");
        System.out.println("========================================\n");
    }

    private void loop() {
        System.out.println("进入主循环...");
        int frameCount = 0;
        while (!GLFW.glfwWindowShouldClose(window)) {
            GL11.glClearColor(0.1f, 0.2f, 0.3f, 1.0f);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

            renderTriangle();

            GLFW.glfwSwapBuffers(window);
            GLFW.glfwPollEvents();
            
            frameCount++;
            if (frameCount % 60 == 0) {
                System.out.println("运行中... 帧率: 60 FPS");
                if (musicSource != 0) {
                    int state = AL10.alGetSourcei(musicSource, AL10.AL_SOURCE_STATE);
                    System.out.println("音频状态: " + getSourceStateString(state));
                }
            }
        }
        System.out.println("退出主循环");
    }

    private void renderTriangle() {
        GL11.glBegin(GL11.GL_TRIANGLES);
        GL11.glColor3f(1.0f, 0.0f, 0.0f);
        GL11.glVertex2f(0.0f, 0.5f);
        GL11.glColor3f(0.0f, 1.0f, 0.0f);
        GL11.glVertex2f(-0.5f, -0.5f);
        GL11.glColor3f(0.0f, 0.0f, 1.0f);
        GL11.glVertex2f(0.5f, -0.5f);
        GL11.glEnd();
    }

    private void cleanup() {
        if (musicSource != 0) {
            AL10.alSourceStop(musicSource);
            AL10.alDeleteSources(musicSource);
        }
        if (musicBuffer != 0) {
            AL10.alDeleteBuffers(musicBuffer);
        }
        
        if (alContext != MemoryUtil.NULL) {
            ALC10.alcMakeContextCurrent(MemoryUtil.NULL);
            ALC10.alcDestroyContext(alContext);
        }
        if (alDevice != MemoryUtil.NULL) {
            ALC10.alcCloseDevice(alDevice);
        }

        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();

        GLFWErrorCallback callback = GLFW.glfwSetErrorCallback(null);
        if (callback != null) {
            callback.free();
        }
        
        System.out.println("程序已退出");
    }
    
    private String getSourceStateString(int state) {
        switch (state) {
            case AL10.AL_INITIAL: return "AL_INITIAL";
            case AL10.AL_PLAYING: return "AL_PLAYING";
            case AL10.AL_PAUSED: return "AL_PAUSED";
            case AL10.AL_STOPPED: return "AL_STOPPED";
            default: return "未知状态: " + state;
        }
    }
}