package stg.stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import stg.ui.GameCanvas;
import stg.util.LogUtil;

/**
 * 关卡组管理器 - 负责管理所有关卡组实例
 * @since 2026-01-30
 */
public class StageGroupManager {
    /**
     * 枚举单例实现
     */
    public enum Instance {
        INSTANCE;
        
        private final StageGroupManager manager;
        
        Instance() {
            manager = new StageGroupManager();
        }
        
        public StageGroupManager getManager() {
            return manager;
        }
    }
    
    private final List<StageGroup> stageGroups;
    private final StageGroupDiscovery discovery;
    private final StageGroupFactory factory;

    /**
     * 私有构造函数
     */
    private StageGroupManager() {
        stageGroups = new ArrayList<>();
        discovery = new StageGroupDiscovery();
        factory = new StageGroupFactory();
    }

    /**
     * 获取单例实例
     * @return 关卡组管理器实例
     */
    public static StageGroupManager getInstance() {
        return Instance.INSTANCE.getManager();
    }

    /**
     * 初始化关卡组
     * @param gameCanvas 游戏画布引用
     */
    public void init(GameCanvas gameCanvas) {
        stageGroups.clear();
        
        try {
            // 1. 发现关卡组类
            List<Class<?>> stageGroupClasses = discovery.discoverStageGroupClasses();
            
            // 2. 创建关卡组实例
            List<StageGroup> instances = factory.createInstances(stageGroupClasses, gameCanvas);
            
            // 3. 添加到管理器
            stageGroups.addAll(instances);
            
            System.out.println("关卡组初始化完成，共管理 " + stageGroups.size() + " 个关卡组");
        } catch (IOException e) {
            LogUtil.error("StageGroupManager", "关卡组初始化失败", e);
        }
    }

    /**
     * 获取所有关卡组
     * @return 关卡组列表
     */
    public List<StageGroup> getStageGroups() {
        return stageGroups;
    }

    /**
     * 根据名称获取关卡组
     * @param name 关卡组名称
     * @return 关卡组对象，不存在则返回null
     */
    public StageGroup getStageGroupByName(String name) {
        for (StageGroup group : stageGroups) {
            if (group.getDisplayName().equals(name)) {
                return group;
            }
        }
        return null;
    }

    /**
     * 清理资源
     */
    public void cleanup() {
        for (StageGroup group : stageGroups) {
            group.cleanup();
        }
        stageGroups.clear();
    }
    
    /**
     * 测试专用方法：重置管理器状态
     * 仅用于测试环境
     */
    public void resetForTest() {
        cleanup();
    }
    
    /**
     * 测试专用方法：手动添加关卡组
     * 仅用于测试环境
     * @param stageGroup 要添加的关卡组
     */
    public void addStageGroupForTest(StageGroup stageGroup) {
        if (stageGroup != null) {
            stageGroups.add(stageGroup);
        }
    }
    
    /**
     * 测试专用方法：获取关卡组列表的直接引用
     * 仅用于测试环境
     * @return 关卡组列表的直接引用
     */
    public List<StageGroup> getStageGroupsForTest() {
        return stageGroups;
    }
}
