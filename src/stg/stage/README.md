# stage 包说明

## 功能概述

**stage 包**是游戏的关卡系统包，包含了关卡相关的类。关卡系统负责管理游戏的关卡流程、敌人生成、胜利条件等，是游戏内容的重要组成部分。

## 包含的类

| 类名 | 功能描述 |
|------|----------|
| Stage | 关卡类，实现了单个关卡的逻辑 |
| StageGroup | 关卡组类，管理多个关卡的集合 |
| StageGroupManager | 关卡组管理器，管理所有关卡组 |
| StageGroupFactory | 关卡组工厂，负责创建关卡组 |
| StageGroupDiscovery | 关卡组发现类，负责发现和加载关卡组 |
| StageGroupInfo | 关卡组信息类，存储关卡组的基本信息 |
| StageCompletionCondition | 关卡完成条件接口，定义关卡完成的条件 |

## 主要功能

### Stage 类
- **关卡逻辑**：实现单个关卡的核心逻辑
- **敌人生成**：管理关卡中的敌人生成
- **胜利条件**：定义关卡的胜利条件
- **难度调整**：支持不同难度的关卡配置
- **关卡进度**：跟踪和管理关卡的进度

### StageGroup 类
- **关卡集合**：管理多个相关联的关卡
- **难度设置**：为关卡组设置整体难度
- **关卡顺序**：定义关卡的播放顺序
- **关卡组信息**：存储关卡组的名称、描述等信息
- **奖励系统**：设置关卡组完成后的奖励

### StageGroupManager 类
- **关卡组管理**：管理游戏中的所有关卡组
- **单例模式**：使用单例模式确保全局唯一
- **关卡组访问**：提供访问和获取关卡组的方法
- **关卡组选择**：支持关卡组的选择和切换

### StageGroupFactory 类
- **关卡组创建**：负责创建关卡组实例
- **配置解析**：解析关卡组的配置信息
- **依赖注入**：为关卡组注入必要的依赖

### StageGroupDiscovery 类
- **关卡组发现**：自动发现游戏中的关卡组
- **反射机制**：使用反射机制加载关卡组类
- **注解扫描**：扫描关卡组相关的注解
- **动态加载**：支持动态加载自定义关卡组

### StageGroupInfo 类
- **信息存储**：存储关卡组的基本信息
- **元数据管理**：管理关卡组的元数据
- **序列化支持**：支持关卡组信息的序列化和反序列化

### StageCompletionCondition 接口
- **条件定义**：定义关卡完成的条件
- **条件检查**：检查关卡是否满足完成条件
- **扩展性**：便于实现不同类型的完成条件

## 类结构

```java
// StageCompletionCondition 接口
public interface StageCompletionCondition {
    boolean isCompleted(Stage stage);
    // 其他方法...
}

// Stage 类
public class Stage {
    private String name;
    private List<EnemySpawnEvent> spawnEvents;
    private StageCompletionCondition completionCondition;
    private double duration;
    
    public Stage(String name) {
        // 初始化...
    }
    
    public void update(double deltaTime) {
        // 更新关卡逻辑...
    }
    
    public boolean isCompleted() {
        return completionCondition.isCompleted(this);
    }
    
    // 其他方法...
}

// StageGroup 类
public class StageGroup {
    private String name;
    private String description;
    private List<Stage> stages;
    private int currentStageIndex;
    private Difficulty difficulty;
    
    public enum Difficulty {
        EASY, NORMAL, HARD, LUNATIC
    }
    
    public StageGroup(String name, String description) {
        // 初始化...
    }
    
    public void start() {
        // 开始关卡组...
    }
    
    public void update(double deltaTime) {
        // 更新当前关卡...
    }
    
    public boolean isCompleted() {
        // 检查是否所有关卡都已完成...
    }
    
    // 其他方法...
}

// StageGroupManager 类
public class StageGroupManager {
    private static class Instance {
        private static final StageGroupManager INSTANCE = new StageGroupManager();
    }
    
    private List<StageGroup> stageGroups;
    
    private StageGroupManager() {
        // 初始化...
    }
    
    public static StageGroupManager getInstance() {
        return Instance.INSTANCE;
    }
    
    public List<StageGroup> getStageGroups() {
        return stageGroups;
    }
    
    public StageGroup getStageGroup(String name) {
        // 根据名称获取关卡组...
    }
    
    // 其他方法...
}

// StageGroupFactory 类
public class StageGroupFactory {
    public StageGroup createStageGroup(String className) {
        // 创建关卡组实例...
    }
    
    // 其他方法...
}

// StageGroupDiscovery 类
public class StageGroupDiscovery {
    public List<StageGroup> discoverStageGroups() {
        // 发现和加载关卡组...
    }
    
    // 其他方法...
}

// StageGroupInfo 类
public class StageGroupInfo {
    private String name;
    private String description;
    private String author;
    private String version;
    
    // Getter和Setter方法...
}
```

## 使用示例

### 创建和使用关卡

```java
// 创建关卡完成条件
StageCompletionCondition condition = stage -> {
    // 检查是否所有敌人都已被摧毁
    return stage.getEnemies().isEmpty();
};

// 创建关卡
Stage stage1 = new Stage("第一关");
stage1.setCompletionCondition(condition);

// 添加敌人生成事件
stage1.addSpawnEvent(1.0, () -> {
    // 在1秒后生成敌人
    Enemy enemy = new Enemy(400, 100, 50, 2.0);
    gameWorld.addObject(enemy);
});

// 创建关卡组
StageGroup stageGroup = new StageGroup("新手教程", "适合新手的教程关卡组");
stageGroup.addStage(stage1);
stageGroup.setDifficulty(StageGroup.Difficulty.EASY);

// 注册关卡组
StageGroupManager.getInstance().addStageGroup(stageGroup);

// 开始关卡组
stageGroup.start();
```

### 关卡组发现和加载

```java
// 发现所有关卡组
List<StageGroup> stageGroups = StageGroupDiscovery.discoverStageGroups();

// 注册关卡组
StageGroupManager manager = StageGroupManager.getInstance();
for (StageGroup group : stageGroups) {
    manager.addStageGroup(group);
}

// 获取关卡组
StageGroup tutorialGroup = manager.getStageGroup("新手教程");

// 开始关卡组
if (tutorialGroup != null) {
    tutorialGroup.start();
}
```

## 设计说明

1. **模块化设计**：将关卡系统拆分为多个职责明确的类
2. **单例模式**：StageGroupManager 使用单例模式确保全局唯一
3. **工厂模式**：StageGroupFactory 负责创建关卡组实例
4. **反射机制**：StageGroupDiscovery 使用反射机制动态加载关卡组
5. **接口分离**：通过接口定义标准，便于扩展

## 开发建议

- 当需要创建新关卡时，创建 Stage 实例并配置相应的参数
- 当需要创建新关卡组时，创建 StageGroup 实例并添加关卡
- 当需要创建自定义关卡组时，继承 StageGroup 类并实现相应的方法
- 为关卡添加适当的敌人生成事件，确保关卡的挑战性
- 考虑关卡的难度曲线，确保游戏的可玩性
- 可以使用配置文件定义关卡，提高关卡的可维护性