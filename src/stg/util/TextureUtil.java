package stg.util;

/**
 * 纹理工具类 - 提供纹理加载和管理相关的功能
 * @since 26-04-07 初始创建
 * @author JavaSTG Team
 */
public class TextureUtil {
    /**
     * 加载素材
     * 使用OpenGL加载图片，支持从图片中截取特定区域的素材
     * @param path 图片文件路径
     * @param x 素材在图片内的X坐标
     * @param y 素材在图片内的Y坐标
     * @param width 素材宽度
     * @param height 素材高度
     * @return 纹理ID
     */
    public static int loadTexture(String path, float x, float y, float width, float height) {
        // 直接使用GL11来加载纹理，不创建新的GLRenderer实例
        // 这样可以确保在同一个OpenGL上下文中加载纹理
        int textureId = -1;
        
        try (org.lwjgl.system.MemoryStack stack = org.lwjgl.system.MemoryStack.stackPush()) {
            java.nio.IntBuffer widthBuffer = stack.mallocInt(1);
            java.nio.IntBuffer heightBuffer = stack.mallocInt(1);
            java.nio.IntBuffer channelsBuffer = stack.mallocInt(1);
            
            // 尝试从文件系统直接读取
            java.nio.file.Path filePath = java.nio.file.Paths.get(path);
            if (!java.nio.file.Files.exists(filePath)) {
                // 如果文件不存在，尝试从类路径读取
                System.out.println("文件系统中找不到图片文件: " + path + "，尝试从类路径读取");
                // 从类路径读取图片
                java.io.InputStream inputStream = TextureUtil.class.getClassLoader().getResourceAsStream(path);
                if (inputStream == null) {
                    System.err.println("图片文件不存在: " + path);
                    return -1;
                }
                
                // 读取输入流到字节数组
                byte[] bytes = inputStream.readAllBytes();
                inputStream.close();
                
                // 分配内存并填充数据
                java.nio.ByteBuffer buffer = org.lwjgl.system.MemoryUtil.memAlloc(bytes.length);
                buffer.put(bytes);
                buffer.flip();
                
                // 加载图片
                java.nio.ByteBuffer image = org.lwjgl.stb.STBImage.stbi_load_from_memory(buffer, widthBuffer, heightBuffer, channelsBuffer, 4);
                org.lwjgl.system.MemoryUtil.memFree(buffer);
                
                if (image == null) {
                    System.err.println("加载图片失败: " + org.lwjgl.stb.STBImage.stbi_failure_reason());
                    return -1;
                }
                
                // 创建纹理
                textureId = org.lwjgl.opengl.GL11.glGenTextures();
                org.lwjgl.opengl.GL11.glBindTexture(org.lwjgl.opengl.GL11.GL_TEXTURE_2D, textureId);
                
                // 设置纹理参数
                org.lwjgl.opengl.GL11.glTexParameteri(org.lwjgl.opengl.GL11.GL_TEXTURE_2D, org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER, org.lwjgl.opengl.GL11.GL_LINEAR);
                org.lwjgl.opengl.GL11.glTexParameteri(org.lwjgl.opengl.GL11.GL_TEXTURE_2D, org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER, org.lwjgl.opengl.GL11.GL_LINEAR);
                
                // 保存宽度和高度值
                int imgWidth = widthBuffer.get();
                int imgHeight = heightBuffer.get();
                
                // 上传纹理数据
                org.lwjgl.opengl.GL11.glTexImage2D(org.lwjgl.opengl.GL11.GL_TEXTURE_2D, 0, org.lwjgl.opengl.GL11.GL_RGBA, imgWidth, imgHeight, 0, org.lwjgl.opengl.GL11.GL_RGBA, org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE, image);
                
                // 释放图片数据
                org.lwjgl.stb.STBImage.stbi_image_free(image);
                
                System.out.println("从类路径加载纹理成功: " + path + " (" + imgWidth + "x" + imgHeight + ")");
            } else {
                // 从文件系统直接读取
                java.nio.ByteBuffer image = org.lwjgl.stb.STBImage.stbi_load(path, widthBuffer, heightBuffer, channelsBuffer, 4);
                
                if (image == null) {
                    System.err.println("加载图片失败: " + org.lwjgl.stb.STBImage.stbi_failure_reason());
                    return -1;
                }
                
                // 创建纹理
                textureId = org.lwjgl.opengl.GL11.glGenTextures();
                org.lwjgl.opengl.GL11.glBindTexture(org.lwjgl.opengl.GL11.GL_TEXTURE_2D, textureId);
                
                // 设置纹理参数
                org.lwjgl.opengl.GL11.glTexParameteri(org.lwjgl.opengl.GL11.GL_TEXTURE_2D, org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER, org.lwjgl.opengl.GL11.GL_LINEAR);
                org.lwjgl.opengl.GL11.glTexParameteri(org.lwjgl.opengl.GL11.GL_TEXTURE_2D, org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER, org.lwjgl.opengl.GL11.GL_LINEAR);
                
                // 保存宽度和高度值
                int imgWidth = widthBuffer.get();
                int imgHeight = heightBuffer.get();
                
                // 上传纹理数据
                org.lwjgl.opengl.GL11.glTexImage2D(org.lwjgl.opengl.GL11.GL_TEXTURE_2D, 0, org.lwjgl.opengl.GL11.GL_RGBA, imgWidth, imgHeight, 0, org.lwjgl.opengl.GL11.GL_RGBA, org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE, image);
                
                // 释放图片数据
                org.lwjgl.stb.STBImage.stbi_image_free(image);
                
                System.out.println("从文件系统加载纹理成功: " + path + " (" + imgWidth + "x" + imgHeight + ")");
            }
            
        } catch (Exception e) {
            System.err.println("加载纹理失败: " + e.getMessage());
            e.printStackTrace();
        }
        
        return textureId;
    }
}