package stg.util.objectpool;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 基于ConcurrentLinkedQueue的对象池实现
 * 线程安全，适用于多线程环境
 * 
 * @param <T> 池化对象的类型
 * @date 2026-02-20
 * @author JavaSTG Team
 */
public class ConcurrentLinkedObjectPool<T> implements ObjectPool<T> {
    
    private final ConcurrentLinkedQueue<T> pool;
    private final ObjectFactory<T> factory;
    private int maxCapacity;
    
    /**
     * 构造函数
     * 
     * @param factory 对象工厂，用于创建新对象
     */
    public ConcurrentLinkedObjectPool(ObjectFactory<T> factory) {
        this.pool = new ConcurrentLinkedQueue<>();
        this.factory = factory;
        this.maxCapacity = -1; // 默认无限制
    }
    
    /**
     * 构造函数
     * 
     * @param factory 对象工厂，用于创建新对象
     * @param initialCapacity 初始容量
     */
    public ConcurrentLinkedObjectPool(ObjectFactory<T> factory, int initialCapacity) {
        this(factory);
        initialize(initialCapacity);
    }
    
    /**
     * 构造函数
     * 
     * @param factory 对象工厂，用于创建新对象
     * @param initialCapacity 初始容量
     * @param maxCapacity 最大容量
     */
    public ConcurrentLinkedObjectPool(ObjectFactory<T> factory, int initialCapacity, int maxCapacity) {
        this(factory, initialCapacity);
        this.maxCapacity = maxCapacity;
    }
    
    @Override
    public T acquire() {
        // 尝试从池中获取对象
        T object = pool.poll();
        
        // 如果池为空，创建新对象
        if (object == null) {
            object = factory.create();
        }
        
        return object;
    }
    
    @Override
    public void release(T object) {
        // 检查对象是否可重置
        if (object instanceof Resettable) {
            ((Resettable) object).resetState();
        }
        
        // 检查是否达到最大容量
        if (maxCapacity == -1 || pool.size() < maxCapacity) {
            // 将对象放回池中
            pool.offer(object);
        }
    }
    
    @Override
    public void initialize(int initialCapacity) {
        for (int i = 0; i < initialCapacity; i++) {
            T object = factory.create();
            pool.offer(object);
        }
    }
    
    @Override
    public void clear() {
        pool.clear();
    }
    
    @Override
    public int size() {
        return pool.size();
    }
    
    @Override
    public int getMaxCapacity() {
        return maxCapacity;
    }
    
    @Override
    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        
        // 如果设置了新的最大容量且当前池大小超过了最大容量，清理多余的对象
        if (maxCapacity > 0) {
            while (pool.size() > maxCapacity) {
                pool.poll();
            }
        }
    }
    
    @Override
    public boolean isEmpty() {
        return pool.isEmpty();
    }
}
