package test;

import stg.service.render.GLRenderer;
import stg.service.render.Java2DRenderer;
import stg.service.render.IRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;

public class PerformanceTest {
    private static final int TEST_DURATION = 5000; // 测试持续时间（毫秒）
    private static final int MAX_OBJECTS = 10000; // 最大对象数
    private static final int TEST_WIDTH = 800;
    private static final int TEST_HEIGHT = 600;
    
    private static long window;
    private static GLRenderer glRenderer;
    private static Java2DRenderer java2DRenderer;
    private static BufferedImage bufferImage;
    private static Graphics2D bufferGraphics;
    
    public static void main(String[] args) {
        System.out.println("=== JavaStg 性能测试 ===");
        
        // 初始化测试环境
        initTestEnvironment();
        
        // 测试Java2D渲染性能
        System.out.println("\n1. 测试Java2D渲染性能...");
        testJava2DRenderPerformance();
        
        // 测试OpenGL渲染性能
        System.out.println("\n2. 测试OpenGL渲染性能...");
        testGLRenderPerformance();
        
        // 测试内存使用
        System.out.println("\n3. 测试内存使用...");
        testMemoryUsage();
        
        // 清理
        cleanup();
        
        System.out.println("\n=== 性能测试完成 ===");
    }
    
    private static void initTestEnvironment() {
        // 初始化Java2D测试环境
        bufferImage = new BufferedImage(TEST_WIDTH, TEST_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        bufferGraphics = bufferImage.createGraphics();
        java2DRenderer = new Java2DRenderer();
        java2DRenderer.init();
        java2DRenderer.setGraphics(bufferGraphics);
        
        // 初始化OpenGL测试环境
        if (!GLFW.glfwInit()) {
            System.err.println("无法初始化GLFW");
            return;
        }
        
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        window = GLFW.glfwCreateWindow(TEST_WIDTH, TEST_HEIGHT, "Performance Test", 0, 0);
        
        if (window == 0) {
            System.err.println("无法创建GLFW窗口");
            GLFW.glfwTerminate();
            return;
        }
        
        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();
        
        glRenderer = new GLRenderer(window);
        glRenderer.init();
    }
    
    private static void testJava2DRenderPerformance() {
        Random random = new Random();
        List<RenderObject> objects = generateTestObjects(MAX_OBJECTS, random);
        
        long startTime = System.currentTimeMillis();
        int frames = 0;
        
        while (System.currentTimeMillis() - startTime < TEST_DURATION) {
            java2DRenderer.beginFrame();
            
            // 渲染所有对象
            for (RenderObject obj : objects) {
                switch (obj.type) {
                    case CIRCLE:
                        java2DRenderer.drawCircle(obj.x, obj.y, obj.size, obj.color);
                        break;
                    case RECT:
                        java2DRenderer.drawRect(obj.x, obj.y, obj.size, obj.size, obj.color);
                        break;
                    case LINE:
                        java2DRenderer.drawLine(obj.x, obj.y, obj.x + obj.size, obj.y + obj.size, obj.color);
                        break;
                }
            }
            
            java2DRenderer.endFrame();
            frames++;
        }
        
        long endTime = System.currentTimeMillis();
        double fps = frames / ((endTime - startTime) / 1000.0);
        
        System.out.println("Java2D渲染性能:");
        System.out.println("- 测试对象数: " + MAX_OBJECTS);
        System.out.println("- 测试时间: " + (endTime - startTime) + "ms");
        System.out.println("- 渲染帧数: " + frames);
        System.out.println("- FPS: " + String.format("%.2f", fps));
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
            frames++;
        }
        
        long endTime = System.currentTimeMillis();
        double fps = frames / ((endTime - startTime) / 1000.0);
        
        System.out.println("OpenGL渲染性能:");
        System.out.println("- 测试对象数: " + MAX_OBJECTS);
        System.out.println("- 测试时间: " + (endTime - startTime) + "ms");
        System.out.println("- 渲染帧数: " + frames);
        System.out.println("- FPS: " + String.format("%.2f", fps));
    }
    
    private static void testMemoryUsage() {
        System.gc();
        Runtime runtime = Runtime.getRuntime();
        long initialMemory = runtime.totalMemory() - runtime.freeMemory();
        
        // 创建大量对象
        List<RenderObject> objects = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < MAX_OBJECTS; i++) {
            objects.add(new RenderObject(
                RenderType.values()[random.nextInt(RenderType.values().length)],
                random.nextFloat() * TEST_WIDTH,
                random.nextFloat() * TEST_HEIGHT,
                random.nextFloat() * 20 + 5,
                new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256), 255)
            ));
        }
        
        System.gc();
        long finalMemory = runtime.totalMemory() - runtime.freeMemory();
        
        System.out.println("内存使用测试:");
        System.out.println("- 初始内存: " + (initialMemory / 1024) + "KB");
        System.out.println("- 最终内存: " + (finalMemory / 1024) + "KB");
        System.out.println("- 内存增长: " + ((finalMemory - initialMemory) / 1024) + "KB");
        System.out.println("- 每对象内存: " + String.format("%.2f", (finalMemory - initialMemory) / (double)MAX_OBJECTS) + "B");
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
        
        if (bufferGraphics != null) {
            bufferGraphics.dispose();
        }
        
        if (java2DRenderer != null) {
            java2DRenderer.cleanup();
        }
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
