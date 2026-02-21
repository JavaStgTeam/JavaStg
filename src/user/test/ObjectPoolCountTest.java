package user.test;

import stg.util.objectpool.ObjectPoolManager;
import stg.util.objectpool.ConcurrentLinkedObjectPool;
import stg.util.objectpool.ObjectFactory;
import user.bullet.DefaultPlayerMainBullet;

/**
 * 对象池计数测试
 * 测试窗口标题是否显示对象池中的对象数
 */
public class ObjectPoolCountTest {
    
    public static void main(String[] args) {
        System.out.println("开始对象池计数测试...");
        
        // 初始化对象池
        try {
            // 注册 Bullet 对象池
            ObjectPoolManager manager = ObjectPoolManager.getInstance();
            
            // 注册 DefaultPlayerMainBullet 对象池
            ConcurrentLinkedObjectPool<DefaultPlayerMainBullet> bulletPool = new ConcurrentLinkedObjectPool<DefaultPlayerMainBullet>(
                new ObjectFactory<DefaultPlayerMainBullet>() {
                    @Override
                    public DefaultPlayerMainBullet create() {
                        return new DefaultPlayerMainBullet(0, 0);
                    }
                },
                0, 100
            );
            manager.registerPool(DefaultPlayerMainBullet.class, bulletPool);
            
            System.out.println("对象池初始化完成");
            System.out.println("对象池数量: " + manager.getPoolCount());
            System.out.println("是否有 DefaultPlayerMainBullet 对象池: " + manager.hasPool(DefaultPlayerMainBullet.class));
            
            // 测试对象池计数
            testObjectPoolCount();
            
        } catch (Exception e) {
            System.err.println("初始化失败: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("对象池计数测试完成！");
    }
    
    private static void testObjectPoolCount() {
        ObjectPoolManager manager = ObjectPoolManager.getInstance();
        
        System.out.println("初始对象池计数: " + manager.getTotalObjectCount());
        
        // 创建一些对象并回收
        for (int i = 0; i < 5; i++) {
            try {
                DefaultPlayerMainBullet bullet = manager.acquire(DefaultPlayerMainBullet.class);
                System.out.println("创建子弹 " + i + ", 当前计数: " + manager.getTotalObjectCount());
                // 回收对象到对象池
                manager.release(bullet);
                System.out.println("回收子弹 " + i + ", 当前计数: " + manager.getTotalObjectCount());
            } catch (Exception e) {
                System.err.println("操作失败: " + e.getMessage());
            }
        }
        
        System.out.println("最终对象池计数: " + manager.getTotalObjectCount());
    }
}
