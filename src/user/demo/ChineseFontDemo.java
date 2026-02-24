package user.demo;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import stg.util.ALAudioManager;

public class ChineseFontDemo {
    private long window;
    private int width = 1000;
    private int height = 700;
    private STBTTFontinfo fontInfo;
    private ByteBuffer fontBuffer;
    private String fontPath = "e:\\Myproject\\Game\\jstg_Team\\JavaStg\\resources\\fonts\\OPPO Sans 4.0.ttf";
    private String musicPath = "audio/music/luastg 0.08.540 - 1.27.800.ogg";
    
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
    
    private ALAudioManager audioManager;
    private float musicVolume = 0.5f;
    private boolean musicPlaying = false;
    private static final String MUSIC_NAME = "bgm";
    
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
            System.out.println("初始化音频系统...");
            audioManager = ALAudioManager.getInstance();
            audioManager.init();
            
            if (!audioManager.isInitialized()) {
                System.err.println("音频系统初始化失败");
                return;
            }
            
            System.out.println("加载音乐: " + musicPath);
            audioManager.loadMusic(MUSIC_NAME, musicPath);
            audioManager.setMusicVolume(musicVolume);
            audioManager.playMusic(MUSIC_NAME, true);
            musicPlaying = true;
            System.out.println("音乐播放已启动");
            
        } catch (Exception e) {
            System.err.println("初始化音频失败: " + e.getMessage());
            e.printStackTrace();
        }
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
        if (audioManager == null || !audioManager.isInitialized()) return;
        
        if (musicPlaying) {
            audioManager.pauseMusic(MUSIC_NAME);
            musicPlaying = false;
            System.out.println("音乐已暂停");
        } else {
            audioManager.resumeMusic(MUSIC_NAME);
            musicPlaying = true;
            System.out.println("音乐已恢复");
        }
    }
    
    private void adjustVolume(float delta) {
        musicVolume = Math.max(0.0f, Math.min(1.0f, musicVolume + delta));
        if (audioManager != null && audioManager.isInitialized()) {
            audioManager.setMusicVolume(musicVolume);
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
        
        if (audioManager != null && audioManager.isInitialized()) {
            audioManager.stopMusic(MUSIC_NAME);
            audioManager.unloadMusic(MUSIC_NAME);
            audioManager.cleanup();
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
