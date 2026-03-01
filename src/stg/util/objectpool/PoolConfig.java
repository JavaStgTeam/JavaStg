package stg.util.objectpool;

/**
 * 对象池配置类
 * 存储单个对象池的配置参数
 * 
 * @date 2026-03-01
 * @author JavaSTG Team
 */
public class PoolConfig {
    
    private int initialCapacity;
    private int maxCapacity;
    
    /**
     * 默认构造函数
     * 使用默认配置：初始容量0，最大容量100
     */
    public PoolConfig() {
        this.initialCapacity = 0;
        this.maxCapacity = 100;
    }
    
    /**
     * 构造函数
     * 
     * @param initialCapacity 初始容量
     * @param maxCapacity 最大容量
     */
    public PoolConfig(int initialCapacity, int maxCapacity) {
        this.initialCapacity = initialCapacity;
        this.maxCapacity = maxCapacity;
    }
    
    /**
     * 获取初始容量
     * 
     * @return 初始容量
     */
    public int getInitialCapacity() {
        return initialCapacity;
    }
    
    /**
     * 设置初始容量
     * 
     * @param initialCapacity 初始容量
     */
    public void setInitialCapacity(int initialCapacity) {
        this.initialCapacity = initialCapacity;
    }
    
    /**
     * 获取最大容量
     * 
     * @return 最大容量
     */
    public int getMaxCapacity() {
        return maxCapacity;
    }
    
    /**
     * 设置最大容量
     * 
     * @param maxCapacity 最大容量
     */
    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }
}