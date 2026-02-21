package stg.service.render;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

public class TextureManager {
    private Map<String, Integer> textures;
    private Map<String, TextureInfo> textureInfos;
    
    public TextureManager() {
        textures = new HashMap<>();
        textureInfos = new HashMap<>();
    }
    
    public int loadTexture(String name, String path) {
        try {
            // 读取文件
            InputStream inputStream = getClass().getResourceAsStream(path);
            if (inputStream == null) {
                throw new IOException("File not found: " + path);
            }
            
            byte[] data = inputStream.readAllBytes();
            inputStream.close();
            
            // 使用STB库加载图片
            ByteBuffer buffer = BufferUtils.createByteBuffer(data.length);
            buffer.put(data);
            buffer.flip();
            
            IntBuffer width = BufferUtils.createIntBuffer(1);
            IntBuffer height = BufferUtils.createIntBuffer(1);
            IntBuffer channels = BufferUtils.createIntBuffer(1);
            
            ByteBuffer image = STBImage.stbi_load_from_memory(buffer, width, height, channels, 4);
            if (image == null) {
                throw new IOException("Failed to load image: " + STBImage.stbi_failure_reason());
            }
            
            int textureId = createTexture(image, width.get(), height.get());
            textures.put(name, textureId);
            textureInfos.put(name, new TextureInfo(width.get(), height.get()));
            
            // 释放STB资源
            STBImage.stbi_image_free(image);
            
            return textureId;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }
    
    private int createTexture(ByteBuffer image, int width, int height) {
        int textureId = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        
        // 设置纹理参数
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        
        // 上传纹理数据
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);
        
        // 生成MIP映射
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        return textureId;
    }
    
    private int createWhiteTexture() {
        int textureId = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        
        // 设置纹理参数
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        
        // 创建白色纹理
        ByteBuffer buffer = BufferUtils.createByteBuffer(4);
        buffer.put((byte)255).put((byte)255).put((byte)255).put((byte)255);
        buffer.flip();
        
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, 1, 1, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        return textureId;
    }
    
    public void bindTexture(String name) {
        Integer textureId = textures.get(name);
        if (textureId != null) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        }
    }
    
    public void unbindTexture() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }
    
    public int getTexture(String name) {
        return textures.getOrDefault(name, -1);
    }
    
    public TextureInfo getTextureInfo(String name) {
        return textureInfos.get(name);
    }
    
    public void removeTexture(String name) {
        Integer textureId = textures.remove(name);
        if (textureId != null) {
            GL11.glDeleteTextures(textureId);
            textureInfos.remove(name);
        }
    }
    
    public void cleanup() {
        for (Integer textureId : textures.values()) {
            GL11.glDeleteTextures(textureId);
        }
        textures.clear();
        textureInfos.clear();
    }
    
    public static class TextureInfo {
        public final int width;
        public final int height;
        
        public TextureInfo(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }
}
