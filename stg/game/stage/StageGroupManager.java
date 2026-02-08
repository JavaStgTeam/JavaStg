package stg.game.stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import stg.game.ui.GameCanvas;

/**
 * 关卡组管理器 - 负责管理所有关卡组，包括自动发现和创建实例
 * @since 2026-01-30
 */
public class StageGroupManager {
    private static StageGroupManager instance;
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
        if (instance == null) {
            instance = new StageGroupManager();
        }
        return instance;
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
            // 扫描多个包
            String[] packageNames = {
                StageGroup.class.getPackage().getName(),
                "user.stageGroup"
            };
            
            for (String packageName : packageNames) {
                System.out.println("扫描包: " + packageName);
                String packagePath = packageName.replace('.', '/');
                System.out.println("包路径: " + packagePath);
                
                // 获取包下的所有类文件
                System.out.println("获取包下的所有类文件...");
                Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(packagePath);
                System.out.println("资源数量: " + (resources.hasMoreElements() ? "至少一个" : "零"));
                while (resources.hasMoreElements()) {
                    URL resource = resources.nextElement();
                    System.out.println("资源URL: " + resource.getFile());
                    File directory = new File(resource.getFile());
                    System.out.println("目录路径: " + directory.getAbsolutePath());
                    System.out.println("目录是否存在: " + directory.exists());
                    
                    if (directory.exists()) {
                        File[] files = directory.listFiles();
                        System.out.println("文件数量: " + (files != null ? files.length : 0));
                        if (files != null) {
                            for (File file : files) {
                                System.out.println("文件名: " + file.getName());
                                if (file.isFile() && file.getName().endsWith(".class")) {
                                    String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                                    System.out.println("类名: " + className);
                                    try {
                                        Class<?> clazz = Class.forName(className);
                                        System.out.println("加载类成功: " + className);
                                        // 检查是否是StageGroup的子类且不是抽象类
                                        if (StageGroup.class.isAssignableFrom(clazz) && !java.lang.reflect.Modifier.isAbstract(clazz.getModifiers()) && clazz != StageGroup.class) {
                                            System.out.println("是StageGroup的子类且不是抽象类: " + className);
                                            // 尝试创建实例
                                            try {
                                                System.out.println("尝试创建实例: " + className);
                                                StageGroup stageGroup = (StageGroup) clazz.getConstructor(GameCanvas.class).newInstance(gameCanvas);
                                                System.out.println("创建实例成功: " + className);
                                                stageGroups.add(stageGroup);
                                                System.out.println("自动发现关卡组 " + stageGroup.getDisplayName());
                                            } catch (Exception e) {
                                                System.out.println("创建关卡组实例失败 " + className);
                                                e.printStackTrace();
                                            }
                                        }
                                    } catch (ClassNotFoundException e) {
                                        System.out.println("加载类失败 " + className);
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                }
            }
            System.out.println("发现关卡组完成，共发现 " + stageGroups.size() + " 个关卡组");
        } catch (IOException e) {
            System.out.println("自动发现关卡组失败");
            e.printStackTrace();
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
}
