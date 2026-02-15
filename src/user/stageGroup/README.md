# user.stageGroup 包

## 功能描述
user.stageGroup 包是 JavaStg 游戏引擎的用户自定义关卡组包，提供了用户自定义的关卡组类。关卡组是游戏中的重要元素，用于组织多个关卡，形成一个完整的游戏流程。

## 包含文件
1. **CustomStageGroup.java**：自定义关卡组类，继承自 `StageGroup` 类，用于测试关卡组功能，包含一个测试关卡。

## 核心功能
1. **关卡组创建**：允许用户创建自定义关卡组类，设置关卡组的名称、描述、难度等属性。
2. **关卡管理**：管理关卡组中的多个关卡，控制关卡的顺序、切换等。
3. **难度设置**：支持不同难度级别的关卡组，如简单、普通、困难、Lunatic等。
4. **状态管理**：管理关卡组的状态，如当前关卡、完成状态等。
5. **资源管理**：管理关卡组的资源加载和释放。

## 设计理念
user.stageGroup 包采用了继承和扩展的设计模式，通过继承游戏引擎提供的 `StageGroup` 基类来实现自定义功能：
- **继承基类**：所有自定义关卡组类都继承自 `stg.game.stage.StageGroup` 类。
- **重写方法**：通过重写基类的方法来实现自定义行为，如 `initStages()` 方法用于初始化关卡组中的关卡。
- **模块化设计**：将不同类型的关卡组分别放在不同的类中，便于管理和扩展。

这种设计使得用户可以方便地创建和管理自定义关卡组，实现不同的游戏流程和难度级别。

## 依赖关系
- 依赖 `stg.game.stage.StageGroup` 类，用于创建自定义关卡组。
- 依赖 `stg.game.ui.GameCanvas` 类，用于关卡组的游戏画布引用。
- 依赖 `user.stage.TestStage` 类，用于关卡组中的测试关卡。

## 使用示例

### 创建自定义关卡组
```java
package user.stageGroup;

import stg.game.stage.StageGroup;
import stg.game.ui.GameCanvas;
import user.stage.MyStage;
import user.stage.MyNextStage;

public class MyStageGroup extends StageGroup {
    
    /**
     * 构造函数
     * @param gameCanvas 游戏画布引用
     */
    public MyStageGroup(GameCanvas gameCanvas) {
        super("我的关卡组", "包含多个关卡的关卡组", StageGroup.Difficulty.NORMAL, gameCanvas);
    }
    
    /**
     * 初始化关卡组
     */
    @Override
    protected void initStages() {
        // 添加关卡
        addStage(new MyStage(1, "关卡1", getGameCanvas()));
        addStage(new MyNextStage(2, "关卡2", getGameCanvas()));
    }
}
```

## 扩展建议
1. **添加更多关卡组类型**：可以创建更多不同类型的关卡组，如不同主题、不同难度的关卡组等。
2. **实现关卡组解锁系统**：可以为关卡组添加解锁系统，通过完成前置关卡组来解锁新的关卡组。
3. **添加关卡组选择界面**：可以为关卡组添加选择界面，允许玩家选择要挑战的关卡组。
4. **实现关卡组评分系统**：可以为关卡组添加评分系统，根据玩家的表现给予评分。
5. **添加关卡组奖励**：可以为关卡组添加奖励系统，完成关卡组后给予玩家奖励。
6. **实现关卡组统计**：可以为关卡组添加统计系统，记录玩家的通关时间、得分等数据。
7. **添加关卡组分支**：可以为关卡组添加分支系统，根据玩家的选择进入不同的关卡组。

## 关键方法

### CustomStageGroup 类
- `CustomStageGroup(GameCanvas gameCanvas)`：构造函数，创建自定义关卡组对象，设置关卡组名称、描述、难度和游戏画布。
- `initStages()`：初始化关卡组，添加测试关卡（重写父类方法）。