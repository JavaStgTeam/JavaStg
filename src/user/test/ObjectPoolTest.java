package user.test;

import stg.entity.bullet.Bullet;
import stg.entity.enemy.Enemy;
import stg.entity.item.Item;
import stg.util.objectpool.ConcurrentLinkedObjectPool;
import stg.util.objectpool.ObjectFactory;
import stg.util.objectpool.ObjectPoolManager;

import java.awt.Color;

/**
 * 对象池测试类
 * 测试对象池的基本功能和性能
 * 
 * @date 2026-02-20
 * @author JavaSTG Team
 */
public class ObjectPoolTest {
    
    private static final int TEST_COUNT = 100000;
    
    public static void main(String[] args) {
        ObjectPoolTest test = new ObjectPoolTest();
        
        System.out.println("开始测试对象池系统...");
        System.out.println("测试数量: " + TEST_COUNT);
        System.out.println();
        
        // 测试基本功能
        test.testBasicFunctionality();
        System.out.println();
        
        // 测试性能
        test.testPerformance();
        System.out.println();
        
        // 测试对象池管理器
        test.testObjectPoolManager();
        
        System.out.println("对象池测试完成！");
    }
    
    /**
     * 测试对象池的基本功能
     */
    public void testBasicFunctionality() {
        System.out.println("=== 测试基本功能 ===");
        
        // 创建子弹对象池
        ConcurrentLinkedObjectPool<TestBullet> bulletPool = new ConcurrentLinkedObjectPool<TestBullet>(
            new ObjectFactory<TestBullet>() {
                @Override
                public TestBullet create() {
                    return new TestBullet(0, 0, 0, 0, 10, Color.RED);
                }
            },
            10, // 初始容量
            100 // 最大容量
        );
        
        // 初始化对象池
        bulletPool.initialize(10);
        System.out.println("子弹对象池初始大小: " + bulletPool.size());
        
        // 获取对象
        TestBullet bullet1 = bulletPool.acquire();
        TestBullet bullet2 = bulletPool.acquire();
        System.out.println("获取两个子弹后，对象池大小: " + bulletPool.size());
        
        // 使用对象
        bullet1.setVx(1);
        bullet1.setVy(-1);
        bullet1.setDamage(5);
        System.out.println("子弹1速度: (" + bullet1.getVx() + ", " + bullet1.getVy() + ")");
        System.out.println("子弹1伤害: " + bullet1.getDamage());
        
        // 回收对象
        bulletPool.release(bullet1);
        bulletPool.release(bullet2);
        System.out.println("回收两个子弹后，对象池大小: " + bulletPool.size());
        
        // 再次获取对象
        TestBullet bullet3 = bulletPool.acquire();
        System.out.println("再次获取子弹后，对象池大小: " + bulletPool.size());
        System.out.println("新获取的子弹速度: (" + bullet3.getVx() + ", " + bullet3.getVy() + ")");
        System.out.println("新获取的子弹伤害: " + bullet3.getDamage());
        
        // 清理对象池
        bulletPool.clear();
        System.out.println("清理对象池后，大小: " + bulletPool.size());
        System.out.println("对象池是否为空: " + bulletPool.isEmpty());
    }
    
    /**
     * 测试对象池的性能
     */
    public void testPerformance() {
        System.out.println("=== 测试性能 ===");
        
        // 创建子弹对象池
        ConcurrentLinkedObjectPool<TestBullet> bulletPool = new ConcurrentLinkedObjectPool<TestBullet>(
            new ObjectFactory<TestBullet>() {
                @Override
                public TestBullet create() {
                    return new TestBullet(0, 0, 0, 0, 10, Color.RED);
                }
            },
            1000 // 初始容量
        );
        
        // 预热
        for (int i = 0; i < 1000; i++) {
            TestBullet bullet = bulletPool.acquire();
            bulletPool.release(bullet);
        }
        
        // 测试对象池性能
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < TEST_COUNT; i++) {
            TestBullet bullet = bulletPool.acquire();
            // 模拟使用
            bullet.setVx(i % 10);
            bullet.setVy(-(i % 10));
            bullet.setDamage(i % 5 + 1);
            bulletPool.release(bullet);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("使用对象池耗时: " + (endTime - startTime) + "ms");
        System.out.println("对象池最终大小: " + bulletPool.size());
        
        // 测试直接创建对象的性能
        startTime = System.currentTimeMillis();
        for (int i = 0; i < TEST_COUNT; i++) {
            TestBullet bullet = new TestBullet(0, 0, 0, 0, 10, Color.RED);
            // 模拟使用
            bullet.setVx(i % 10);
            bullet.setVy(-(i % 10));
            bullet.setDamage(i % 5 + 1);
            // 直接丢弃，依赖GC
        }
        endTime = System.currentTimeMillis();
        System.out.println("直接创建对象耗时: " + (endTime - startTime) + "ms");
    }
    
    /**
     * 测试对象池管理器
     */
    public void testObjectPoolManager() {
        System.out.println("=== 测试对象池管理器 ===");
        
        ObjectPoolManager manager = ObjectPoolManager.getInstance();
        
        // 注册子弹对象池
        ConcurrentLinkedObjectPool<TestBullet> bulletPool = new ConcurrentLinkedObjectPool<TestBullet>(
            new ObjectFactory<TestBullet>() {
                @Override
                public TestBullet create() {
                    return new TestBullet(0, 0, 0, 0, 10, Color.RED);
                }
            }
        );
        manager.registerPool(TestBullet.class, bulletPool);
        
        // 注册敌人对象池
        ConcurrentLinkedObjectPool<TestEnemy> enemyPool = new ConcurrentLinkedObjectPool<TestEnemy>(
            new ObjectFactory<TestEnemy>() {
                @Override
                public TestEnemy create() {
                    return new TestEnemy(0, 0, 0, 0, 20, Color.BLUE, 10);
                }
            }
        );
        manager.registerPool(TestEnemy.class, enemyPool);
        
        // 注册道具对象池
        ConcurrentLinkedObjectPool<TestItem> itemPool = new ConcurrentLinkedObjectPool<TestItem>(
            new ObjectFactory<TestItem>() {
                @Override
                public TestItem create() {
                    return new TestItem(0, 0, 0, 0, 15, Color.YELLOW);
                }
            }
        );
        manager.registerPool(TestItem.class, itemPool);
        
        System.out.println("对象池管理器中注册的对象池数量: " + manager.getPoolCount());
        
        // 初始化所有对象池
        manager.initializeAllPools();
        System.out.println("初始化所有对象池完成");
        
        // 使用对象池管理器获取对象
        TestBullet bullet = manager.acquire(TestBullet.class);
        TestEnemy enemy = manager.acquire(TestEnemy.class);
        TestItem item = manager.acquire(TestItem.class);
        
        System.out.println("成功从对象池管理器获取对象");
        
        // 使用对象
        bullet.setDamage(10);
        enemy.takeDamage(5);
        
        System.out.println("子弹伤害: " + bullet.getDamage());
        System.out.println("敌人生命值: " + enemy.getHp());
        
        // 回收对象
        manager.release(bullet);
        manager.release(enemy);
        manager.release(item);
        
        System.out.println("成功回收对象到对象池管理器");
        
        // 清理
        manager.clearAllPools();
        System.out.println("清理所有对象池完成");
    }
    
    /**
     * 测试子弹类
     */
    private static class TestBullet extends Bullet {
        
        public TestBullet(float x, float y, float vx, float vy, float size, Color color) {
            super(x, y, vx, vy, size, color);
        }
        
        @Override
        protected void onTaskStart() {
            // 空实现
        }
        
        @Override
        protected void onTaskEnd() {
            // 空实现
        }
    }
    
    /**
     * 测试敌人类
     */
    private static class TestEnemy extends Enemy {
        
        public TestEnemy(float x, float y, float vx, float vy, float size, Color color, int hp) {
            super(x, y, vx, vy, size, color, hp);
        }
        
        @Override
        protected void onTaskStart() {
            // 空实现
        }
        
        @Override
        protected void onTaskEnd() {
            // 空实现
        }
    }
    
    /**
     * 测试道具类
     */
    private static class TestItem extends Item {
        
        public TestItem(float x, float y, float vx, float vy, float size, Color color) {
            super(x, y, vx, vy, size, color);
        }
        
        @Override
        protected void onTaskStart() {
            // 空实现
        }
        
        @Override
        protected void onTaskEnd() {
            // 空实现
        }
    }
}
