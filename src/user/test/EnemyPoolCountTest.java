package user.test;

import stg.util.objectpool.ObjectPoolManager;
import stg.entity.base.Obj;
import user.enemy.__FairyEnemy;
import user.enemy.__MidFairyEnemy;
import user.boss.__MinorikoBoss;

/**
 * 敌人对象池计数测试
 * 测试敌人是否被正确计入对象池的对象数
 */
public class EnemyPoolCountTest {
    
    public static void main(String[] args) {
        System.out.println("开始敌人对象池计数测试...");
        
        try {
            // 初始化对象池
            Obj.initializeObjectPools();
            System.out.println("对象池初始化完成");
            
            ObjectPoolManager manager = ObjectPoolManager.getInstance();
            System.out.println("初始对象池计数: " + manager.getTotalObjectCount());
            
            // 测试创建 FairyEnemy
            System.out.println("\n1. 测试创建 FairyEnemy");
            for (int i = 0; i < 5; i++) {
                __FairyEnemy fairy = Obj.create(__FairyEnemy.class, 100.0f + i * 20, 200.0f);
                System.out.println("创建 FairyEnemy " + i + ", 当前计数: " + manager.getTotalObjectCount());
                Obj.release(fairy);
                System.out.println("回收 FairyEnemy " + i + ", 当前计数: " + manager.getTotalObjectCount());
            }
            
            // 测试创建 MidFairyEnemy
            System.out.println("\n2. 测试创建 MidFairyEnemy");
            for (int i = 0; i < 3; i++) {
                __MidFairyEnemy midFairy = Obj.create(__MidFairyEnemy.class, 150.0f + i * 30, 250.0f);
                System.out.println("创建 MidFairyEnemy " + i + ", 当前计数: " + manager.getTotalObjectCount());
                Obj.release(midFairy);
                System.out.println("回收 MidFairyEnemy " + i + ", 当前计数: " + manager.getTotalObjectCount());
            }
            
            // 测试创建 Boss
            System.out.println("\n3. 测试创建 Boss");
            __MinorikoBoss boss = Obj.create(__MinorikoBoss.class, 0.0f, 100.0f);
            System.out.println("创建 Boss, 当前计数: " + manager.getTotalObjectCount());
            Obj.release(boss);
            System.out.println("回收 Boss, 当前计数: " + manager.getTotalObjectCount());
            
            // 最终计数
            System.out.println("\n最终对象池计数: " + manager.getTotalObjectCount());
            System.out.println("敌人对象池计数测试完成！");
            
        } catch (Exception e) {
            System.err.println("测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}