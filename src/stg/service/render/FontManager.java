package stg.service.render;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import stg.util.ResourceManager;

public class FontManager {
    private static FontManager instance;
    private Map<String, FontData> fonts;
    
    private FontManager() {
        fonts = new HashMap<>();
    }
    
    public static FontManager getInstance() {
        if (instance == null) {
            instance = new FontManager();
        }
        return instance;
    }
    
    public FontData loadFont(String name, String path, int fontSize) throws IOException {
        if (fonts.containsKey(name)) {
            return fonts.get(name);
        }
        
        try {
            // 读取字体文件
            ByteBuffer fontBuffer = ResourceManager.getInstance().loadResourceAsBuffer(path);
            if (fontBuffer == null) {
                throw new IOException("Failed to load font: " + path);
            }
            
            // 计算字体纹理大小
            int bitmapWidth = 512;
            int bitmapHeight = 512;
            
            // 创建字形表
            STBTTBakedChar.Buffer charData = STBTTBakedChar.malloc(96);
            ByteBuffer bitmap = BufferUtils.createByteBuffer(bitmapWidth * bitmapHeight);
            
            // 烘焙字体
            STBTruetype.stbtt_BakeFontBitmap(fontBuffer, fontSize * 10, bitmap, bitmapWidth, bitmapHeight, 32, charData);
            
            // 创建OpenGL纹理
            int textureId = GL11.glGenTextures();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_ALPHA, bitmapWidth, bitmapHeight, 0, GL11.GL_ALPHA, GL11.GL_UNSIGNED_BYTE, bitmap);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            
            // 创建字体数据
            FontData fontData = new FontData(name, path, fontSize, textureId, charData, bitmapWidth, bitmapHeight);
            fonts.put(name, fontData);
            
            return fontData;
        } catch (Exception e) {
            System.err.println("Failed to load font, creating fallback font: " + e.getMessage());
            // 创建一个简单的回退字体
            return createFallbackFont(name, fontSize);
        }
    }
    
    private FontData createFallbackFont(String name, int fontSize) {
        // 创建一个简单的回退字体纹理
        int bitmapWidth = 512;
        int bitmapHeight = 512;
        ByteBuffer bitmap = BufferUtils.createByteBuffer(bitmapWidth * bitmapHeight);
        
        // 填充一个简单的白色纹理
        for (int i = 0; i < bitmap.capacity(); i++) {
            bitmap.put((byte) 0);
        }
        bitmap.flip();
        
        // 创建OpenGL纹理
        int textureId = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_ALPHA, bitmapWidth, bitmapHeight, 0, GL11.GL_ALPHA, GL11.GL_UNSIGNED_BYTE, bitmap);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        
        // 创建一个空的字形表
        STBTTBakedChar.Buffer charData = STBTTBakedChar.malloc(96);
        
        // 创建字体数据
        FontData fontData = new FontData(name, "fallback", fontSize, textureId, charData, bitmapWidth, bitmapHeight);
        fonts.put(name, fontData);
        
        return fontData;
    }
    
    public FontData getFont(String name) {
        return fonts.get(name);
    }
    
    public void unloadFont(String name) {
        FontData fontData = fonts.remove(name);
        if (fontData != null) {
            GL11.glDeleteTextures(fontData.getTextureId());
            fontData.getCharData().free();
        }
    }
    
    public void cleanup() {
        for (FontData fontData : fonts.values()) {
            GL11.glDeleteTextures(fontData.getTextureId());
            fontData.getCharData().free();
        }
        fonts.clear();
    }
    
    public static class FontData {
        private String name;
        private String path;
        private int fontSize;
        private int textureId;
        private STBTTBakedChar.Buffer charData;
        private int bitmapWidth;
        private int bitmapHeight;
        
        public FontData(String name, String path, int fontSize, int textureId, STBTTBakedChar.Buffer charData, int bitmapWidth, int bitmapHeight) {
            this.name = name;
            this.path = path;
            this.fontSize = fontSize;
            this.textureId = textureId;
            this.charData = charData;
            this.bitmapWidth = bitmapWidth;
            this.bitmapHeight = bitmapHeight;
        }
        
        public String getName() { return name; }
        public String getPath() { return path; }
        public int getFontSize() { return fontSize; }
        public int getTextureId() { return textureId; }
        public STBTTBakedChar.Buffer getCharData() { return charData; }
        public int getBitmapWidth() { return bitmapWidth; }
        public int getBitmapHeight() { return bitmapHeight; }
    }
}