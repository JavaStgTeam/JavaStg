package Main;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

public class LWJGLTest {
    public static void main(String[] args) {
        System.out.println("Testing LWJGL 3 initialization...");
        
        // 初始化GLFW
        if (!GLFW.glfwInit()) {
            System.err.println("Failed to initialize GLFW");
            return;
        }
        
        System.out.println("GLFW initialized successfully");
        
        // 创建一个窗口
        long window = GLFW.glfwCreateWindow(300, 300, "LWJGL Test", MemoryUtil.NULL, MemoryUtil.NULL);
        if (window == MemoryUtil.NULL) {
            System.err.println("Failed to create GLFW window");
            GLFW.glfwTerminate();
            return;
        }
        
        System.out.println("GLFW window created successfully");
        
        // 激活OpenGL上下文
        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();
        
        System.out.println("OpenGL context created successfully");
        System.out.println("OpenGL version: " + GL11.glGetString(GL11.GL_VERSION));
        
        // 清理
        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
        
        System.out.println("LWJGL 3 test completed successfully!");
    }
}