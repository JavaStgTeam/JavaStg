package test;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimplePerformanceTest {
    private static final int TEST_DURATION = 5000; // 测试持续时间（毫秒）
    private static final int MAX_OBJECTS = 10000; // 最大对象数
    private static final int TEST_WIDTH = 800;
    private static final int TEST_HEIGHT = 600;
    
    private static BufferedImage bufferImage;
    private static Graphics2D bufferGraphics;
    
    public static void main(String[] args) {
        System.out.println("=== JavaStg 简化性能测试 ===");
        
        // 初始化测试环境
        initTestEnvironment();
        
        // 测试Java2D渲染性能
        System.out.println("\n1. 测试Java2D渲染性能...");
        testJava2DRenderPerformance();
        
        // 测试内存使用
        System.out.println("\n2. 测试内存使用...");
        testMemoryUsage();
        
        // 清理
        cleanup();
        
        System.out.println("\n=== 性能测试完成 ===");
        System.out.println("\n注意: OpenGL测试需要修复编译错误后才能运行");
        System.out.println("\n=== 性能优化建议 ===");
        System.out.println("1. OpenGL渲染优化:");
        System.out.println("   - 实现批量渲染，减少绘制调用");
        System.out.println("   - 使用顶点缓冲对象(VBO)和顶点数组对象(VAO)");
        System.out.println("   - 实现纹理图集，减少纹理切换");
        System.out.println("   - 使用实例化渲染，减少相同对象的重复绘制");
        System.out.println("   - 优化着色器，减少计算复杂度");
        System.out.println("\n2. Java2D渲染优化:");
        System.out.println("   - 使用BufferedImage进行离屏渲染");
        System.out.println("   - 减少Graphics2D对象的创建和销毁");
        System.out.println("   - 优化颜色和字体的设置");
        System.out.println("   - 使用RenderingHints优化渲染质量和速度");
        System.out.println("\n3. 内存使用优化:");
        System.out.println("   - 实现对象池，减少垃圾回收");
        System.out.println("   - 优化纹理和资源的加载和释放");
        System.out.println("   - 使用弱引用和软引用管理资源");
        System.out.println("   - 减少临时对象的创建");
        System.out.println("\n4. 音频性能优化:");
        System.out.println("   - 使用OpenAL的缓冲区和源管理");
        System.out.println("   - 实现音频资源的预加载和缓存");
        System.out.println("   - 优化音频播放的状态管理");
        System.out.println("   - 减少音频API的调用次数");
    }
    
    private static void initTestEnvironment() {
        // 初始化Java2D测试环境
        bufferImage = new BufferedImage(TEST_WIDTH, TEST_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        bufferGraphics = bufferImage.createGraphics();
        // 启用抗锯齿
        bufferGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }
    
    private static void testJava2DRenderPerformance() {
        Random random = new Random();
        List<RenderObject> objects = generateTestObjects(MAX_OBJECTS, random);
        
        long startTime = System.currentTimeMillis();
        int frames = 0;
        
        while (System.currentTimeMillis() - startTime < TEST_DURATION) {
            // 清空缓冲区
            bufferGraphics.setColor(Color.BLACK);
            bufferGraphics.fillRect(0, 0, TEST_WIDTH, TEST_HEIGHT);
            
            // 渲染所有对象
            for (RenderObject obj : objects) {
                bufferGraphics.setColor(obj.color);
                switch (obj.type) {
                    case CIRCLE:
                        bufferGraphics.fillOval(
                            Math.round(obj.x - obj.size/2),
                            Math.round(obj.y - obj.size/2),
                            Math.round(obj.size),
                            Math.round(obj.size)
                        );
                        break;
                    case RECT:
                        bufferGraphics.fillRect(
                            Math.round(obj.x - obj.size/2),
                            Math.round(obj.y - obj.size/2),
                            Math.round(obj.size),
                            Math.round(obj.size)
                        );
                        break;
                    case LINE:
                        bufferGraphics.drawLine(
                            Math.round(obj.x),
                            Math.round(obj.y),
                            Math.round(obj.x + obj.size),
                            Math.round(obj.y + obj.size)
                        );
                        break;
                }
            }
            
            frames++;
        }
        
        long endTime = System.currentTimeMillis();
        double fps = frames / ((endTime - startTime) / 1000.0);
        
        System.out.println("Java2D渲染性能:");
        System.out.println("- 测试对象数: " + MAX_OBJECTS);
        System.out.println("- 测试时间: " + (endTime - startTime) + "ms");
        System.out.println("- 渲染帧数: " + frames);
        System.out.println("- FPS: " + String.format("%.2f", fps));
        System.out.println("- 每帧渲染对象数: " + (MAX_OBJECTS));
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
        if (bufferGraphics != null) {
            bufferGraphics.dispose();
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
