package user.test;

import stg.entity.base.Obj;
import user.bullet.DefaultPlayerMainBullet;
import user.enemy.__FairyEnemy;

/**
 * 对象池集成测试
 * 测试修改后的对象池是否能正常工作
 */
public class ObjectPoolIntegrationTest {
    
    public static void main(String[] args) {
        System.out.println("开始对象池集成测试...");
        
        // 1. 初始化对象池
        System.out.println("1. 初始化对象池");
        Obj.initializeObjectPools();
        System.out.println("对象池初始化完成");
        
        // 2. 测试创建子弹
        System.out.println("\n2. 测试创建子弹");
        testCreateBullet();
        
        // 3. 测试创建敌人
        System.out.println("\n3. 测试创建敌人");
        testCreateEnemy();
        
        // 4. 测试创建道具
        System.out.println("\n4. 测试创建道具");
        testCreateItem();
        
        // 5. 测试对象回收
        System.out.println("\n5. 测试对象回收");
        testReleaseObject();
        
        System.out.println("\n对象池集成测试完成！");
    }
    
    private static void testCreateBullet() {
        try {
            DefaultPlayerMainBullet bullet = Obj.create(DefaultPlayerMainBullet.class, 100, 200);
            System.out.println("成功创建子弹: " + bullet.getClass().getName());
            System.out.println("子弹位置: (" + bullet.getX() + ", " + bullet.getY() + ")");
            
            // 测试回收
            Obj.release(bullet);
            System.out.println("成功回收子弹");
        } catch (Exception e) {
            System.err.println("创建子弹失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testCreateEnemy() {
        try {
            __FairyEnemy enemy = Obj.create(__FairyEnemy.class, 150, 300);
            System.out.println("成功创建敌人: " + enemy.getClass().getName());
            System.out.println("敌人位置: (" + enemy.getX() + ", " + enemy.getY() + ")");
            
            // 测试回收
            Obj.release(enemy);
            System.out.println("成功回收敌人");
        } catch (Exception e) {
            System.err.println("创建敌人失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testCreateItem() {
        try {
            // Item 是抽象类，不能直接创建实例
            // 这里跳过道具测试，因为需要具体的子类
            System.out.println("跳过道具测试: Item 是抽象类");
        } catch (Exception e) {
            System.err.println("创建道具失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testReleaseObject() {
        try {
            // 创建多个对象并回收
            for (int i = 0; i < 5; i++) {
                DefaultPlayerMainBullet bullet = Obj.create(DefaultPlayerMainBullet.class, 100 + i * 10, 200 + i * 10);
                System.out.println("创建子弹 " + i + ": (" + bullet.getX() + ", " + bullet.getY() + ")");
                Obj.release(bullet);
                System.out.println("回收子弹 " + i);
            }
            System.out.println("批量创建和回收测试完成");
        } catch (Exception e) {
            System.err.println("批量测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
