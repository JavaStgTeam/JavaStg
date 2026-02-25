package stg.stage;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import stg.core.GameWorld;

/**
 * 关卡组工厂 - 负责创建StageGroup实例
 * @since 2026-02-16
 */
public class StageGroupFactory {
    
    /**
     * 根据类列表创建StageGroup实例
     * @param stageGroupClasses 关卡组类列表
     * @param gameWorld 游戏世界引用
     * @return 创建的StageGroup实例列表
     */
    public List<StageGroup> createInstances(List<Class<?>> stageGroupClasses, GameWorld gameWorld) {
        List<StageGroup> instances = new ArrayList<>();
        
        if (stageGroupClasses == null || stageGroupClasses.isEmpty()) {
            System.out.println("没有可创建的关卡组类");
            return instances;
        }
        
        System.out.println("开始创建关卡组实例...");
        
        for (Class<?> clazz : stageGroupClasses) {
            try {
                StageGroup instance = createInstance(clazz, gameWorld);
                if (instance != null) {
                    instances.add(instance);
                    System.out.println("创建关卡组实例成功: " + instance.getDisplayName());
                }
            } catch (Exception e) {
                System.err.println("创建关卡组实例失败 " + clazz.getName());
                System.err.println("错误类型: " + e.getClass().getName());
                System.err.println("错误信息: " + e.getMessage());
            }
        }
        
        System.out.println("创建关卡组实例完成，共创建 " + instances.size() + " 个实例");
        return instances;
    }
    
    /**
     * 创建单个StageGroup实例
     * @param clazz 关卡组类
     * @param gameWorld 游戏世界引用
     * @return 创建的StageGroup实例，如果创建失败则返回null
     * @throws NoSuchMethodException 如果构造方法不存在
     * @throws InstantiationException 如果实例化失败
     * @throws IllegalAccessException 如果访问权限不足
     * @throws InvocationTargetException 如果构造方法调用失败
     */
    public StageGroup createInstance(Class<?> clazz, GameWorld gameWorld) 
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        // 检查是否是有效的StageGroup子类
        if (!isValidStageGroupClass(clazz)) {
            System.err.println("无效的关卡组类: " + clazz.getName());
            return null;
        }
        
        // 尝试通过构造方法创建实例
        return (StageGroup) clazz.getConstructor(GameWorld.class).newInstance(gameWorld);
    }
    
    /**
     * 检查是否是有效的StageGroup子类
     * @param clazz 要检查的类
     * @return 如果是有效的StageGroup子类则返回true
     */
    private boolean isValidStageGroupClass(Class<?> clazz) {
        return StageGroup.class.isAssignableFrom(clazz) && 
               !java.lang.reflect.Modifier.isAbstract(clazz.getModifiers()) && 
               clazz != StageGroup.class;
    }
}