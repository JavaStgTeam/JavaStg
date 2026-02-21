package Main;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import stg.service.render.GLRenderer;
import stg.service.render.FontManager;
import stg.util.ResourceManager;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;

public class TextRenderTest {
    private long window;
    private GLRenderer renderer;
    private FontManager fontManager;
    
    public void run() {
        init();
        loop();
        cleanup();
    }
    
    private void init() {
        // 设置错误回调
        GLFWErrorCallback.createPrint(System.err).set();
        
        // 初始化GLFW
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        
        // 配置窗口
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
        
        // 创建窗口
        window = GLFW.glfwCreateWindow(800, 600, "Text Render Test", 0, 0);
        if (window == 0) {
            throw new IllegalStateException("Failed to create the GLFW window");
        }
        
        // 绑定OpenGL上下文
        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();
        
        // 显示窗口
        GLFW.glfwShowWindow(window);
        
        // 初始化渲染器
        renderer = new GLRenderer(window);
        renderer.init();
        
        // 初始化字体管理器
        fontManager = FontManager.getInstance();
        
        // 加载字体
        try {
            // 注意：需要确保在resources/fonts/目录下有字体文件
            fontManager.loadFont("default", "fonts/arial.ttf", 24);
            System.out.println("Font loaded successfully!");
        } catch (IOException e) {
            System.err.println("Failed to load font: " + e.getMessage());
            // 如果没有字体文件，使用内置字体作为备选
            System.out.println("Using fallback font rendering");
        }
    }
    
    private void loop() {
        while (!GLFW.glfwWindowShouldClose(window)) {
            // 开始帧
            renderer.beginFrame();
            
            // 渲染测试文本
            renderer.drawText("Hello, OpenGL Text!", 100, 300, new Font("Arial", Font.PLAIN, 24), Color.WHITE);
            renderer.drawText("Testing different sizes: 12px", 100, 250, new Font("Arial", Font.PLAIN, 12), Color.RED);
            renderer.drawText("Testing different sizes: 36px", 100, 350, new Font("Arial", Font.PLAIN, 36), Color.GREEN);
            renderer.drawText("Testing different styles: Bold", 100, 400, new Font("Arial", Font.BOLD, 24), Color.BLUE);
            renderer.drawText("Testing different styles: Italic", 100, 450, new Font("Arial", Font.ITALIC, 24), Color.YELLOW);
            
            // 结束帧
            renderer.endFrame();
            
            // 处理事件
            GLFW.glfwPollEvents();
        }
    }
    
    private void cleanup() {
        // 清理渲染器
        renderer.cleanup();
        
        // 清理字体管理器
        fontManager.cleanup();
        
        // 销毁窗口
        GLFW.glfwDestroyWindow(window);
        
        // 终止GLFW
        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
    }
    
    public static void main(String[] args) {
        new TextRenderTest().run();
    }
}