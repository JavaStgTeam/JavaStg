# user.player 包

## 功能描述
user.player 包是 JavaStg 游戏引擎的用户自定义玩家包，提供了用户自定义的玩家类。玩家是游戏中的核心元素，由用户控制，与敌人战斗，完成游戏目标。

## 包含文件
1. **DefaultPlayer.java**：默认玩家类，继承自 `Player` 类，实现了发射两个主炮的功能，方向竖直向上。

## 核心功能
1. **玩家创建**：允许用户创建自定义玩家类，设置玩家的初始位置、速度、大小、颜色等属性。
2. **射击系统**：实现玩家的射击功能，发射两个主炮，方向竖直向上。
3. **移动系统**：支持玩家的上下左右移动，支持普通速度和低速模式。
4. **生命值管理**：管理玩家的生命值，处理玩家的死亡和重生逻辑。
5. **无敌系统**：支持玩家的无敌状态，如重生后的无敌时间。
6. **任务系统**：支持玩家的任务系统，如任务开始和结束时的行为。

## 设计理念
user.player 包采用了继承和扩展的设计模式，通过继承游戏引擎提供的 `Player` 基类来实现自定义功能：
- **继承基类**：所有自定义玩家类都继承自 `stg.game.player.Player` 类。
- **重写方法**：通过重写基类的方法来实现自定义行为，如 `shoot()` 方法用于实现玩家的射击逻辑。
- **模块化设计**：将不同类型的玩家分别放在不同的类中，便于管理和扩展。

这种设计使得用户可以方便地创建和管理自定义玩家，实现不同的玩家行为和射击模式。

## 依赖关系
- 依赖 `stg.game.player.Player` 类，用于创建自定义玩家。
- 依赖 `user.bullet.DefaultPlayerMainBullet` 类，用于玩家的射击功能。

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

## 扩展建议
1. **添加更多玩家类型**：可以创建更多不同类型的玩家，如不同角色、不同能力的玩家等。
2. **实现不同的射击模式**：可以重写 `shoot()` 方法来实现不同的射击模式，如散弹、激光、跟踪弹等。
3. **添加玩家技能**：可以为玩家添加特殊技能，如炸弹、无敌护盾、速度提升等。
4. **实现玩家升级系统**：可以添加玩家升级系统，通过收集经验值或道具来提升玩家属性。
5. **添加玩家动画**：可以为玩家添加动画效果，如移动动画、射击动画、受伤动画等。
6. **添加玩家特效**：可以为玩家添加特效，如移动轨迹、射击特效、受伤特效等。
7. **实现玩家状态系统**：可以为玩家添加不同的状态，如加速、减速、无敌等。

## 关键方法

### DefaultPlayer 类
- `DefaultPlayer()`：构造函数，创建默认玩家对象，使用默认位置。
- `DefaultPlayer(float x, float y)`：构造函数，创建默认玩家对象，设置初始位置。
- `shoot()`：发射子弹，实现发射两个主炮，方向竖直向上（重写父类方法）。