package stg.util.objectpool;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * 通用对象池管理类
 * 提供灵活的对象池配置和管理功能
 * 支持自定义对象工厂和池配置
 * 
 * @date 2026-03-01
 * @author JavaSTG Team
 */
public class GenericObjectPoolManager {
    
    // 单例实例
    private static final GenericObjectPoolManager INSTANCE = new GenericObjectPoolManager();
    
    // 存储不同类型的对象池（线程安全）
    private final Map<Class<?>, ObjectPool<?>> poolMap;
    
    // 存储对象工厂（线程安全）
    private final Map<Class<?>, ObjectFactory<?>> factoryMap;
    
    // 存储对象池配置（线程安全）
    private final Map<Class<?>, PoolConfig> configMap;
    
    /**
     * 私有构造函数
     */
    private GenericObjectPoolManager() {
        poolMap = new ConcurrentHashMap<>();
        factoryMap = new ConcurrentHashMap<>();
        configMap = new ConcurrentHashMap<>();
    }
    
    /**
     * 获取单例实例
     * 
     * @return 通用对象池管理器实例
     */
    public static GenericObjectPoolManager getInstance() {
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
     * 注册对象工厂
     * 
     * @param <T> 对象类型
     * @param type 对象类型的Class
     * @param factory 对象工厂
     */
    public <T> void registerFactory(Class<T> type, ObjectFactory<T> factory) {
        factoryMap.put(type, factory);
    }
    
    /**
     * 注册对象工厂（使用Supplier）
     * 
     * @param <T> 对象类型
     * @param type 对象类型的Class
     * @param supplier 对象供应者
     */
    public <T> void registerFactoryWithSupplier(Class<T> type, Supplier<T> supplier) {
        ObjectFactory<T> factory = supplier::get;
        registerFactory(type, factory);
    }
    
    /**
     * 注册对象池配置
     * 
     * @param <T> 对象类型
     * @param type 对象类型的Class
     * @param config 对象池配置
     */
    public <T> void registerConfig(Class<T> type, PoolConfig config) {
        configMap.put(type, config);
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
            // 如果没有注册的对象池，尝试为该类型创建一个
            pool = getOrCreatePool(type);
        }
        return pool.acquire();
    }
    
    /**
     * 获取或创建对象池
     * 
     * @param <T> 对象类型
     * @param type 对象类型的Class
     * @return 对象池实例
     */
    @SuppressWarnings("unchecked")
    public <T> ObjectPool<T> getOrCreatePool(Class<T> type) {
        ObjectPool<T> pool = getPool(type);
        if (pool == null) {
            // 获取对象工厂
            ObjectFactory<T> factory = (ObjectFactory<T>) factoryMap.get(type);
            if (factory == null) {
                // 默认工厂：尝试使用无参构造函数
                factory = () -> {
                    try {
                        return type.getDeclaredConstructor().newInstance();
                    } catch (Exception e) {
                        // 如果无参构造函数失败，尝试使用float, float构造函数
                        try {
                            return type.getConstructor(float.class, float.class).newInstance(0.0f, 0.0f);
                        } catch (Exception ex) {
                            System.err.println("Failed to create object for " + type.getName() + ": " + ex.getMessage());
                            return null;
                        }
                    }
                };
            }
            
            // 获取对象池配置
            PoolConfig config = configMap.get(type);
            if (config == null) {
                // 默认配置
                config = new PoolConfig();
            }
            
            // 创建新的对象池
            pool = new ConcurrentLinkedObjectPool<>(
                factory,
                config.getInitialCapacity(),
                config.getMaxCapacity()
            );
            
            // 初始化对象池
            if (config.getInitialCapacity() > 0) {
                pool.initialize(config.getInitialCapacity());
            }
            
            // 注册对象池
            registerPool(type, pool);
        }
        return pool;
    }
    
    /**
     * 获取所有已注册的对象池类型
     * 
     * @return 已注册的对象池类型集合
     */
    public Set<Class<?>> getRegisteredTypes() {
        return poolMap.keySet();
    }
    
    /**
     * 回收对象到对象池
     * 
     * @param <T> 对象类型
     * @param object 要回收的对象
     */
    @SuppressWarnings("unchecked")
    public <T> void release(T object) {
        if (object == null) {
            return;
        }
        
        Class<?> clazz = object.getClass();
        ObjectPool<T> pool = getPool((Class<T>) clazz);
        
        if (pool == null) {
            // 如果没有注册的对象池，尝试为该类型创建一个
            pool = getOrCreatePool((Class<T>) clazz);
        }
        
        pool.release(object);
    }
    
    /**
     * 初始化所有对象池
     */
    public void initializeAllPools() {
        for (Map.Entry<Class<?>, ObjectPool<?>> entry : poolMap.entrySet()) {
            PoolConfig config = configMap.get(entry.getKey());
            if (config != null && config.getInitialCapacity() > 0) {
                entry.getValue().initialize(config.getInitialCapacity());
            }
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
        factoryMap.remove(type);
        configMap.remove(type);
    }
    
    /**
     * 移除所有对象池
     */
    public void removeAllPools() {
        poolMap.clear();
        factoryMap.clear();
        configMap.clear();
    }
    
    /**
     * 获取所有对象池中的对象总数
     * @return 对象总数
     */
    public int getTotalObjectCount() {
        int total = 0;
        for (ObjectPool<?> pool : poolMap.values()) {
            total += pool.size();
        }
        return total;
    }
    
    /**
     * 为指定类型配置对象池
     * 
     * @param <T> 对象类型
     * @param type 对象类型的Class
     * @param initialCapacity 初始容量
     * @param maxCapacity 最大容量
     */
    public <T> void configurePool(Class<T> type, int initialCapacity, int maxCapacity) {
        PoolConfig config = new PoolConfig(initialCapacity, maxCapacity);
        registerConfig(type, config);
        
        // 如果对象池已存在，更新配置
        ObjectPool<T> pool = getPool(type);
        if (pool != null) {
            pool.setMaxCapacity(maxCapacity);
        }
    }
    
    /**
     * 为指定类型配置对象池（使用自定义工厂）
     * 
     * @param <T> 对象类型
     * @param type 对象类型的Class
     * @param factory 对象工厂
     * @param initialCapacity 初始容量
     * @param maxCapacity 最大容量
     */
    public <T> void configurePool(Class<T> type, ObjectFactory<T> factory, int initialCapacity, int maxCapacity) {
        registerFactory(type, factory);
        configurePool(type, initialCapacity, maxCapacity);
    }
    
    /**
     * 为指定类型配置对象池（使用Supplier）
     * 
     * @param <T> 对象类型
     * @param type 对象类型的Class
     * @param supplier 对象供应者
     * @param initialCapacity 初始容量
     * @param maxCapacity 最大容量
     */
    public <T> void configurePool(Class<T> type, Supplier<T> supplier, int initialCapacity, int maxCapacity) {
        registerFactoryWithSupplier(type, supplier);
        configurePool(type, initialCapacity, maxCapacity);
    }
}