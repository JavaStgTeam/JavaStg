package stg.util.objectpool;

import java.util.HashMap;
import java.util.Map;

/**
 * 对象池管理器
 * 集中管理各种类型的对象池
 * 使用单例模式，提供全局访问点
 * 
 * @date 2026-02-20
 * @author JavaSTG Team
 */
public class ObjectPoolManager {
    
    // 单例实例
    private static final ObjectPoolManager INSTANCE = new ObjectPoolManager();
    
    // 存储不同类型的对象池
    private final Map<Class<?>, ObjectPool<?>> poolMap;
    
    /**
     * 私有构造函数
     */
    private ObjectPoolManager() {
        poolMap = new HashMap<>();
    }
    
    /**
     * 获取单例实例
     * 
     * @return 对象池管理器实例
     */
    public static ObjectPoolManager getInstance() {
        return INSTANCE;
    }
    
    /**
     * 注册对象池
     * 
     * @param <T> 对象类型
     * @param type 对象类型的Class
     * @param pool 对象池实例
     */
    public <T> void registerPool(Class<T> type, ObjectPool<T> pool) {
        poolMap.put(type, pool);
    }
    
    /**
     * 获取指定类型的对象池
     * 
     * @param <T> 对象类型
     * @param type 对象类型的Class
     * @return 对象池实例
     */
    @SuppressWarnings("unchecked")
    public <T> ObjectPool<T> getPool(Class<T> type) {
        return (ObjectPool<T>) poolMap.get(type);
    }
    
    /**
     * 检查是否存在指定类型的对象池
     * 
     * @param type 对象类型的Class
     * @return 是否存在
     */
    public boolean hasPool(Class<?> type) {
        return poolMap.containsKey(type);
    }
    
    /**
     * 从对象池中获取对象
     * 
     * @param <T> 对象类型
     * @param type 对象类型的Class
     * @return 池化对象
     */
    public <T> T acquire(Class<T> type) {
        ObjectPool<T> pool = getPool(type);
        if (pool == null) {
            throw new IllegalArgumentException("No object pool registered for type: " + type.getName());
        }
        return pool.acquire();
    }
    
    /**
     * 回收对象到对象池
     * 
     * @param <T> 对象类型
     * @param object 要回收的对象
     */
    public <T> void release(T object) {
        if (object == null) {
            return;
        }
        
        ObjectPool<T> pool = getPool((Class<T>) object.getClass());
        if (pool == null) {
            throw new IllegalArgumentException("No object pool registered for type: " + object.getClass().getName());
        }
        pool.release(object);
    }
    
    /**
     * 初始化所有对象池
     */
    public void initializeAllPools() {
        for (ObjectPool<?> pool : poolMap.values()) {
            // 每个对象池默认初始化10个对象
            pool.initialize(10);
        }
    }
    
    /**
     * 清理所有对象池
     */
    public void clearAllPools() {
        for (ObjectPool<?> pool : poolMap.values()) {
            pool.clear();
        }
    }
    
    /**
     * 获取对象池的数量
     * 
     * @return 对象池数量
     */
    public int getPoolCount() {
        return poolMap.size();
    }
    
    /**
     * 移除指定类型的对象池
     * 
     * @param type 对象类型的Class
     */
    public void removePool(Class<?> type) {
        poolMap.remove(type);
    }
    
    /**
     * 移除所有对象池
     */
    public void removeAllPools() {
        poolMap.clear();
    }
}
