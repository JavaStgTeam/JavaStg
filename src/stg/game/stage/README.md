# stg.game.stage 包

## 功能描述
stg.game.stage 包是 JavaStg 游戏引擎的关卡系统包，提供了关卡的基本实现、关卡组管理和关卡组管理器。关卡系统负责管理游戏的关卡流程，包括关卡的加载、开始、结束，以及关卡组的管理和切换。

## 包含文件
1. **Stage.java**：关卡类，管理单个关卡的逻辑，包括关卡的状态管理、敌人添加、波次管理等。
2. **StageCompletionCondition.java**：关卡完成条件接口，定义了关卡完成的判断方法。
3. **StageGroup.java**：关卡组类，管理多个关卡的顺序、切换和状态，支持不同难度级别。
4. **StageGroupInfo.java**：关卡组信息注解，用于标记关卡组类，支持注解扫描自动发现。
5. **StageGroupManager.java**：关卡组管理器，负责管理所有关卡组，包括自动发现和创建实例。

## 核心功能
1. **关卡基本管理**：实现关卡的创建、加载、开始、结束和清理等基本操作。
2. **关卡状态管理**：使用状态枚举（CREATED、LOADED、STARTED、COMPLETED、CLEANED_UP）管理关卡的生命周期。
3. **波次管理**：实现关卡中的敌人波次逻辑，通过帧计数控制敌人的生成。
4. **关卡完成条件**：支持自定义关卡完成条件，通过接口实现灵活的完成判断。
5. **关卡组管理**：实现关卡组的创建、关卡添加、关卡切换等功能。
6. **难度级别**：支持不同难度级别（EASY、NORMAL、HARD、LUNATIC）的设置。
7. **关卡组自动发现**：通过注解扫描和反射机制自动发现并创建关卡组实例，支持在不同环境下的稳定工作。
8. **关卡组状态管理**：管理关卡组的当前关卡、完成状态等。

## 设计理念
stg.game.stage 包采用了层次化的设计模式，通过不同的类负责不同的功能：
- **Stage**：负责单个关卡的逻辑管理，是一个抽象类，需要子类实现具体的关卡逻辑。
- **StageCompletionCondition**：定义关卡完成的判断标准，支持自定义完成条件。
- **StageGroup**：负责多个关卡的管理和切换，支持不同难度级别。
- **StageGroupManager**：负责全局关卡组的管理，提供自动发现功能。

这种设计使得关卡系统具有良好的扩展性和灵活性，可以方便地添加新的关卡、关卡组和完成条件。

## 依赖关系
- 依赖 `stg.game.enemy.Enemy` 类，用于关卡中的敌人管理。
- 依赖 `stg.game.ui.GameCanvas` 类，用于获取游戏世界引用和画布尺寸。

## 使用示例

### 创建关卡
```java
// 创建自定义关卡类
public class MyStage extends Stage {
    public MyStage(int stageId, String stageName, GameCanvas gameCanvas) {
        super(stageId, stageName, gameCanvas);
    }
    
    @Override
    protected void initStage() {
        // 初始化关卡资源
    }
    
    @Override
    public void load() {
        // 加载关卡资源
        setLoaded();
    }
    
    @Override
    protected void updateWaveLogic() {
        // 实现波次逻辑
        if (getCurrentFrame() == 60) {
            // 在第60帧添加敌人
            Enemy enemy = new Enemy(0, 0, 100, 2, 3.0f, 10);
            addEnemy(enemy);
        }
    }
    
    @Override
    public Stage nextStage() {
        // 返回下一关
        return new MyNextStage(2, "关卡2", getGameCanvas());
    }
}
```

### 创建关卡组

#### 使用注解标记关卡组（推荐）
```java
import stg.game.stage.StageGroup;
import stg.game.stage.StageGroupInfo;
import stg.game.ui.GameCanvas;

// 使用 @StageGroupInfo 注解标记关卡组
@StageGroupInfo(
    name = "我的关卡组",
    description = "包含多个关卡的关卡组",
    difficulty = StageGroup.Difficulty.NORMAL
)
public class MyStageGroup extends StageGroup {
    public MyStageGroup(GameCanvas gameCanvas) {
        super("我的关卡组", "包含多个关卡的关卡组", Difficulty.NORMAL, gameCanvas);
    }
    
    @Override
    protected void initStages() {
        // 添加关卡
        addStage(new MyStage(1, "关卡1", getGameCanvas()));
        addStage(new MyNextStage(2, "关卡2", getGameCanvas()));
    }
}
```

#### 传统方式（向后兼容）
```java
import stg.game.stage.StageGroup;
import stg.game.ui.GameCanvas;

// 传统方式创建关卡组（无需注解）
public class MyStageGroup extends StageGroup {
    public MyStageGroup(GameCanvas gameCanvas) {
        super("我的关卡组", "包含多个关卡的关卡组", Difficulty.NORMAL, gameCanvas);
    }
    
    @Override
    protected void initStages() {
        // 添加关卡
        addStage(new MyStage(1, "关卡1", getGameCanvas()));
        addStage(new MyNextStage(2, "关卡2", getGameCanvas()));
    }
}
```

### 使用关卡组管理器
```java
// 初始化关卡组管理器
StageGroupManager manager = StageGroupManager.getInstance();
manager.init(gameCanvas);

// 获取所有关卡组
List<StageGroup> stageGroups = manager.getStageGroups();

// 获取指定关卡组
StageGroup myStageGroup = manager.getStageGroupByName("我的关卡组");

// 开始关卡组
myStageGroup.start();

// 在游戏循环中更新关卡组
public void gameLoop() {
    myStageGroup.update();
    // 其他游戏逻辑
}
```

### 自定义关卡完成条件
```java
// 创建自定义关卡完成条件
public class EnemyClearCondition implements StageCompletionCondition {
    @Override
    public boolean isCompleted(Stage stage) {
        // 检查关卡中是否还有敌人
        return stage.getEnemies().isEmpty();
    }
}

// 设置关卡完成条件
stage.setCompletionCondition(new EnemyClearCondition());
```

## 扩展建议
1. **添加关卡编辑器**：可以开发一个关卡编辑器，方便用户可视化编辑关卡和波次。
2. **实现关卡存档系统**：添加关卡进度存档功能，允许玩家保存和加载关卡进度。
3. **添加关卡奖励系统**：实现关卡完成后的奖励机制，如道具、分数奖励等。
4. **实现关卡难度动态调整**：根据玩家的表现动态调整关卡难度，提供更好的游戏体验。
5. **添加关卡背景和音乐**：为不同的关卡添加不同的背景和音乐，增强游戏的沉浸感。
6. **实现关卡选择界面**：开发一个关卡选择界面，允许玩家选择要挑战的关卡。
7. **添加关卡评分系统**：为关卡完成添加评分系统，根据完成时间、剩余生命等因素计算评分。

## StageGroupInfo 注解

### 注解属性
1. **name**：关卡组名称，与构造函数中的 groupName 参数对应。
2. **description**：关卡组描述，与构造函数中的 description 参数对应。
3. **difficulty**：难度级别，与构造函数中的 difficulty 参数对应，使用 StageGroup.Difficulty 枚举。
4. **iconPath**：图标路径（可选），与构造函数中的 iconPath 参数对应，默认为空字符串。

### 使用示例
```java
import stg.game.stage.StageGroup;
import stg.game.stage.StageGroupInfo;
import stg.game.ui.GameCanvas;

// 完整的注解使用示例
@StageGroupInfo(
    name = "东方风神录一面",
    description = "秋静叶的山道",
    difficulty = StageGroup.Difficulty.NORMAL,
    iconPath = "path/to/icon.png"
)
public class MountainPathStageGroup extends StageGroup {
    public MountainPathStageGroup(GameCanvas gameCanvas) {
        super("东方风神录一面", "秋静叶的山道", StageGroup.Difficulty.NORMAL, "path/to/icon.png", gameCanvas);
    }
    
    @Override
    protected void initStages() {
        // 添加关卡
    }
}
```

## 关键方法

### Stage 类
- `Stage(int stageId, String stageName, GameCanvas gameCanvas)`：构造函数，创建关卡对象。
- `start()`：开始关卡。
- `end()`：结束关卡。
- `isActive()`：检查关卡是否激活。
- `nextStage()`：获取下一关。
- `load()`：加载关卡。
- `cleanup()`：清理关卡资源。
- `addEnemy(Enemy enemy)`：添加敌人到关卡。
- `isCompleted()`：检查关卡是否完成。
- `isStarted()`：检查关卡是否已开始。
- `getStageName()`：获取关卡名称。
- `getStageId()`：获取关卡ID。
- `getEnemies()`：获取当前关卡的敌人列表。
- `setCompletionCondition(StageCompletionCondition condition)`：设置关卡完成条件。
- `update()`：更新关卡逻辑。
- `updateWaveLogic()`：更新波次逻辑（可重写）。
- `getCurrentFrame()`：获取当前帧数。
- `reset()`：重置关卡。

### StageCompletionCondition 接口
- `isCompleted(Stage stage)`：检查关卡是否完成。

### StageGroup 类
- `StageGroup(String groupName, String description, Difficulty difficulty, GameCanvas gameCanvas)`：构造函数，创建关卡组对象。
- `addStage(Stage stage)`：添加关卡到关卡组。
- `start()`：开始关卡组。
- `nextStage()`：进入下一关。
- `goToStage(int stageIndex)`：进入指定关卡。
- `getCurrentStage()`：获取当前关卡。
- `getStageCount()`：获取关卡组中的关卡数量。
- `getCurrentStageIndex()`：获取当前关卡索引。
- `isCompleted()`：检查关卡组是否完成。
- `getGroupName()`：获取关卡组名称。
- `cleanup()`：清理关卡组资源。
- `update()`：更新关卡组状态。
- `reset()`：重置关卡组。
- `getGameCanvas()`：获取游戏画布引用。
- `getDescription()`：获取关卡组描述。
- `setDescription(String description)`：设置关卡组描述。
- `getDifficulty()`：获取难度级别。
- `setDifficulty(Difficulty difficulty)`：设置难度级别。
- `getIconPath()`：获取图标路径。
- `setIconPath(String iconPath)`：设置图标路径。
- `isUnlockable()`：检查关卡组是否可解锁。
- `getDisplayInfo()`：获取关卡组显示信息。
- `getDisplayName()`：获取关卡组显示名称。
- `initStages()`：初始化关卡组（可重写）。

### StageGroupManager 类
- `getInstance()`：获取单例实例。
- `init(GameCanvas gameCanvas)`：初始化关卡组。
- `getStageGroups()`：获取所有关卡组。
- `getStageGroupByName(String name)`：根据名称获取关卡组。
- `cleanup()`：清理资源。