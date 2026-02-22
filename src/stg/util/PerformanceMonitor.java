package stg.util;

/**
 * 性能监控工具类
 * 用于测量渲染时间、内存使用和FPS
 */
public class PerformanceMonitor {
    private static final long MEGABYTE = 1024L * 1024L;
    
    private long lastTime;
    private int frameCount;
    private double fps;
    private long totalRenderTime;
    private int renderCount;
    private long startTime;
    
    // 内存使用情况
    private long initialMemory;
    private long peakMemory;
    
    public PerformanceMonitor() {
        reset();
    }
    
    /**
     * 重置性能监控数据
     */
    public void reset() {
        lastTime = System.currentTimeMillis();
        frameCount = 0;
        fps = 0.0;
        totalRenderTime = 0;
        renderCount = 0;
        startTime = System.currentTimeMillis();
        
        // 记录初始内存使用
        Runtime runtime = Runtime.getRuntime();
        initialMemory = runtime.totalMemory() - runtime.freeMemory();
        peakMemory = initialMemory;
    }
    
    /**
     * 开始渲染计时
     * @return 开始时间（纳秒）
     */
    public long startRender() {
        return System.nanoTime();
    }
    
    /**
     * 结束渲染计时
     * @param startNanos 开始时间（纳秒）
     */
    public void endRender(long startNanos) {
        long renderTime = System.nanoTime() - startNanos;
        totalRenderTime += renderTime;
        renderCount++;
        frameCount++;
        
        // 更新FPS
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTime >= 1000) {
            fps = frameCount * 1000.0 / (currentTime - lastTime);
            lastTime = currentTime;
            frameCount = 0;
        }
        
        // 更新内存使用情况
        Runtime runtime = Runtime.getRuntime();
        long currentMemory = runtime.totalMemory() - runtime.freeMemory();
        if (currentMemory > peakMemory) {
            peakMemory = currentMemory;
        }
    }
    
    /**
     * 获取当前FPS
     * @return FPS值
     */
    public double getFPS() {
        return fps;
    }
    
    /**
     * 获取平均渲染时间（毫秒）
     * @return 平均渲染时间
     */
    public double getAverageRenderTime() {
        if (renderCount == 0) return 0;
        return totalRenderTime / (renderCount * 1_000_000.0);
    }
    
    /**
     * 获取当前内存使用（MB）
     * @return 当前内存使用
     */
    public double getCurrentMemory() {
        Runtime runtime = Runtime.getRuntime();
        return (runtime.totalMemory() - runtime.freeMemory()) / (double)MEGABYTE;
    }
    
    /**
     * 获取峰值内存使用（MB）
     * @return 峰值内存使用
     */
    public double getPeakMemory() {
        return peakMemory / (double)MEGABYTE;
    }
    
    /**
     * 获取初始内存使用（MB）
     * @return 初始内存使用
     */
    public double getInitialMemory() {
        return initialMemory / (double)MEGABYTE;
    }
    
    /**
     * 获取运行时间（秒）
     * @return 运行时间
     */
    public double getElapsedTime() {
        return (System.currentTimeMillis() - startTime) / 1000.0;
    }
    
    /**
     * 打印性能报告
     */
    public void printReport() {
        System.out.println("=== 性能报告 ===");
        System.out.printf("FPS: %.2f\n", getFPS());
        System.out.printf("平均渲染时间: %.4f ms\n", getAverageRenderTime());
        System.out.printf("当前内存: %.2f MB\n", getCurrentMemory());
        System.out.printf("峰值内存: %.2f MB\n", getPeakMemory());
        System.out.printf("初始内存: %.2f MB\n", getInitialMemory());
        System.out.printf("内存增长: %.2f MB\n", getPeakMemory() - getInitialMemory());
        System.out.printf("运行时间: %.2f s\n", getElapsedTime());
        System.out.println("==============");
    }
    
    /**
     * 打印简要性能信息
     */
    public void printBriefInfo() {
        System.out.printf("FPS: %.1f | 渲染时间: %.3f ms | 内存: %.1f MB\r", 
                getFPS(), getAverageRenderTime(), getCurrentMemory());
    }
}
