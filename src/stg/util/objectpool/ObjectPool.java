package stg.util.objectpool;

/**
 * 通用对象池接口
 * 定义对象池的基本操作方法
 * 
 * @param <T> 池化对象的类型
 * @date 2026-02-20
 * @author JavaSTG Team
 */
public interface ObjectPool<T> {
    
    /**
     * 从对象池中获取一个对象
     * 如果对象池为空，会创建新对象
     * 
     * @return 池化对象
     */
    T acquire();
    
    /**
     * 回收对象到对象池
     * 对象会被重置并准备下次使用
     * 
     * @param object 要回收的对象
     */
    void release(T object);
    
    /**
     * 初始化对象池
     * 预先创建指定数量的对象
     * 
     * @param initialCapacity 初始容量
     */
    void initialize(int initialCapacity);
    
    /**
     * 清理对象池
     * 释放所有池化对象
     */
    void clear();
    
    /**
     * 获取当前对象池中的对象数量
     * 
     * @return 当前对象数量
     */
    int size();
    
    /**
     * 获取对象池的最大容量
     * 如果返回-1，表示无限制
     * 
     * @return 最大容量
     */
    int getMaxCapacity();
    
    /**
     * 设置对象池的最大容量
     * 设置为-1表示无限制
     * 
     * @param maxCapacity 最大容量
     */
    void setMaxCapacity(int maxCapacity);
    
    /**
     * 检查对象池是否为空
     * 
     * @return 是否为空
     */
    boolean isEmpty();
}
