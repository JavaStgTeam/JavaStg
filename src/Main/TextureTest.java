package Main;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import stg.service.render.GLRenderer;

import java.awt.Color;
import java.awt.Font;

public class TextureTest {
    private long window;
    private GLRenderer renderer;
    
    public void run() {
        init();
        loop();
        cleanup();
    }
    
    private void init() {
        // 初始化GLFW
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        
        // 创建窗口
        window = GLFW.glfwCreateWindow(800, 600, "Texture Test", 0, 0);
        if (window == 0) {
            GLFW.glfwTerminate();
            throw new IllegalStateException("Unable to create GLFW window");
        }
        
        // 绑定OpenGL上下文
        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();
        
        // 初始化渲染器
        renderer = new GLRenderer(window);
        renderer.init();
        
        // 加载测试纹理
        // 注意：需要在resources目录下添加测试图片
        renderer.loadTexture("test", "/test.png");
    }
    
    private void loop() {
        while (!GLFW.glfwWindowShouldClose(window)) {
            renderer.beginFrame();
            
            // 绘制测试内容
            // 1. 绘制背景
            renderer.drawRect(400, 300, 800, 600, new Color(30, 30, 30, 255));
            
            // 2. 绘制纹理
            renderer.drawImage("test", 400, 300, 200, 200);
            
            // 3. 绘制文本
            renderer.drawText("Texture Mapping Test", 400, 100, new Font("Arial", Font.PLAIN, 24), Color.WHITE);
            renderer.drawText("If you see an image above, texture mapping works!", 400, 500, new Font("Arial", Font.PLAIN, 16), Color.WHITE);
            
            renderer.endFrame();
            GLFW.glfwPollEvents();
        }
    }
    
    private void cleanup() {
        renderer.cleanup();
        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
    }
    
    public static void main(String[] args) {
        new TextureTest().run();
    }
}