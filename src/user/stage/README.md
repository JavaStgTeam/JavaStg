# user.stage 包

## 功能描述
user.stage 包是 JavaStg 游戏引擎的用户自定义关卡包，提供了用户自定义的关卡类。关卡是游戏中的重要元素，用于组织游戏的流程，包含敌人的生成、Boss的出现等内容。

## 包含文件
1. **TestStage.java**：测试关卡类，继承自 `Stage` 类，用于测试关卡功能，包含敌人的生成逻辑和Boss的出现逻辑。

## 核心功能
1. **关卡创建**：允许用户创建自定义关卡类，设置关卡的ID、名称、游戏画布等属性。
2. **波次管理**：实现关卡的波次系统，控制敌人的生成频率和数量。
3. **敌人生成**：生成普通敌人和Boss，设置敌人的初始位置、属性等。
4. **关卡逻辑**：实现关卡的开始、结束和更新逻辑，控制关卡的流程。
5. **资源管理**：管理关卡的资源加载和释放。

## 设计理念
user.stage 包采用了继承和扩展的设计模式，通过继承游戏引擎提供的 `Stage` 基类来实现自定义功能：
- **继承基类**：所有自定义关卡类都继承自 `stg.game.stage.Stage` 类。
- **重写方法**：通过重写基类的方法来实现自定义行为，如 `initStage()`、`onStageStart()`、`onStageEnd()`、`updateWaveLogic()`、`nextStage()` 和 `load()` 方法。
- **模块化设计**：将不同类型的关卡分别放在不同的类中，便于管理和扩展。

这种设计使得用户可以方便地创建和管理自定义关卡，实现不同的关卡设计和敌人波次。

## 依赖关系
- 依赖 `stg.game.stage.Stage` 类，用于创建自定义关卡。
- 依赖 `stg.game.ui.GameCanvas` 类，用于关卡的游戏画布引用。
- 依赖 `user.enemy.DefaultEnemy` 类，用于生成普通敌人。
- 依赖 `user.boss.testBoss` 类，用于生成Boss。

## 使用示例

### 创建自定义关卡
```java
package user.stage;

import stg.game.stage.Stage;
import stg.game.ui.GameCanvas;
import user.enemy.MyEnemy;
import user.boss.MyBoss;

public class MyStage extends Stage {
    private int enemyCount = 0;
    private static final int MAX_ENEMIES = 15;
    private boolean hasSpawnedBoss = false;
    
    public MyStage(int stageId, String stageName, GameCanvas gameCanvas) {
        super(stageId, stageName, gameCanvas);
    }
    
    @Override
    protected void initStage() {
        // 初始化关卡资源
    }
    
    @Override
    protected void onStageStart() {
        // 关卡开始逻辑
        enemyCount = 0;
        hasSpawnedBoss = false;
    }
    
    @Override
    protected void onStageEnd() {
        // 关卡结束逻辑
    }
    
    @Override
    protected void updateWaveLogic() {
        // 每45帧生成一个敌人，最多生成15个
        if (getCurrentFrame() % 45 == 0 && enemyCount < MAX_ENEMIES) {
            // 生成一个敌人，位置在随机X坐标，Y坐标为100
            float randomX = (float) (Math.random() * 400 - 200); // -200到200之间的随机值
            MyEnemy enemy = new MyEnemy(randomX, 100);
            addEnemy(enemy);
            System.out.println("生成敌人，位置: (" + randomX + ", 100)，总数: " + (enemyCount + 1));
            enemyCount++;
        }
        
        // 关卡开始10秒时生成Boss（假设60帧/秒）
        if (getCurrentFrame() == 600 && !hasSpawnedBoss) {
            // 生成Boss，位置在屏幕中央
            MyBoss boss = new MyBoss(0, 100);
            addEnemy(boss);
            System.out.println("生成Boss，位置: (0, 100)");
            hasSpawnedBoss = true;
        }
    }
    
    @Override
    public Stage nextStage() {
        // 返回下一关，这里返回null表示没有下一关
        return null;
    }
    
    @Override
    public void load() {
        // 加载关卡资源
        setLoaded();
    }
}
```

## 扩展建议
1. **添加更多关卡类型**：可以创建更多不同类型的关卡，如不同场景、不同难度的关卡等。
2. **实现关卡背景**：可以为关卡添加背景，如不同的背景图片、背景动画等。
3. **添加关卡音效**：可以为关卡添加音效，如背景音乐、敌人出现音效等。
4. **实现关卡特效**：可以为关卡添加特效，如天气效果、光影效果等。
5. **添加关卡事件**：可以为关卡添加事件，如对话、剧情动画等。
6. **实现关卡分支**：可以为关卡添加分支路径，根据玩家的选择进入不同的关卡。
7. **添加关卡难度系统**：可以为关卡添加难度系统，根据难度调整敌人的数量、属性等。

## 关键方法

### TestStage 类
- `TestStage(int stageId, String stageName, GameCanvas gameCanvas)`：构造函数，创建测试关卡对象，设置关卡ID、名称和游戏画布。
- `initStage()`：初始化关卡（空实现）。
- `onStageStart()`：关卡开始时触发的方法，重置敌人计数器和Boss生成状态。
- `onStageEnd()`：关卡结束时触发的方法（空实现）。
- `updateWaveLogic()`：更新波次逻辑，控制敌人的生成和Boss的出现。
- `nextStage()`：获取下一关，返回null表示没有下一关。
- `load()`：加载关卡资源，设置关卡为已加载状态。