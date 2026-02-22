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
    private Map<String, String> pathToNameMap; // 路径到名称的映射，用于缓存复用
    private Map<String, TextureAtlas> atlases; // 纹理图集映射
    private Map<String, AtlasRegion> atlasRegions; // 图集中的区域映射
    private int currentBoundTextureId; // 当前绑定的纹理ID，用于优化绑定操作
    
    public TextureManager() {
        textures = new HashMap<>();
        textureInfos = new HashMap<>();
        pathToNameMap = new HashMap<>();
        atlases = new HashMap<>();
        atlasRegions = new HashMap<>();
        currentBoundTextureId = 0;
    }
    
    public int loadTexture(String name, String path) {
        // 检查是否已经加载过同名纹理
        if (textures.containsKey(name)) {
            return textures.get(name);
        }
        
        // 检查是否已经加载过同路径的纹理
        if (pathToNameMap.containsKey(path)) {
            String existingName = pathToNameMap.get(path);
            int existingTextureId = textures.get(existingName);
            textures.put(name, existingTextureId);
            textureInfos.put(name, textureInfos.get(existingName));
            return existingTextureId;
        }
        
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
            
            // STBImage自动支持PNG、JPG、BMP等格式
            ByteBuffer image = STBImage.stbi_load_from_memory(buffer, width, height, channels, 4);
            if (image == null) {
                throw new IOException("Failed to load image: " + STBImage.stbi_failure_reason());
            }
            
            int textureId = createTexture(image, width.get(), height.get());
            textures.put(name, textureId);
            textureInfos.put(name, new TextureInfo(width.get(), height.get()));
            pathToNameMap.put(path, name);
            
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
        
        // 绑定纹理
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
        
        // 解绑纹理
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
        if (textureId != null && textureId != currentBoundTextureId) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
            currentBoundTextureId = textureId;
        }
    }
    
    public void bindTexture(int textureId) {
        if (textureId != currentBoundTextureId) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
            currentBoundTextureId = textureId;
        }
    }
    
    public void unbindTexture() {
        if (currentBoundTextureId != 0) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            currentBoundTextureId = 0;
        }
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
            // 检查是否还有其他名称引用此纹理
            boolean isReferenced = false;
            for (Integer id : textures.values()) {
                if (id.equals(textureId)) {
                    isReferenced = true;
                    break;
                }
            }
            
            if (!isReferenced) {
                GL11.glDeleteTextures(textureId);
                // 从pathToNameMap中移除
                for (Map.Entry<String, String> entry : pathToNameMap.entrySet()) {
                    if (entry.getValue().equals(name)) {
                        pathToNameMap.remove(entry.getKey());
                        break;
                    }
                }
            }
            
            textureInfos.remove(name);
            
            // 如果删除的是当前绑定的纹理，解绑
            if (currentBoundTextureId == textureId) {
                currentBoundTextureId = 0;
            }
        }
    }
    

    
    // 预加载纹理方法
    public void preloadTexture(String name, String path) {
        loadTexture(name, path);
    }
    
    // 批量加载纹理方法
    public void preloadTextures(Map<String, String> textureMap) {
        for (Map.Entry<String, String> entry : textureMap.entrySet()) {
            loadTexture(entry.getKey(), entry.getValue());
        }
    }
    
    public static class TextureInfo {
        public final int width;
        public final int height;
        
        public TextureInfo(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }
    
    public static class AtlasRegion {
        public final String atlasName;
        public final int textureId;
        public final float u1, v1, u2, v2; // 纹理坐标
        public final int width, height;
        
        public AtlasRegion(String atlasName, int textureId, float u1, float v1, float u2, float v2, int width, int height) {
            this.atlasName = atlasName;
            this.textureId = textureId;
            this.u1 = u1;
            this.v1 = v1;
            this.u2 = u2;
            this.v2 = v2;
            this.width = width;
            this.height = height;
        }
    }
    
    public static class TextureAtlas {
        public final String name;
        public final int textureId;
        public final int width, height;
        public final Map<String, AtlasRegion> regions;
        
        public TextureAtlas(String name, int textureId, int width, int height) {
            this.name = name;
            this.textureId = textureId;
            this.width = width;
            this.height = height;
            this.regions = new HashMap<>();
        }
    }
    
    // 创建纹理图集
    public TextureAtlas createAtlas(String name, int width, int height) {
        int textureId = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        
        // 设置纹理参数
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        
        // 创建空纹理
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
        
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        
        TextureAtlas atlas = new TextureAtlas(name, textureId, width, height);
        atlases.put(name, atlas);
        return atlas;
    }
    
    // 向图集中添加区域
    public AtlasRegion addRegionToAtlas(String atlasName, String regionName, int x, int y, int width, int height, ByteBuffer imageData) {
        TextureAtlas atlas = atlases.get(atlasName);
        if (atlas == null) {
            return null;
        }
        
        // 绑定图集纹理
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, atlas.textureId);
        
        // 上传区域数据
        GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, x, y, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imageData);
        
        // 生成MIP映射
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        
        // 计算纹理坐标
        float u1 = (float)x / atlas.width;
        float v1 = (float)y / atlas.height;
        float u2 = (float)(x + width) / atlas.width;
        float v2 = (float)(y + height) / atlas.height;
        
        AtlasRegion region = new AtlasRegion(atlasName, atlas.textureId, u1, v1, u2, v2, width, height);
        atlas.regions.put(regionName, region);
        atlasRegions.put(regionName, region);
        
        return region;
    }
    
    // 从图集中获取区域
    public AtlasRegion getAtlasRegion(String regionName) {
        return atlasRegions.get(regionName);
    }
    
    // 从图集中获取纹理
    public int getAtlasTexture(String atlasName) {
        TextureAtlas atlas = atlases.get(atlasName);
        return atlas != null ? atlas.textureId : -1;
    }
    
    // 检查是否为图集区域
    public boolean isAtlasRegion(String name) {
        return atlasRegions.containsKey(name);
    }
    
    // 绑定图集区域纹理
    public void bindAtlasRegion(String regionName) {
        AtlasRegion region = atlasRegions.get(regionName);
        if (region != null) {
            bindTexture(region.textureId);
        }
    }
    
    // 清理方法
    public void cleanup() {
        // 清理纹理
        for (Integer textureId : textures.values()) {
            GL11.glDeleteTextures(textureId);
        }
        
        // 清理图集
        for (TextureAtlas atlas : atlases.values()) {
            GL11.glDeleteTextures(atlas.textureId);
        }
        
        textures.clear();
        textureInfos.clear();
        pathToNameMap.clear();
        atlases.clear();
        atlasRegions.clear();
        currentBoundTextureId = 0;
    }
}

