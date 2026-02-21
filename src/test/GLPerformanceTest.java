package test;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import stg.service.render.GLRenderer;
import stg.service.render.ShaderProgram;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GLPerformanceTest {
    private static final int TEST_DURATION = 5000; // 测试持续时间（毫秒）
    private static final int MAX_OBJECTS = 10000; // 最大对象数
    private static final int TEST_WIDTH = 800;
    private static final int TEST_HEIGHT = 600;
    
    private static long window;
    private static GLRenderer glRenderer;
    
    public static void main(String[] args) {
        System.out.println("=== OpenGL 性能测试 ===");
        
        // 初始化测试环境
        if (!initTestEnvironment()) {
            System.err.println("初始化OpenGL环境失败");
            return;
        }
        
        // 测试OpenGL渲染性能
        System.out.println("\n测试OpenGL渲染性能...");
        testGLRenderPerformance();
        
        // 清理
        cleanup();
        
        System.out.println("\n=== OpenGL 性能测试完成 ===");
    }
    
    private static boolean initTestEnvironment() {
        // 初始化GLFW
        if (!GLFW.glfwInit()) {
            System.err.println("无法初始化GLFW");
            return false;
        }
        
        // 创建窗口
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        window = GLFW.glfwCreateWindow(TEST_WIDTH, TEST_HEIGHT, "OpenGL Performance Test", 0, 0);
        
        if (window == 0) {
            System.err.println("无法创建GLFW窗口");
            GLFW.glfwTerminate();
            return false;
        }
        
        // 绑定上下文
        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();
        
        // 初始化渲染器
        glRenderer = new GLRenderer(window);
        glRenderer.init();
        
        return true;
    }
    
    private static void testGLRenderPerformance() {
        Random random = new Random();
        List<RenderObject> objects = generateTestObjects(MAX_OBJECTS, random);
        
        long startTime = System.currentTimeMillis();
        int frames = 0;
        
        while (System.currentTimeMillis() - startTime < TEST_DURATION) {
            glRenderer.beginFrame();
            
            // 渲染所有对象
            for (RenderObject obj : objects) {
                switch (obj.type) {
                    case CIRCLE:
                        glRenderer.drawCircle(obj.x, obj.y, obj.size, obj.color);
                        break;
                    case RECT:
                        glRenderer.drawRect(obj.x, obj.y, obj.size, obj.size, obj.color);
                        break;
                    case LINE:
                        glRenderer.drawLine(obj.x, obj.y, obj.x + obj.size, obj.y + obj.size, obj.color);
                        break;
                }
            }
            
            glRenderer.endFrame();
            
            // 处理事件
            GLFW.glfwPollEvents();
            
            frames++;
        }
        
        long endTime = System.currentTimeMillis();
        double fps = frames / ((endTime - startTime) / 1000.0);
        
        System.out.println("OpenGL渲染性能:");
        System.out.println("- 测试对象数: " + MAX_OBJECTS);
        System.out.println("- 测试时间: " + (endTime - startTime) + "ms");
        System.out.println("- 渲染帧数: " + frames);
        System.out.println("- FPS: " + String.format("%.2f", fps));
        System.out.println("- 每帧渲染对象数: " + (MAX_OBJECTS));
        
        // 与Java2D性能对比（基于之前的测试结果）
        double java2dFps = 33.71; // 从之前的测试结果获取
        double improvement = ((fps - java2dFps) / java2dFps) * 100;
        System.out.println("\n性能对比:");
        System.out.println("- Java2D FPS: " + String.format("%.2f", java2dFps));
        System.out.println("- OpenGL FPS: " + String.format("%.2f", fps));
        System.out.println("- 性能提升: " + String.format("%.2f%%", improvement));
        
        if (improvement >= 30) {
            System.out.println("✅ 达到性能目标: OpenGL渲染性能比Java2D提升了30%以上");
        } else {
            System.out.println("❌ 未达到性能目标: OpenGL渲染性能需要比Java2D提升30%以上");
            System.out.println("需要进一步优化渲染性能");
        }
    }
    
    private static List<RenderObject> generateTestObjects(int count, Random random) {
        List<RenderObject> objects = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            objects.add(new RenderObject(
                RenderType.values()[random.nextInt(RenderType.values().length)],
                random.nextFloat() * TEST_WIDTH,
                random.nextFloat() * TEST_HEIGHT,
                random.nextFloat() * 20 + 5,
                new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256), 255)
            ));
        }
        return objects;
    }
    
    private static void cleanup() {
        if (glRenderer != null) {
            glRenderer.cleanup();
        }
        
        if (window != 0) {
            GLFW.glfwDestroyWindow(window);
        }
        
        GLFW.glfwTerminate();
    }
    
    private enum RenderType {
        CIRCLE, RECT, LINE
    }
    
    private static class RenderObject {
        public RenderType type;
        public float x;
        public float y;
        public float size;
        public Color color;
        
        public RenderObject(RenderType type, float x, float y, float size, Color color) {
            this.type = type;
            this.x = x;
            this.y = y;
            this.size = size;
            this.color = color;
        }
    }
}
