# user 包

## 功能描述
user 包是 JavaStg 游戏引擎的用户自定义内容包，提供了用户自定义的游戏内容，包括玩家、敌人、子弹、Boss、符卡、关卡和关卡组等。这个包是游戏引擎的扩展区域，允许用户根据自己的需求创建和定制游戏内容。

## 包含文件
1. **player/**：玩家包，包含自定义玩家类。
   - **DefaultPlayer.java**：默认玩家类，继承自 `Player` 类，实现了发射两个主炮的功能。
2. **enemy/**：敌人包，包含自定义敌人类。
   - **DefaultEnemy.java**：默认敌人类，继承自 `Enemy` 类，提供了基本的敌人行为。
3. **bullet/**：子弹包，包含自定义子弹类。
   - **DefaultPlayerMainBullet.java**：默认玩家主炮子弹类，继承自子弹基类。
   - **SimpleBullet.java**：简单子弹类，提供了基本的子弹行为。
4. **boss/**：Boss包，包含自定义Boss类。
   - **testBoss.java**：测试Boss类，继承自 `Boss` 类，用于测试Boss功能。
5. **spellcard/**：符卡包，包含自定义符卡类。
   - **testSpellcard_1.java**：测试符卡1，继承自 `EnemySpellcard` 类。
   - **testSpellcard_2.java**：测试符卡2，继承自 `EnemySpellcard` 类。
   - **testSpellcard_3.java**：测试符卡3，继承自 `EnemySpellcard` 类。
   - **testSpellcard_4.java**：测试符卡4，继承自 `EnemySpellcard` 类。
   - **testSpellcard_5.java**：测试符卡5，继承自 `EnemySpellcard` 类。
6. **stage/**：关卡包，包含自定义关卡类。
   - **TestStage.java**：测试关卡类，继承自 `Stage` 类，用于测试关卡功能。
7. **stageGroup/**：关卡组包，包含自定义关卡组类。
   - **CustomStageGroup.java**：自定义关卡组类，继承自 `StageGroup` 类，用于管理多个关卡。

## 核心功能
1. **玩家自定义**：允许用户创建自定义玩家类，实现不同的玩家行为和射击模式。
2. **敌人自定义**：允许用户创建自定义敌人类，实现不同的敌人行为和攻击模式。
3. **子弹自定义**：允许用户创建自定义子弹类，实现不同的子弹效果和行为。
4. **Boss自定义**：允许用户创建自定义Boss类，实现复杂的Boss行为和多阶段战斗。
5. **符卡自定义**：允许用户创建自定义符卡类，实现不同的符卡效果和弹幕模式。
6. **关卡自定义**：允许用户创建自定义关卡类，实现不同的关卡设计和敌人波次。
7. **关卡组自定义**：允许用户创建自定义关卡组类，实现多个关卡的组织和管理。

## 设计理念
user 包采用了继承和扩展的设计模式，通过继承游戏引擎提供的基类来实现自定义功能：
- **继承基类**：所有自定义类都继承自游戏引擎提供的基类（如 `Player`、`Enemy`、`Stage` 等）。
- **重写方法**：通过重写基类的方法来实现自定义行为（如 `shoot()`、`update()`、`render()` 等）。
- **模块化设计**：将不同类型的自定义内容分别放在不同的包中，便于管理和扩展。
- **示例代码**：提供了默认实现和测试代码，帮助用户快速上手。

这种设计使得用户可以方便地扩展游戏引擎，创建自己的游戏内容，而不需要修改游戏引擎的核心代码。

## 依赖关系
- 依赖 `stg.game.player.Player` 类，用于创建自定义玩家。
- 依赖 `stg.game.enemy.Enemy` 类，用于创建自定义敌人。
- 依赖 `stg.game.bullet.Bullet` 类，用于创建自定义子弹。
- 依赖 `stg.game.enemy.Boss` 类，用于创建自定义Boss。
- 依赖 `stg.game.enemy.EnemySpellcard` 类，用于创建自定义符卡。
- 依赖 `stg.game.stage.Stage` 类，用于创建自定义关卡。
- 依赖 `stg.game.stage.StageGroup` 类，用于创建自定义关卡组。

## 使用示例

### 创建自定义玩家
```java
package user.player;

import java.awt.Color;
import stg.game.player.Player;

public class MyPlayer extends Player {
    private static final Color BULLET_COLOR = new Color(255, 255, 255);
    private static final float BULLET_SPEED = 48.0f;
    private static final float BULLET_SIZE = 4.0f;
    
    public MyPlayer(float x, float y) {
        super(x, y, 5.0f, 2.0f, 20);
    }
    
    @Override
    protected void shoot() {
        // 实现自定义射击逻辑
        float bulletOffset = 10.0f;
        
        // 左侧子弹
        float leftBulletX = getX() - bulletOffset;
        float leftBulletY = getY() + getSize();
        
        // 右侧子弹
        float rightBulletX = getX() + bulletOffset;
        float rightBulletY = getY() + getSize();
        
        // 获取游戏世界引用
        stg.game.GameWorld gameWorld = getGameWorld();
        if (gameWorld != null) {
            // 创建并添加子弹
            user.bullet.MyBullet leftBullet = new user.bullet.MyBullet(leftBulletX, leftBulletY);
            leftBullet.setDamage(getBulletDamage());
            gameWorld.addPlayerBullet(leftBullet);
            
            user.bullet.MyBullet rightBullet = new user.bullet.MyBullet(rightBulletX, rightBulletY);
            rightBullet.setDamage(getBulletDamage());
            gameWorld.addPlayerBullet(rightBullet);
        }
    }
}
```

### 创建自定义敌人
```java
package user.enemy;

import stg.game.enemy.Enemy;

public class MyEnemy extends Enemy {
    public MyEnemy(float x, float y, int hp, int score, float speed, float size) {
        super(x, y, hp, score, speed, size);
    }
    
    @Override
    protected void onUpdate() {
        // 实现自定义更新逻辑
        super.onUpdate();
        
        // 自定义移动逻辑
        setVy(-1.0f); // 向上移动
        
        // 自定义攻击逻辑
        if (getCurrentFrame() % 60 == 0) {
            shoot();
        }
    }
    
    @Override
    protected void shoot() {
        // 实现自定义射击逻辑
        stg.game.GameWorld gameWorld = getGameWorld();
        if (gameWorld != null) {
            user.bullet.MyBullet bullet = new user.bullet.MyBullet(getX(), getY());
            gameWorld.addEnemyBullet(bullet);
        }
    }
}
```

### 创建自定义Boss
```java
package user.boss;

import stg.game.enemy.Boss;

public class MyBoss extends Boss {
    public MyBoss(float x, float y, int hp, int score, float size) {
        super(x, y, hp, score, size);
    }
    
    @Override
    protected void onUpdate() {
        // 实现自定义更新逻辑
        super.onUpdate();
        
        // 自定义移动逻辑
        setVx(0.5f); // 向右移动
        
        // 自定义攻击逻辑
        if (getCurrentFrame() % 120 == 0) {
            nextSpellcard();
        }
    }
}
```

### 创建自定义符卡
```java
package user.spellcard;

import stg.game.enemy.EnemySpellcard;

public class MySpellcard extends EnemySpellcard {
    public MySpellcard(String name, Enemy owner) {
        super(name, owner);
    }
    
    @Override
    protected void onSpellcardStart() {
        // 符卡开始时的逻辑
        System.out.println("符卡 " + getName() + " 开始");
    }
    
    @Override
    protected void updateSpellcard() {
        // 符卡更新逻辑
        if (getCurrentFrame() % 10 == 0) {
            // 每10帧发射一次子弹
            shoot();
        }
    }
    
    @Override
    protected void onSpellcardEnd() {
        // 符卡结束时的逻辑
        System.out.println("符卡 " + getName() + " 结束");
    }
    
    @Override
    protected void shoot() {
        // 符卡射击逻辑
        stg.game.GameWorld gameWorld = getGameWorld();
        if (gameWorld != null) {
            // 创建圆形弹幕
            for (int i = 0; i < 8; i++) {
                float angle = (float) (Math.PI * 2 * i / 8);
                float vx = (float) (Math.cos(angle) * 3.0f);
                float vy = (float) (Math.sin(angle) * 3.0f);
                user.bullet.MyBullet bullet = new user.bullet.MyBullet(getOwner().getX(), getOwner().getY());
                bullet.setVelocity(vx, vy);
                gameWorld.addEnemyBullet(bullet);
            }
        }
    }
}
```

### 创建自定义关卡
```java
package user.stage;

import stg.game.stage.Stage;
import stg.game.ui.GameCanvas;

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
            user.enemy.MyEnemy enemy = new user.enemy.MyEnemy(0, 0, 100, 2, 3.0f, 10);
            addEnemy(enemy);
        }
        
        if (getCurrentFrame() == 300) {
            // 在第300帧添加Boss
            user.boss.MyBoss boss = new user.boss.MyBoss(0, 0, 1000, 100, 20);
            addEnemy(boss);
        }
    }
    
    @Override
    public Stage nextStage() {
        // 返回下一关
        return new MyNextStage(2, "关卡2", getGameCanvas());
    }
}
```

### 创建自定义关卡组
```java
package user.stageGroup;

import stg.game.stage.StageGroup;
import stg.game.ui.GameCanvas;
import user.stage.MyStage;

public class MyStageGroup extends StageGroup {
    public MyStageGroup(GameCanvas gameCanvas) {
        super("我的关卡组", "包含多个关卡的关卡组", StageGroup.Difficulty.NORMAL, gameCanvas);
    }
    
    @Override
    protected void initStages() {
        // 添加关卡
        addStage(new MyStage(1, "关卡1", getGameCanvas()));
        addStage(new MyNextStage(2, "关卡2", getGameCanvas()));
    }
}
```

## 扩展建议
1. **添加更多玩家类型**：可以创建更多不同类型的玩家，如不同角色、不同能力的玩家等。
2. **添加更多敌人类型**：可以创建更多不同类型的敌人，如不同行为、不同攻击模式的敌人等。
3. **添加更多子弹类型**：可以创建更多不同类型的子弹，如跟踪弹、激光、散弹等。
4. **添加更多Boss类型**：可以创建更多不同类型的Boss，如多阶段Boss、组合Boss等。
5. **添加更多符卡类型**：可以创建更多不同类型的符卡，如复杂弹幕、组合符卡等。
6. **添加更多关卡类型**：可以创建更多不同类型的关卡，如不同场景、不同难度的关卡等。
7. **添加更多关卡组类型**：可以创建更多不同类型的关卡组，如不同主题、不同难度的关卡组等。

## 关键方法

### DefaultPlayer 类
- `DefaultPlayer()`：构造函数，创建默认玩家对象。
- `DefaultPlayer(float x, float y)`：构造函数，创建默认玩家对象，指定初始位置。
- `shoot()`：发射子弹（重写父类方法）。

### DefaultEnemy 类
- `DefaultEnemy(float x, float y, int hp, int score, float speed, float size)`：构造函数，创建默认敌人对象。
- `onUpdate()`：更新敌人状态（可重写）。
- `shoot()`：发射子弹（可重写）。

### DefaultPlayerMainBullet 类
- `DefaultPlayerMainBullet(float x, float y)`：构造函数，创建默认玩家主炮子弹对象。
- `setDamage(int damage)`：设置子弹伤害。
- `getDamage()`：获取子弹伤害。

### SimpleBullet 类
- `SimpleBullet(float x, float y)`：构造函数，创建简单子弹对象。
- `setVelocity(float vx, float vy)`：设置子弹速度。
- `getVx()`：获取X方向速度。
- `getVy()`：获取Y方向速度。

### testBoss 类
- `testBoss(float x, float y, int hp, int score, float size)`：构造函数，创建测试Boss对象。
- `onUpdate()`：更新Boss状态（可重写）。
- `nextSpellcard()`：切换到下一个符卡。

### testSpellcard 类
- `testSpellcard(String name, Enemy owner)`：构造函数，创建测试符卡对象。
- `onSpellcardStart()`：符卡开始时调用（可重写）。
- `updateSpellcard()`：更新符卡状态（可重写）。
- `onSpellcardEnd()`：符卡结束时调用（可重写）。
- `shoot()`：发射子弹（可重写）。

### TestStage 类
- `TestStage(int stageId, String stageName, GameCanvas gameCanvas)`：构造函数，创建测试关卡对象。
- `initStage()`：初始化关卡（可重写）。
- `load()`：加载关卡。
- `updateWaveLogic()`：更新波次逻辑（可重写）。
- `nextStage()`：获取下一关（必须实现）。

### CustomStageGroup 类
- `CustomStageGroup(GameCanvas gameCanvas)`：构造函数，创建自定义关卡组对象。
- `initStages()`：初始化关卡组（可重写）。