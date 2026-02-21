package stg.service.render;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;

import java.awt.Color;
import java.awt.Font;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

public class GLRenderer implements IRenderer {
    private long window;
    private boolean initialized;
    private ShaderProgram defaultShader;
    private Map<String, ShaderProgram> shaders;
    private Map<String, Integer> textures;
    private Map<String, VertexBuffer> vertexBuffers;
    private TextRenderer textRenderer;
    private BatchRenderer batchRenderer;
    
    public GLRenderer(long window) {
        this.window = window;
        this.shaders = new HashMap<>();
        this.vertexBuffers = new HashMap<>();
        this.textures = new HashMap<>();
    }
    
    @Override
    public void init() {
        if (initialized) return;
        
        // 初始化OpenGL
        GL.createCapabilities();
        
        // 设置视口
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            GLFW.glfwGetFramebufferSize(window, width, height);
            GL11.glViewport(0, 0, width.get(), height.get());
        }
        
        // 启用混合
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
        // 编译默认着色器
        String vertexShader = "#version 330 core\n" +
            "layout(location = 0) in vec3 aPos;\n" +
            "layout(location = 1) in vec4 aColor;\n" +
            "uniform mat4 uProjection;\n" +
            "uniform mat4 uModel;\n" +
            "out vec4 vColor;\n" +
            "void main() {\n" +
            "    gl_Position = uProjection * uModel * vec4(aPos, 1.0);\n" +
            "    vColor = aColor;\n" +
            "}";
        
        String fragmentShader = "#version 330 core\n" +
            "in vec4 vColor;\n" +
            "out vec4 FragColor;\n" +
            "void main() {\n" +
            "    FragColor = vColor;\n" +
            "}";
        
        defaultShader = new ShaderProgram();
        defaultShader.compile(vertexShader, fragmentShader);
        shaders.put("default", defaultShader);
        
        // 编译纹理着色器
        String textureVertexShader = "#version 330 core\n" +
            "layout(location = 0) in vec3 aPos;\n" +
            "layout(location = 1) in vec2 aTexCoord;\n" +
            "uniform mat4 uProjection;\n" +
            "uniform mat4 uModel;\n" +
            "out vec2 vTexCoord;\n" +
            "void main() {\n" +
            "    gl_Position = uProjection * uModel * vec4(aPos, 1.0);\n" +
            "    vTexCoord = aTexCoord;\n" +
            "}";
        
        String textureFragmentShader = "#version 330 core\n" +
            "in vec2 vTexCoord;\n" +
            "uniform sampler2D uTexture;\n" +
            "out vec4 FragColor;\n" +
            "void main() {\n" +
            "    FragColor = texture(uTexture, vTexCoord);\n" +
            "}";
        
        ShaderProgram textureShader = new ShaderProgram();
        textureShader.compile(textureVertexShader, textureFragmentShader);
        shaders.put("texture", textureShader);
        
        // 初始化投影矩阵
        defaultShader.use();
        float[] projection = new float[]{
            2.0f/800, 0.0f, 0.0f, 0.0f,
            0.0f, -2.0f/600, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f, 0.0f,
            -1.0f, 1.0f, 0.0f, 1.0f
        };
        defaultShader.setUniformMatrix4("uProjection", projection);
        
        // 初始化纹理着色器的投影矩阵
        textureShader.use();
        textureShader.setUniformMatrix4("uProjection", projection);
        
        // 初始化文本渲染器
        textRenderer = new TextRenderer(this);
        
        // 初始化批量渲染器
        batchRenderer = new BatchRenderer(defaultShader);
        
        initialized = true;
    }
    
    @Override
    public void beginFrame() {
        if (!initialized) init();
        
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        
        defaultShader.use();
        float[] model = new float[]{
            1.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f
        };
        defaultShader.setUniformMatrix4("uModel", model);
        
        // 开始批量渲染
        batchRenderer.begin();
    }
    
    @Override
    public void endFrame() {
        // 结束批量渲染
        batchRenderer.end();
        GLFW.glfwSwapBuffers(window);
    }
    
    @Override
    public void drawCircle(float x, float y, float radius, Color color) {
        batchRenderer.drawCircle(x, y, radius, color);
    }
    
    @Override
    public void drawRect(float x, float y, float width, float height, Color color) {
        batchRenderer.drawRect(x, y, width, height, color);
    }
    
    @Override
    public void drawLine(float x1, float y1, float x2, float y2, Color color) {
        batchRenderer.drawLine(x1, y1, x2, y2, color);
    }
    
    @Override
    public void drawText(String text, float x, float y, Font font, Color color) {
        // 使用TextRenderer渲染文本
        // 默认使用"default"字体，字体大小基于Font对象
        float fontSize = font.getSize2D();
        textRenderer.renderText(text, x, y, "default", fontSize, color);
    }
    
    @Override
    public void drawImage(Object image, float x, float y, float width, float height) {
        if (image instanceof String) {
            String textureName = (String) image;
            int textureId = textures.getOrDefault(textureName, -1);
            
            if (textureId != -1) {
                // 使用纹理着色器
                ShaderProgram textureShader = shaders.get("texture");
                if (textureShader != null) {
                    textureShader.use();
                    
                    // 设置模型矩阵
                    float[] model = new float[]{
                        1.0f, 0.0f, 0.0f, 0.0f,
                        0.0f, 1.0f, 0.0f, 0.0f,
                        0.0f, 0.0f, 1.0f, 0.0f,
                        x, y, 0.0f, 1.0f
                    };
                    textureShader.setUniformMatrix4("uModel", model);
                    
                    // 绑定纹理
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
                    textureShader.setUniform("uTexture", 0);
                    
                    // 创建带纹理坐标的顶点数据
                    float[] vertices = new float[]{
                        // 位置                // 纹理坐标
                        -width/2, -height/2, 0.0f,   0.0f, 1.0f,  // 左下角
                         width/2, -height/2, 0.0f,   1.0f, 1.0f,  // 右下角
                        -width/2,  height/2, 0.0f,   0.0f, 0.0f,  // 左上角
                         width/2,  height/2, 0.0f,   1.0f, 0.0f   // 右上角
                    };
                    
                    // 上传顶点数据
                    VertexBuffer buffer = new VertexBuffer();
                    buffer.uploadWithTexCoords(vertices);
                    buffer.draw(GL11.GL_TRIANGLE_STRIP);
                    
                    // 解绑纹理
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
                }
            }
        }
    }
    
    // 加载纹理方法
    public int loadTexture(String name, String path) {
        try {
            // 使用STB库加载图片
            java.io.InputStream inputStream = getClass().getResourceAsStream(path);
            if (inputStream == null) {
                throw new java.io.IOException("File not found: " + path);
            }
            
            byte[] data = inputStream.readAllBytes();
            inputStream.close();
            
            java.nio.ByteBuffer buffer = org.lwjgl.BufferUtils.createByteBuffer(data.length);
            buffer.put(data);
            buffer.flip();
            
            java.nio.IntBuffer width = org.lwjgl.BufferUtils.createIntBuffer(1);
            java.nio.IntBuffer height = org.lwjgl.BufferUtils.createIntBuffer(1);
            java.nio.IntBuffer channels = org.lwjgl.BufferUtils.createIntBuffer(1);
            
            java.nio.ByteBuffer image = org.lwjgl.stb.STBImage.stbi_load_from_memory(buffer, width, height, channels, 4);
            if (image == null) {
                throw new java.io.IOException("Failed to load image: " + org.lwjgl.stb.STBImage.stbi_failure_reason());
            }
            
            // 创建纹理
            int textureId = GL11.glGenTextures();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
            
            // 设置纹理参数
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            
            // 上传纹理数据
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width.get(), height.get(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);
            
            // 生成MIP映射
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            
            // 释放资源
            org.lwjgl.stb.STBImage.stbi_image_free(image);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            
            // 存储纹理
            textures.put(name, textureId);
            return textureId;
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return -1;
        }
    }
    
    @Override
    public void setColor(Color color) {
        // 已在各绘制方法中处理
    }
    
    @Override
    public void setFont(Font font) {
        // 已在drawText中处理
    }
    
    @Override
    public void cleanup() {
        if (!initialized) return;
        
        // 清理文本渲染器
        if (textRenderer != null) {
            textRenderer.cleanup();
        }
        
        // 清理批量渲染器
        if (batchRenderer != null) {
            batchRenderer.cleanup();
        }
        
        // 清理着色器
        for (ShaderProgram shader : shaders.values()) {
            shader.cleanup();
        }
        
        // 清理纹理
        for (Integer texture : textures.values()) {
            GL11.glDeleteTextures(texture);
        }
        
        initialized = false;
    }
    
    @Override
    public boolean isInitialized() {
        return initialized;
    }
    
    // 着色器管理方法
    public void addShader(String name, ShaderProgram shader) {
        shaders.put(name, shader);
    }
    
    public ShaderProgram getShader(String name) {
        return shaders.get(name);
    }
    
    // 顶点缓冲管理方法
    public void addVertexBuffer(String name, VertexBuffer buffer) {
        vertexBuffers.put(name, buffer);
    }
    
    public VertexBuffer getVertexBuffer(String name) {
        return vertexBuffers.get(name);
    }
    
    // 纹理管理方法
    public void addTexture(String name, int textureId) {
        textures.put(name, textureId);
    }
    
    public int getTexture(String name) {
        return textures.getOrDefault(name, -1);
    }
}
