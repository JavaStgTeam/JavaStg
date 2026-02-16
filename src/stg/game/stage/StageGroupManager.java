package stg.game.stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import stg.game.ui.GameCanvas;
import stg.game.util.AnnotationScanner;
import stg.util.LogUtil;

/**
 * 关卡组管理器 - 负责管理所有关卡组，包括自动发现和创建实例
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
    
    private List<StageGroup> stageGroups;

    /**
     * 私有构造函数
     */
    private StageGroupManager() {
        stageGroups = new ArrayList<>();
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
        
        // 自动发现并创建关卡组实例
        discoverStageGroups(gameCanvas);
    }

    /**
     * 自动发现所有StageGroup的子类并创建实例
     * @param gameCanvas 游戏画布引用
     */
    private void discoverStageGroups(GameCanvas gameCanvas) {
        try {
            System.out.println("开始发现关卡组...");
            
            // 1. 首先尝试使用注解扫描发现关卡组
            System.out.println("尝试使用注解扫描发现关卡组...");
            List<Class<?>> annotatedClasses = AnnotationScanner.scanClassesWithAnnotation("user.stageGroup", StageGroupInfo.class);
            
            for (Class<?> clazz : annotatedClasses) {
                try {
                    // 检查是否是StageGroup的子类且不是抽象类
                    if (StageGroup.class.isAssignableFrom(clazz) && !java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())) {
                        // 尝试创建实例
                        try {
                            StageGroup stageGroup = (StageGroup) clazz.getConstructor(GameCanvas.class).newInstance(gameCanvas);
                            stageGroups.add(stageGroup);
                            System.out.println("通过注解扫描发现关卡组 " + stageGroup.getDisplayName());
                        } catch (Exception e) {
                            System.err.println("创建关卡组实例失败 " + clazz.getName());
                            System.err.println("错误类型: " + e.getClass().getName());
                            System.err.println("错误信息: " + e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    LogUtil.error("StageGroupManager", "处理关卡组类时发生错误: " + clazz.getName(), e);
                }
            }
            
            // 2. 如果没有找到关卡组，尝试使用传统方法（向后兼容）
            if (stageGroups.isEmpty()) {
                System.out.println("注解扫描未发现关卡组，尝试使用传统方法...");
                
                // 直接尝试加载已知的关卡组类
                String[] stageGroupClasses = {
                    "user.stageGroup.CustomStageGroup"
                };
                
                for (String className : stageGroupClasses) {
                    try {
                        // 使用当前线程的上下文类加载器
                        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                        Class<?> clazz = classLoader.loadClass(className);
                        
                        // 检查是否是StageGroup的子类且不是抽象类
                        if (StageGroup.class.isAssignableFrom(clazz) && !java.lang.reflect.Modifier.isAbstract(clazz.getModifiers()) && clazz != StageGroup.class) {
                            // 尝试创建实例
                            try {
                                StageGroup stageGroup = (StageGroup) clazz.getConstructor(GameCanvas.class).newInstance(gameCanvas);
                                stageGroups.add(stageGroup);
                                System.out.println("自动发现关卡组 " + stageGroup.getDisplayName());
                            } catch (Exception e) {
                                System.err.println("创建关卡组实例失败 " + className);
                                System.err.println("错误信息: " + e.getMessage());
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        // 类未找到，继续尝试其他类
                    }
                }
                
                // 如果仍然没有找到关卡组，尝试扫描文件系统
                if (stageGroups.isEmpty()) {
                    System.out.println("直接加载失败，尝试使用文件系统扫描发现关卡组...");
                    // 扫描多个包
                    String[] packageNames = {
                        StageGroup.class.getPackage().getName(),
                        "user.stageGroup"
                    };
                    
                    for (String packageName : packageNames) {
                        String packagePath = packageName.replace('.', '/');
                        
                        // 获取包下的所有类文件
                        Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(packagePath);
                        while (resources.hasMoreElements()) {
                            URL resource = resources.nextElement();
                            String protocol = resource.getProtocol();
                            
                            if ("file".equals(protocol)) {
                                String filePath = resource.getFile();
                                File directory = new File(filePath);
                                
                                if (directory.exists()) {
                                    File[] files = directory.listFiles();
                                    if (files != null) {
                                        for (File file : files) {
                                            if (file.isFile() && file.getName().endsWith(".class")) {
                                                String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                                                try {
                                                    Class<?> clazz = Class.forName(className);
                                                    // 检查是否是StageGroup的子类且不是抽象类
                                                    if (StageGroup.class.isAssignableFrom(clazz) && !java.lang.reflect.Modifier.isAbstract(clazz.getModifiers()) && clazz != StageGroup.class) {
                                                        // 尝试创建实例
                                                        try {
                                                            StageGroup stageGroup = (StageGroup) clazz.getConstructor(GameCanvas.class).newInstance(gameCanvas);
                                                            stageGroups.add(stageGroup);
                                                            System.out.println("自动发现关卡组 " + stageGroup.getDisplayName());
                                                        } catch (Exception e) {
                                                            System.err.println("创建关卡组实例失败 " + className);
                                                            System.err.println("错误信息: " + e.getMessage());
                                                        }
                                                    }
                                                } catch (ClassNotFoundException e) {
                                                    // 类未找到，继续尝试其他类
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            System.out.println("发现关卡组完成，共发现 " + stageGroups.size() + " 个关卡组");
        } catch (Exception e) {
            LogUtil.error("StageGroupManager", "自动发现关卡组失败", e);
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
