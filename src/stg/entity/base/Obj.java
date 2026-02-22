package stg.entity.base;

import java.awt.*;
import stg.renderer.IRenderer;
import stg.util.CoordinateSystem;
import stg.util.objectpool.ConcurrentLinkedObjectPool;
import stg.util.objectpool.ObjectFactory;
import stg.util.objectpool.ObjectPoolManager;

/**
 * 游戏物体基类 - 所有游戏中的物体都继承自此类
 * @since 1.0
 * @author JavaSTG Team
 * @date 2026-01-19 初始创建
 * @date 2026-01-20 将类移动到stg.game.obj
 * @date 2026-01-29 功能优化
 * @date 2026-02-16 重构坐标系统，使用固定360*480游戏逻辑尺寸
 * @date 2026-02-18 迁移到stg.entity.base包
 * @date 2026-02-21 添加对象池支持
 */
public abstract class Obj {
    protected float x; // X坐标
    protected float y; // Y坐标
    protected float vx; // X方向速度
    protected float vy; // Y方向速度
    protected float size; // 物体大小
    protected Color color; // 物体颜色
    protected float hitboxRadius; // 碰撞判定半径
    protected boolean active; // 激活状态
    protected int frame; // 帧计数器
    
    // 坐标系统（用于动态坐标转换）
    private static CoordinateSystem sharedCoordinateSystem;
    
    // 对象池初始化标志
    private static volatile boolean objectPoolsInitialized = false;
    // 对象池初始化锁
    private static final Object poolInitLock = new Object();

    /**
     * 设置共享的坐标系统
     * @param coordinateSystem 坐标系统实例
     */
    public static void setSharedCoordinateSystem(CoordinateSystem coordinateSystem) {
        sharedCoordinateSystem = coordinateSystem;
    }

    /**
     * 获取共享的坐标系统
     * @return 坐标系统实例
     */
    public static CoordinateSystem getSharedCoordinateSystem() {
        return sharedCoordinateSystem;
    }

    /**
     * 检查坐标系统是否已初始化
     * @return 是否已初始化
     */
    public static boolean isCoordinateSystemInitialized() {
        return sharedCoordinateSystem != null;
    }

    /**
     * 要求坐标系统必须已初始化
     * @throws IllegalStateException 如果坐标系统未初始化
     */
    public static void requireCoordinateSystem() {
        if (sharedCoordinateSystem == null) {
            throw new IllegalStateException(
                "CoordinateSystem not initialized. " +
                "Please call Obj.setSharedCoordinateSystem() before using game objects."
            );
        }
    }

    /**
     * 将游戏坐标转换为屏幕坐标
     * @param worldX 游戏世界X坐标
     * @param worldY 游戏世界Y坐标
     * @return 屏幕坐标数组 [x, y]
     * @throws IllegalStateException 如果坐标系统未初始化
     */
    protected float[] toScreenCoords(float worldX, float worldY) {
        requireCoordinateSystem();
        return sharedCoordinateSystem.toScreenCoords(worldX, worldY);
    }

    /**
     * 构造函数
     * @param x 初始X坐标
     * @param y 初始Y坐标
     * @param vx X方向速度
     * @param vy Y方向速度
     * @param size 物体大小
     * @param color 物体颜色
     */
    public Obj(float x, float y, float vx, float vy, float size, Color color) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.size = size;
        this.color = color;
        this.hitboxRadius = size;
        this.active = true;
        this.frame = 0;
        initBehavior();
    }

    /**
     * 初始化行为参数
     * 在构造函数中调用，用于初始化行为参数
     */
    protected void initBehavior() {
        // 子类可以重写此方法初始化行为参数
    }

    /**
     * 实现每帧的自定义更新逻辑
     */
    protected void onUpdate() {
        // 子类可以重写此方法实现每帧的自定义更新逻辑
    }

    /**
     * 实现自定义移动逻辑
     */
    protected void onMove() {
        // 子类可以重写此方法实现自定义移动逻辑
    }

    /**
     * 更新物体状态
     */
    public void update() {
        frame++;

        // 调用自定义更新逻辑
        onUpdate();

        // 调用自定义移动逻辑
        onMove();

        // 更新位置
        x += vx;
        y += vy;
    }

    /**
     * 渲染物体（Java2D版本）
     * @param g 图形上下文
     * @throws IllegalStateException 如果坐标系统未初始化
     */
    public void render(Graphics2D g) {
        if (!active) return;

        requireCoordinateSystem();
        float[] screenCoords = toScreenCoords(x, y);
        float screenX = screenCoords[0];
        float screenY = screenCoords[1];

        g.setColor(color);
        g.fillOval((int)(screenX - size/2), (int)(screenY - size/2), (int)size, (int)size);
    }

    /**
     * 渲染物体（IRenderer版本，支持OpenGL）
     * @param renderer 渲染器
     * @throws IllegalStateException 如果坐标系统未初始化
     */
    public void render(IRenderer renderer) {
        if (!active) return;

        requireCoordinateSystem();
        // 使用渲染器绘制圆形，保持与Java2D一致的效果
        renderer.drawCircle(x, y, size/2, color);
    }

    /**
     * 检查物体是否超出边界
     * @return 是否超出边界
     * @throws IllegalStateException 如果坐标系统未初始化
     */
    public boolean isOutOfBounds() {
        requireCoordinateSystem();
        CoordinateSystem cs = sharedCoordinateSystem;
        float leftBound = cs.getLeftBound() - size;
        float rightBound = cs.getRightBound() + size;
        float topBound = cs.getTopBound() - size;
        float bottomBound = cs.getBottomBound() + size;
        return x < leftBound || x > rightBound || y < topBound || y > bottomBound;
    }

    /**
     * 移动到指定坐标
     * @param x 目标X坐标
     * @param y 目标Y坐标
     */
    public void moveTo(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * 检查物体是否激活
     * @return 是否激活
     */
    public boolean isActive() {
        return active;
    }

    /**
     * 设置物体激活状态
     * @param active 是否激活
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * 获取X坐标
     * @return X坐标
     */
    public float getX() {
        return x;
    }

    /**
     * 获取Y坐标
     * @return Y坐标
     */
    public float getY() {
        return y;
    }

    /**
     * 获取物体大小
     * @return 物体大小
     */
    public float getSize() {
        return size;
    }

    /**
     * 获取物体颜色
     * @return 物体颜色
     */
    public Color getColor() {
        return color;
    }

    /**
     * 重置物体状态
     */
    public void reset() {
        this.active = true;
        this.frame = 0;
    }

    /**
     * 设置X坐标
     * @param x X坐标
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * 设置Y坐标
     * @param y Y坐标
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * 设置X方向速度
     * @param vx X方向速度
     */
    public void setVx(float vx) {
        this.vx = vx;
    }

    /**
     * 设置Y方向速度
     * @param vy Y方向速度
     */
    public void setVy(float vy) {
        this.vy = vy;
    }

    /**
     * 获取X方向速度
     * @return X方向速度
     */
    public float getVx() {
        return vx;
    }

    /**
     * 获取Y方向速度
     * @return Y方向速度
     */
    public float getVy() {
        return vy;
    }

    /**
     * 设置碰撞判定半径
     * @param hitboxRadius 碰撞判定半径
     */
    public void setHitboxRadius(float hitboxRadius) {
        this.hitboxRadius = hitboxRadius;
    }

    /**
     * 获取碰撞判定半径
     * @return 碰撞判定半径
     */
    public float getHitboxRadius() {
        return hitboxRadius;
    }
    
    // ==================== 对象池操作方法 ====================
    
    /**
     * 从对象池创建实例
     * @param <T> 对象类型
     * @param clazz 对象类型的 Class
     * @param args 构造函数参数
     * @return 对象实例
     */
    @SuppressWarnings("unchecked")
    public static <T extends Obj> T create(Class<T> clazz, Object... args) {
        try {
            // 确保对象池已初始化
            if (!objectPoolsInitialized) {
                initializeObjectPools();
            }
            
            // 尝试从对象池获取或创建对象
            try {
                // 获取或创建对象池
                ObjectPoolManager.getInstance().getOrCreatePool(clazz);
                
                // 尝试从对象池获取
                T object = ObjectPoolManager.getInstance().acquire(clazz);
                if (object != null) {
                    // 设置对象的属性
                    if (args.length >= 2) {
                        // 处理不同类型的参数
                        if (args[0] instanceof Number) {
                            object.setX(((Number) args[0]).floatValue());
                        }
                        if (args[1] instanceof Number) {
                            object.setY(((Number) args[1]).floatValue());
                        }
                    }
                    return object;
                }
            } catch (Exception poolEx) {
                // 对象池获取失败，记录异常
                System.err.println("Object pool acquire failed: " + poolEx.getMessage());
            }
            
            // 直接创建对象
            try {
                // 查找匹配的构造函数
                for (java.lang.reflect.Constructor<?> constructor : clazz.getConstructors()) {
                    if (constructor.getParameterCount() == args.length) {
                        T object = (T) constructor.newInstance(args);
                        
                        // 将新创建的对象添加到对象池中（如果对象池存在）
                        try {
                            if (objectPoolsInitialized) {
                                ObjectPoolManager.getInstance().release(object);
                                // 重新从对象池获取，这样可以确保对象被正确跟踪
                                return ObjectPoolManager.getInstance().acquire(clazz);
                            }
                        } catch (Exception e) {
                            // 忽略异常，直接返回创建的对象
                        }
                        
                        return object;
                    }
                }
                throw new IllegalArgumentException("No suitable constructor found for " + clazz.getName());
            } catch (Exception ex) {
                throw new RuntimeException("Failed to create object: " + ex.getMessage(), ex);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create object: " + e.getMessage(), e);
        }
    }
    
    /**
     * 回收对象到对象池
     * @param <T> 对象类型
     * @param object 要回收的对象
     */
    public static <T extends Obj> void release(T object) {
        if (object == null) {
            return;
        }
        
        try {
            ObjectPoolManager.getInstance().release(object);
        } catch (Exception e) {
            // 如果对象池未初始化或失败，忽略异常
            // 对象会被 GC 回收
        }
    }
    
    /**
     * 初始化对象池
     */
    public static void initializeObjectPools() {
        if (objectPoolsInitialized) {
            return; // 已经初始化过
        }
        
        synchronized (poolInitLock) {
            if (objectPoolsInitialized) {
                return; // 双重检查锁定
            }
            
            try {
                ObjectPoolManager manager = ObjectPoolManager.getInstance();
                
                // 注册基类对象池
                registerPool(manager, "stg.entity.bullet.Bullet", 100, 500);
                registerPool(manager, "stg.entity.enemy.Enemy", 20, 100);
                registerPool(manager, "stg.entity.item.Item", 50, 200);
                
                // 注册用户定义的子弹类
                registerPool(manager, "user.bullet.DefaultPlayerMainBullet", 50, 200);
                registerPool(manager, "user.bullet.SimpleBullet", 100, 500);
                
                // 注册用户定义的敌人类
                registerPool(manager, "user.enemy.DefaultEnemy", 10, 50);
                registerPool(manager, "user.enemy.__FairyEnemy", 15, 75);
                registerPool(manager, "user.enemy.__MidFairyEnemy", 5, 25);
                registerPool(manager, "user.boss.__MinorikoBoss", 1, 5);
                
                // 初始化所有对象池
                manager.initializeAllPools();
                
                objectPoolsInitialized = true;
            } catch (Exception e) {
                // 初始化失败，记录异常但继续运行
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 注册指定类型的对象池
     * @param manager 对象池管理器
     * @param className 类名
     * @param initialCapacity 初始容量
     * @param maxCapacity 最大容量
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void registerPool(ObjectPoolManager manager, String className, int initialCapacity, int maxCapacity) {
        try {
            Class<?> clazz = Class.forName(className);
            
            // 检查是否已经注册
            if (manager.hasPool(clazz)) {
                return;
            }
            
            // 创建对象池
            ConcurrentLinkedObjectPool pool = new ConcurrentLinkedObjectPool(
                new ObjectFactory() {
                    @Override
                    public Object create() {
                        // 对于不同类型的对象，使用不同的默认参数
                        try {
                            if (className.contains("FairyEnemy")) {
                                return clazz.getConstructor(float.class, float.class).newInstance(0.0f, 0.0f);
                            } else if (className.contains("MidFairyEnemy")) {
                                return clazz.getConstructor(float.class, float.class).newInstance(0.0f, 0.0f);
                            } else if (className.contains("DefaultEnemy")) {
                                return clazz.getConstructor(float.class, float.class).newInstance(0.0f, 0.0f);
                            } else if (className.contains("TestBoss")) {
                                return clazz.getConstructor(float.class, float.class).newInstance(0.0f, 0.0f);
                            } else if (className.contains("MinorikoBoss")) {
                                return clazz.getConstructor(float.class, float.class).newInstance(0.0f, 0.0f);
                            } else if (className.contains("DefaultPlayerMainBullet")) {
                                return clazz.getConstructor(float.class, float.class).newInstance(0.0f, 0.0f);
                            } else if (className.contains("SimpleBullet")) {
                                return clazz.getConstructor(float.class, float.class, float.class, float.class, float.class, java.awt.Color.class).newInstance(0.0f, 0.0f, 0.0f, 0.0f, 5.0f, java.awt.Color.RED);
                            } else if (className.contains("Bullet")) {
                                return clazz.getConstructor(float.class, float.class).newInstance(0.0f, 0.0f);
                            } else if (className.contains("Enemy")) {
                                return clazz.getConstructor(float.class, float.class).newInstance(0.0f, 0.0f);
                            } else if (className.contains("Item")) {
                                return clazz.getConstructor(float.class, float.class, float.class, float.class).newInstance(0.0f, 0.0f, 0.0f, 0.0f);
                            } else {
                                // 尝试使用默认构造函数
                                return clazz.newInstance();
                            }
                        } catch (Exception e) {
                            // 如果创建失败，返回一个简单的实例
                            System.err.println("Failed to create object for " + className + ": " + e.getMessage());
                            return null;
                        }
                    }
                },
                0, // 初始容量为 0，避免初始化时创建对象
                maxCapacity
            );
            
            // 注册对象池
            manager.registerPool(clazz, pool);
            System.out.println("Registered pool for " + className + ", pool size: " + pool.size());
        } catch (Exception e) {
            // 注册失败，记录异常但继续
            e.printStackTrace();
        }
    }
    
    /**
     * 检查对象池是否已初始化
     * @return 是否已初始化
     */
    public static boolean isObjectPoolsInitialized() {
        return objectPoolsInitialized;
    }
}