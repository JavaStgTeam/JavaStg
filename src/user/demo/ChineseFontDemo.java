package user.demo;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.stb.STBVorbisInfo;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public class ChineseFontDemo {
    private long window;
    private int width = 1000;
    private int height = 700;
    private STBTTFontinfo fontInfo;
    private ByteBuffer fontBuffer;
    private String fontPath = "e:\\Myproject\\Game\\jstg_Team\\JavaStg\\resources\\fonts\\OPPO Sans 4.0.ttf";
    private String musicPath = "e:\\Myproject\\Game\\jstg_Team\\JavaStg\\resources\\audio\\music\\luastg 0.08.540 - 1.27.800.ogg";
    
    private Map<Integer, GlyphCache> glyphCache = new HashMap<>();
    
    private float[] fontSizes = {24.0f, 36.0f, 48.0f, 64.0f};
    private int currentFontSizeIndex = 2;
    
    private float[][] colors = {
        {1.0f, 1.0f, 1.0f},
        {1.0f, 0.0f, 0.0f},
        {0.0f, 1.0f, 0.0f},
        {0.0f, 0.5f, 1.0f},
        {1.0f, 1.0f, 0.0f},
        {1.0f, 0.5f, 0.0f},
        {0.8f, 0.4f, 1.0f},
    };
    private int currentColorIndex = 0;
    
    private String[] demoTexts = {
        "汉字测试 Hello World 你好世界",
        "东方Project 弹幕游戏\n博丽灵梦 雾雨魔理沙",
        "春眠不觉晓，处处闻啼鸟。\n夜来风雨声，花落知多少。",
        "The quick brown fox\njumps over the lazy dog.\n敏捷的棕色狐狸跳过懒狗。",
        "1234567890\n+-*/=<>[]{}\n!@#$%^&*()",
    };
    private int currentTextIndex = 0;
    
    private float scrollY = 0;
    private float maxScrollY = 0;
    
    private long alDevice;
    private long alContext;
    private int musicSource;
    private int[] musicBuffers;
    private float musicVolume = 0.5f;
    private boolean musicPlaying = false;
    private long vorbisDecoder;
    private ByteBuffer vorbisData;
    private int vorbisChannels;
    private int vorbisSampleRate;
    private static final int BUFFER_SIZE = 44100 * 2;
    private static final int NUM_BUFFERS = 4;
    
    private static class GlyphCache {
        int textureId;
        int width;
        int height;
        int xOff;
        int yOff;
        int advanceWidth;
        
        GlyphCache(int textureId, int width, int height, int xOff, int yOff, int advanceWidth) {
            this.textureId = textureId;
            this.width = width;
            this.height = height;
            this.xOff = xOff;
            this.yOff = yOff;
            this.advanceWidth = advanceWidth;
        }
    }

    public static void main(String[] args) {
        ChineseFontDemo demo = new ChineseFontDemo();
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

        window = GLFW.glfwCreateWindow(width, height, "汉字渲染演示 - 按H查看帮助", 0, 0);
        if (window == 0) {
            throw new RuntimeException("无法创建GLFW窗口");
        }

        GLFW.glfwSetKeyCallback(window, this::handleKeyInput);
        
        GLFW.glfwSetScrollCallback(window, (win, xoffset, yoffset) -> {
            scrollY -= yoffset * 30;
            scrollY = Math.max(0, Math.min(scrollY, maxScrollY));
        });

        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();

        GLFW.glfwSwapInterval(1);

        initFont();
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
        System.out.println("汉字渲染演示程序");
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
            
            initVorbisStream();
            
        } catch (Exception e) {
            System.err.println("初始化音频失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void initVorbisStream() {
        try {
            System.out.println("加载音乐文件: " + musicPath);
            byte[] bytes = Files.readAllBytes(Paths.get(musicPath));
            System.out.println("文件大小: " + bytes.length + " bytes");
            
            vorbisData = MemoryUtil.memAlloc(bytes.length);
            vorbisData.put(bytes);
            vorbisData.flip();
            
            try (MemoryStack stack = MemoryStack.stackPush()) {
                IntBuffer error = stack.mallocInt(1);
                vorbisDecoder = STBVorbis.stb_vorbis_open_memory(vorbisData, error, null);
                
                if (vorbisDecoder == MemoryUtil.NULL) {
                    System.err.println("解码OGG文件失败，错误码: " + error.get(0));
                    return;
                }
                
                STBVorbisInfo info = STBVorbisInfo.malloc(stack);
                STBVorbis.stb_vorbis_get_info(vorbisDecoder, info);
                vorbisChannels = info.channels();
                vorbisSampleRate = info.sample_rate();
                System.out.println("音频信息: channels=" + vorbisChannels + ", sampleRate=" + vorbisSampleRate);
            }
            
            musicBuffers = new int[NUM_BUFFERS];
            for (int i = 0; i < NUM_BUFFERS; i++) {
                musicBuffers[i] = AL10.alGenBuffers();
                checkALError("生成缓冲区 " + i);
            }
            
            musicSource = AL10.alGenSources();
            checkALError("生成音频源");
            
            AL10.alSourcef(musicSource, AL10.AL_GAIN, musicVolume);
            AL10.alSourcei(musicSource, AL10.AL_LOOPING, AL10.AL_FALSE);
            AL10.alSourcef(musicSource, AL10.AL_PITCH, 1.0f);
            AL10.alSource3f(musicSource, AL10.AL_POSITION, 0.0f, 0.0f, 0.0f);
            AL10.alSource3f(musicSource, AL10.AL_VELOCITY, 0.0f, 0.0f, 0.0f);
            checkALError("设置音频源属性");
            
            for (int i = 0; i < NUM_BUFFERS; i++) {
                boolean success = streamBuffer(musicBuffers[i]);
                System.out.println("缓冲区 " + i + " 填充: " + (success ? "成功" : "失败"));
            }
            
            IntBuffer buffers = BufferUtils.createIntBuffer(NUM_BUFFERS);
            for (int i = 0; i < NUM_BUFFERS; i++) {
                buffers.put(musicBuffers[i]);
            }
            buffers.flip();
            AL10.alSourceQueueBuffers(musicSource, buffers);
            checkALError("队列缓冲区");
            
            int queued = AL10.alGetSourcei(musicSource, AL10.AL_BUFFERS_QUEUED);
            System.out.println("已队列缓冲区数: " + queued);
            
            AL10.alSourcePlay(musicSource);
            checkALError("播放");
            
            int state = AL10.alGetSourcei(musicSource, AL10.AL_SOURCE_STATE);
            System.out.println("播放后状态: " + stateString(state));
            
            musicPlaying = true;
            System.out.println("音乐播放已启动");
            
        } catch (Exception e) {
            System.err.println("加载音乐失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void checkALError(String context) {
        int error = AL10.alGetError();
        if (error != AL10.AL_NO_ERROR) {
            System.err.println("OpenAL 错误 [" + context + "]: " + error);
        }
    }
    
    private String stateString(int state) {
        return switch (state) {
            case AL10.AL_PLAYING -> "播放中";
            case AL10.AL_PAUSED -> "暂停";
            case AL10.AL_STOPPED -> "停止";
            case AL10.AL_INITIAL -> "初始";
            default -> "未知(" + state + ")";
        };
    }
    
    private boolean streamBuffer(int buffer) {
        ShortBuffer pcm = MemoryUtil.memAllocShort(BUFFER_SIZE * vorbisChannels);
        
        try {
            int samples = STBVorbis.stb_vorbis_get_samples_short_interleaved(
                vorbisDecoder, vorbisChannels, pcm);
            
            if (samples <= 0) {
                STBVorbis.stb_vorbis_seek_start(vorbisDecoder);
                samples = STBVorbis.stb_vorbis_get_samples_short_interleaved(
                    vorbisDecoder, vorbisChannels, pcm);
                if (samples <= 0) {
                    return false;
                }
            }
            
            pcm.flip();
            int format = vorbisChannels == 1 ? AL10.AL_FORMAT_MONO16 : AL10.AL_FORMAT_STEREO16;
            AL10.alBufferData(buffer, format, pcm, vorbisSampleRate);
            
            return true;
        } finally {
            MemoryUtil.memFree(pcm);
        }
    }
    
    private boolean debugAudioPrinted = false;
    
    private void updateAudio() {
        if (musicSource == 0 || !musicPlaying) return;
        
        int processed = AL10.alGetSourcei(musicSource, AL10.AL_BUFFERS_PROCESSED);
        
        if (!debugAudioPrinted) {
            int state = AL10.alGetSourcei(musicSource, AL10.AL_SOURCE_STATE);
            int queued = AL10.alGetSourcei(musicSource, AL10.AL_BUFFERS_QUEUED);
            System.out.println("音频更新: 状态=" + stateString(state) + ", 已处理=" + processed + ", 已队列=" + queued);
        }
        
        while (processed-- > 0) {
            int buffer = AL10.alSourceUnqueueBuffers(musicSource);
            if (streamBuffer(buffer)) {
                AL10.alSourceQueueBuffers(musicSource, buffer);
            }
        }
        
        int state = AL10.alGetSourcei(musicSource, AL10.AL_SOURCE_STATE);
        if (state != AL10.AL_PLAYING) {
            if (!debugAudioPrinted) {
                System.out.println("音频源未在播放，尝试重新启动...");
            }
            AL10.alSourcePlay(musicSource);
        }
        
        debugAudioPrinted = true;
    }

    private void handleKeyInput(long win, int key, int scancode, int action, int mods) {
        if (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT) {
            switch (key) {
                case GLFW.GLFW_KEY_ESCAPE -> GLFW.glfwSetWindowShouldClose(window, true);
                case GLFW.GLFW_KEY_F -> {
                    currentFontSizeIndex = (currentFontSizeIndex + 1) % fontSizes.length;
                    glyphCache.clear();
                    System.out.println("字体大小: " + fontSizes[currentFontSizeIndex]);
                }
                case GLFW.GLFW_KEY_C -> {
                    currentColorIndex = (currentColorIndex + 1) % colors.length;
                    float[] c = colors[currentColorIndex];
                    System.out.printf("颜色: RGB(%.0f, %.0f, %.0f)%n", c[0]*255, c[1]*255, c[2]*255);
                }
                case GLFW.GLFW_KEY_T -> {
                    currentTextIndex = (currentTextIndex + 1) % demoTexts.length;
                    scrollY = 0;
                    System.out.println("切换文本: " + (currentTextIndex + 1) + "/" + demoTexts.length);
                }
                case GLFW.GLFW_KEY_H -> showHelp();
                case GLFW.GLFW_KEY_UP -> scrollY = Math.max(0, scrollY - 20);
                case GLFW.GLFW_KEY_DOWN -> scrollY = Math.min(maxScrollY, scrollY + 20);
                case GLFW.GLFW_KEY_HOME -> scrollY = 0;
                case GLFW.GLFW_KEY_END -> scrollY = maxScrollY;
                case GLFW.GLFW_KEY_M -> toggleMusic();
                case GLFW.GLFW_KEY_COMMA -> adjustVolume(-0.1f);
                case GLFW.GLFW_KEY_PERIOD -> adjustVolume(0.1f);
            }
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
        System.out.println("F - 切换字体大小 (24/36/48/64)");
        System.out.println("C - 切换文字颜色");
        System.out.println("T - 切换演示文本");
        System.out.println("M - 暂停/恢复音乐");
        System.out.println(", / . - 降低/提高音量");
        System.out.println("↑/↓ - 滚动文本");
        System.out.println("Home/End - 跳转到开头/结尾");
        System.out.println("鼠标滚轮 - 滚动文本");
        System.out.println("ESC - 退出程序");
        System.out.println("H - 显示此帮助");
        System.out.println("========================================\n");
    }

    private void initFont() {
        try {
            byte[] fontBytes = Files.readAllBytes(Paths.get(fontPath));
            fontBuffer = MemoryUtil.memAlloc(fontBytes.length);
            fontBuffer.put(fontBytes);
            fontBuffer.flip();
            System.out.println("字体加载成功: " + fontBytes.length + " bytes");
        } catch (IOException e) {
            throw new RuntimeException("无法加载字体文件: " + fontPath, e);
        }

        fontInfo = STBTTFontinfo.create();
        if (!STBTruetype.stbtt_InitFont(fontInfo, fontBuffer)) {
            throw new RuntimeException("无法初始化字体信息");
        }
    }

    private void loop() {
        while (!GLFW.glfwWindowShouldClose(window)) {
            GL11.glClearColor(0.05f, 0.05f, 0.1f, 1.0f);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GL11.glOrtho(0, width, height, 0, -1, 1);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glLoadIdentity();

            renderUI();
            
            updateAudio();

            GLFW.glfwSwapBuffers(window);
            GLFW.glfwPollEvents();
        }
    }

    private void renderUI() {
        float fontSize = fontSizes[currentFontSizeIndex];
        float[] color = colors[currentColorIndex];
        
        renderText("汉字渲染演示程序", 20, 30 - scrollY, fontSize * 1.2f, color);
        
        float lineY = 30 + fontSize * 1.8f - scrollY;
        renderText("按 H 键查看帮助", 20, lineY, fontSize * 0.6f, new float[]{0.7f, 0.7f, 0.7f});
        
        lineY += fontSize * 1.5f;
        renderText("─".repeat(60), 20, lineY, fontSize * 0.5f, new float[]{0.3f, 0.3f, 0.4f});
        
        lineY += fontSize;
        String musicStatus = musicPlaying ? "播放中" : "已暂停";
        String info = String.format("字体: %.0f | 文本: %d/%d | 音乐: %s | 音量: %.0f%%", 
            fontSize, currentTextIndex + 1, demoTexts.length, musicStatus, musicVolume * 100);
        renderText(info, 20, lineY, fontSize * 0.5f, new float[]{0.6f, 0.8f, 0.6f});
        
        lineY += fontSize * 1.5f;
        renderText("─".repeat(60), 20, lineY, fontSize * 0.5f, new float[]{0.3f, 0.3f, 0.4f});
        
        lineY += fontSize;
        String[] lines = demoTexts[currentTextIndex].split("\n");
        for (String line : lines) {
            renderText(line, 40, lineY, fontSize, color);
            lineY += fontSize * 1.5f;
        }
        
        maxScrollY = Math.max(0, lineY + scrollY - height + 50);
        
        renderScrollBar();
    }
    
    private void renderScrollBar() {
        if (maxScrollY > 0) {
            float barHeight = height * height / (maxScrollY + height);
            float barY = (scrollY / maxScrollY) * (height - barHeight);
            
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glColor4f(0.3f, 0.3f, 0.3f, 0.5f);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f(width - 12, barY);
            GL11.glVertex2f(width - 4, barY);
            GL11.glVertex2f(width - 4, barY + barHeight);
            GL11.glVertex2f(width - 12, barY + barHeight);
            GL11.glEnd();
        }
    }

    private void renderText(String text, float x, float y, float fontSize, float[] color) {
        if (y + fontSize < 0 || y > height) {
            return;
        }
        
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(color[0], color[1], color[2], 1.0f);

        float scale = STBTruetype.stbtt_ScaleForPixelHeight(fontInfo, fontSize);

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);
            IntBuffer pXOff = stack.mallocInt(1);
            IntBuffer pYOff = stack.mallocInt(1);
            IntBuffer pAdvanceWidth = stack.mallocInt(1);
            IntBuffer pLeftSideBearing = stack.mallocInt(1);
            IntBuffer pAscent = stack.mallocInt(1);
            IntBuffer pDescent = stack.mallocInt(1);
            IntBuffer pLineGap = stack.mallocInt(1);
            
            STBTruetype.stbtt_GetFontVMetrics(fontInfo, pAscent, pDescent, pLineGap);
            int ascent = pAscent.get(0);

            float currentX = x;
            float baseline = y + ascent * scale;

            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                
                int cacheKey = (int)c | ((int)fontSize << 16);
                GlyphCache cached = glyphCache.get(cacheKey);
                
                if (cached == null) {
                    cached = createGlyphCache(c, fontSize, scale, stack, 
                        pWidth, pHeight, pXOff, pYOff, pAdvanceWidth, pLeftSideBearing);
                    glyphCache.put(cacheKey, cached);
                }
                
                if (cached.textureId > 0 && cached.width > 0 && cached.height > 0) {
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, cached.textureId);

                    float charX = currentX + cached.xOff;
                    float charY = baseline + cached.yOff;

                    GL11.glBegin(GL11.GL_QUADS);
                    GL11.glTexCoord2f(0, 0); GL11.glVertex2f(charX, charY);
                    GL11.glTexCoord2f(1, 0); GL11.glVertex2f(charX + cached.width, charY);
                    GL11.glTexCoord2f(1, 1); GL11.glVertex2f(charX + cached.width, charY + cached.height);
                    GL11.glTexCoord2f(0, 1); GL11.glVertex2f(charX, charY + cached.height);
                    GL11.glEnd();
                    
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                }

                currentX += cached.advanceWidth * scale;
            }
        }

        GL11.glDisable(GL11.GL_BLEND);
    }
    
    private GlyphCache createGlyphCache(char c, float fontSize, float scale, MemoryStack stack,
            IntBuffer pWidth, IntBuffer pHeight, IntBuffer pXOff, IntBuffer pYOff,
            IntBuffer pAdvanceWidth, IntBuffer pLeftSideBearing) {
        
        int glyphIndex = STBTruetype.stbtt_FindGlyphIndex(fontInfo, c);
        
        if (glyphIndex == 0) {
            return new GlyphCache(0, 0, 0, 0, 0, (int)(fontSize * 0.5f / scale));
        }

        ByteBuffer bitmap = STBTruetype.stbtt_GetGlyphBitmap(
            fontInfo, scale, scale, glyphIndex,
            pWidth, pHeight, pXOff, pYOff
        );

        int bitmapWidth = pWidth.get(0);
        int bitmapHeight = pHeight.get(0);
        int xOff = pXOff.get(0);
        int yOff = pYOff.get(0);

        STBTruetype.stbtt_GetGlyphHMetrics(fontInfo, glyphIndex, pAdvanceWidth, pLeftSideBearing);
        int advanceWidth = pAdvanceWidth.get(0);

        if (bitmap == null || bitmapWidth <= 0 || bitmapHeight <= 0) {
            return new GlyphCache(0, 0, 0, 0, 0, advanceWidth);
        }

        ByteBuffer rgbaBitmap = BufferUtils.createByteBuffer(bitmapWidth * bitmapHeight * 4);
        for (int j = 0; j < bitmapWidth * bitmapHeight; j++) {
            byte alpha = bitmap.get(j);
            rgbaBitmap.put((byte) 255);
            rgbaBitmap.put((byte) 255);
            rgbaBitmap.put((byte) 255);
            rgbaBitmap.put(alpha);
        }
        rgbaBitmap.flip();

        int textureId = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, 
            bitmapWidth, bitmapHeight, 0,
            GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, rgbaBitmap);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

        return new GlyphCache(textureId, bitmapWidth, bitmapHeight, xOff, yOff, advanceWidth);
    }

    private void cleanup() {
        for (GlyphCache cached : glyphCache.values()) {
            if (cached.textureId > 0) {
                GL11.glDeleteTextures(cached.textureId);
            }
        }
        glyphCache.clear();
        
        MemoryUtil.memFree(fontBuffer);
        
        if (musicSource != 0) {
            AL10.alSourceStop(musicSource);
            AL10.alDeleteSources(musicSource);
        }
        if (musicBuffers != null) {
            AL10.alDeleteBuffers(musicBuffers);
        }
        if (vorbisDecoder != MemoryUtil.NULL) {
            STBVorbis.stb_vorbis_close(vorbisDecoder);
        }
        if (vorbisData != null) {
            MemoryUtil.memFree(vorbisData);
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
}
