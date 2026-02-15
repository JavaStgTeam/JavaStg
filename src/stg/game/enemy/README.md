# stg.game.enemy 包

## 功能描述
stg.game.enemy 包是 JavaStg 游戏引擎的敌人相关类包，提供了敌人的基本实现、Boss 类和符卡系统。敌人是游戏中的重要元素，包括普通敌人和 Boss 敌人，Boss 敌人还具有符卡系统，用于实现复杂的攻击模式。

## 包含文件
1. **Enemy.java**：敌人基类，继承自 `Obj` 类，实现了敌人的基本功能，如移动、渲染、受伤害等。
2. **Boss.java**：Boss 类，继承自 `Enemy` 类，管理 Boss 的入场、退场和符卡系统。
3. **EnemySpellcard.java**：敌方符卡基类，用于定义 Boss 的攻击模式和阶段。
4. **IEnemy.java**：敌人接口，定义了敌人的行为和属性。

## 核心功能
1. **敌人基本属性**：位置、速度、大小、颜色、生命值等。
2. **敌人行为**：移动、渲染、受伤害、死亡等。
3. **Boss 系统**：Boss 的入场、退场动画，多阶段战斗。
4. **符卡系统**：定义 Boss 的攻击模式和阶段，包括符卡名称、生命值、持续时间等。
5. **伤害系统**：敌人受到伤害的处理逻辑，包括生命值减少、死亡判定等。
6. **任务系统**：提供任务开始和结束时的回调方法。

## 设计理念
stg.game.enemy 包采用了继承和组合相结合的设计模式，通过 `Enemy` 基类提供基本实现，`Boss` 类扩展了特殊功能，`EnemySpellcard` 类实现了符卡系统。这种设计使得敌人系统具有良好的扩展性，可以方便地添加新的敌人类型和 Boss 战。

## 依赖关系
- 依赖 `stg.game.obj.Obj` 类，敌人是游戏对象的一种。
- 依赖 `stg.game.IGameObject` 接口，敌人实现了游戏对象的基本接口。

## 使用示例

### 创建普通敌人
```java
// 创建普通敌人
Enemy enemy = new Enemy(100, 100) {
    @Override
    protected void onTaskStart() {
        // 任务开始时的处理
    }
    
    @Override
    protected void onTaskEnd() {
        // 任务结束时的处理
    }
};
enemy.setSpeed(2.0f);
enemy.setHp(50);

// 添加敌人到游戏世界
gameWorld.addObject(enemy);
```

### 创建 Boss 和符卡
```java
// 创建 Boss
Boss boss = new Boss(0, -100, 40, Color.RED) {
    @Override
    protected void initSpellcards() {
        // 添加符卡
        addSpellcard(new EnemySpellcard("弹幕之星", 1, this, 1000) {
            @Override
            protected void onStart() {
                // 符卡开始时的处理
            }
            
            @Override
            protected void onEnd() {
                // 符卡结束时的处理
            }
            
            @Override
            protected void updateLogic() {
                // 符卡逻辑更新
                // 实现弹幕模式
            }
        });
        
        addSpellcard(new EnemySpellcard("激光矩阵", 2, this, 1500) {
            @Override
            protected void onStart() {
                // 符卡开始时的处理
            }
            
            @Override
            protected void onEnd() {
                // 符卡结束时的处理
            }
            
            @Override
            protected void updateLogic() {
                // 符卡逻辑更新
                // 实现激光攻击
            }
        });
    }
    
    @Override
    protected void onTaskStart() {
        // 任务开始时的处理
    }
    
    @Override
    protected void onTaskEnd() {
        // 任务结束时的处理
    }
};

// 添加 Boss 到游戏世界
gameWorld.addObject(boss);
```

## 扩展建议
1. **添加新的敌人类型**：可以通过继承 `Enemy` 类来创建新的敌人类型，如飞行敌人、地面敌人、自爆敌人等。
2. **实现特殊敌人行为**：可以在 `Enemy` 类的基础上添加特殊行为，如追踪玩家、发射子弹、释放技能等。
3. **扩展 Boss 系统**：可以通过继承 `Boss` 类来创建特殊的 Boss 类型，如多形态 Boss、阶段性 Boss 等。
4. **创建更多符卡模式**：可以通过继承 `EnemySpellcard` 类来创建各种符卡模式，如环形弹幕、螺旋弹幕、激光网等。
5. **添加敌人 AI**：可以实现更复杂的敌人 AI，如根据玩家位置调整攻击策略、躲避玩家子弹等。

## 关键方法

### 1. Enemy 类
- `Enemy(float x, float y, float vx, float vy, float size, Color color, int hp)`：构造函数，创建敌人对象。
- `update(int canvasWidth, int canvasHeight)`：更新敌人状态。
- `render(Graphics2D g)`：渲染敌人。
- `takeDamage(int damage)`：处理敌人受到的伤害。
- `onDeath()`：敌人死亡时触发的方法（可重写）。
- `isOutOfBounds(int canvasWidth, int canvasHeight)`：检查敌人是否越界。
- `isAlive()`：检查敌人是否存活。
- `getHp()`：获取敌人当前生命值。
- `setHp(int hp)`：设置敌人生命值。
- `getMaxHp()`：获取敌人最大生命值。
- `reset()`：重置敌人状态。
- `onTaskStart()`：任务开始时触发的方法（抽象方法，需要子类实现）。
- `onTaskEnd()`：任务结束时触发的方法（抽象方法，需要子类实现）。

### 2. Boss 类
- `Boss(float x, float y, float size, Color color)`：构造函数，创建 Boss 对象。
- `initSpellcards()`：初始化符卡（抽象方法，需要子类实现）。
- `update(int canvasWidth, int canvasHeight)`：更新 Boss 状态。
- `startNextSpellcard()`：开始下一个符卡。
- `startExit()`：开始退场。
- `addSpellcard(EnemySpellcard spellcard)`：添加符卡。
- `getCurrentSpellcard()`：获取当前符卡。
- `getCurrentPhase()`：获取当前阶段。
- `getMaxPhase()`：获取最大阶段。
- `isEntering()`：检查是否正在入场。
- `isExiting()`：检查是否正在退场。

### 3. EnemySpellcard 类
- `EnemySpellcard(String name, int phase, Boss boss, int hp)`：构造函数，创建符卡对象。
- `start()`：开始符卡。
- `end()`：结束符卡。
- `update()`：更新符卡逻辑。
- `onStart()`：符卡开始时调用的方法（抽象方法，需要子类实现）。
- `onEnd()`：符卡结束时调用的方法（抽象方法，需要子类实现）。
- `updateLogic()`：更新符卡逻辑（抽象方法，需要子类实现）。
- `isSpellcardPhase()`：检查是否为符卡阶段。
- `takeDamage(int damage)`：处理符卡受到的伤害。
- `isDefeated()`：检查符卡是否被击败。

### 4. IEnemy 接口
- `takeDamage(int damage)`：承受伤害。
- `isAlive()`：检查敌人是否存活。
- `getHp()`：获取当前生命值。
- `getMaxHp()`：获取最大生命值。
- `setHp(int hp)`：设置生命值。
- `isOutOfBounds(int width, int height)`：检查敌人是否越界。
- `getType()`：获取敌人类型。
- `setSpeed(float speed)`：设置敌人速度。
- `getSpeed()`：获取敌人速度。