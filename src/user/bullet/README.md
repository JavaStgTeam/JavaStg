# user.bullet 包

## 功能描述
user.bullet 包是 JavaStg 游戏引擎的用户自定义子弹包，提供了用户自定义的子弹类。子弹是游戏中的重要元素，用于玩家和敌人的攻击。

## 包含文件
1. **DefaultPlayerMainBullet.java**：默认玩家主子弹类，继承自 `Bullet` 类，实现了沿y方向竖直向上的子弹，弹速为75像素/tick（原速度的五倍）。
2. **SimpleBullet.java**：简单子弹类，继承自 `Bullet` 类，提供了空的task实现，用于不需要特殊task行为的子弹。

## 核心功能
1. **子弹创建**：允许用户创建自定义子弹类，设置子弹的初始位置、速度、大小、颜色等属性。
2. **子弹行为**：实现子弹的基本行为，如移动、碰撞检测等。
3. **子弹类型**：支持不同类型的子弹，如玩家子弹、敌人子弹等。
4. **任务系统**：支持子弹的任务系统，如子弹的开始和结束行为。

## 设计理念
user.bullet 包采用了继承和扩展的设计模式，通过继承游戏引擎提供的 `Bullet` 基类来实现自定义功能：
- **继承基类**：所有自定义子弹类都继承自 `stg.game.bullet.Bullet` 类。
- **重写方法**：通过重写基类的方法来实现自定义行为，如 `onTaskStart()` 和 `onTaskEnd()` 方法。
- **模块化设计**：将不同类型的子弹分别放在不同的类中，便于管理和扩展。

这种设计使得用户可以方便地创建和管理自定义子弹，实现不同的攻击效果。

## 依赖关系
- 依赖 `stg.game.bullet.Bullet` 类，用于创建自定义子弹。

## 使用示例

### 创建自定义玩家子弹
```java
package user.bullet;

import java.awt.Color;
import stg.game.bullet.Bullet;

public class MyPlayerBullet extends Bullet {
    private static final float BULLET_SPEED = 50.0f;
    private static final float BULLET_SIZE = 4.0f;
    private static final Color BULLET_COLOR = new Color(255, 255, 255);
    
    /**
     * 构造函数
     * @param x 初始X坐标
     * @param y 初始Y坐标
     */
    public MyPlayerBullet(float x, float y) {
        super(x, y, 0, BULLET_SPEED, BULLET_SIZE, BULLET_COLOR);
    }
    
    /**
     * 任务开始时触发的方法
     */
    @Override
    protected void onTaskStart() {
        // 子弹开始时的逻辑
    }
    
    /**
     * 任务结束时触发的方法
     */
    @Override
    protected void onTaskEnd() {
        // 子弹结束时的逻辑
    }
}
```

### 创建自定义敌人子弹
```java
package user.bullet;

import java.awt.Color;
import stg.game.bullet.Bullet;

public class MyEnemyBullet extends Bullet {
    private static final float BULLET_SPEED = 30.0f;
    private static final float BULLET_SIZE = 3.0f;
    private static final Color BULLET_COLOR = new Color(255, 0, 0);
    
    /**
     * 构造函数
     * @param x 初始X坐标
     * @param y 初始Y坐标
     * @param angle 子弹角度（弧度）
     */
    public MyEnemyBullet(float x, float y, float angle) {
        float vx = (float) Math.cos(angle) * BULLET_SPEED;
        float vy = (float) Math.sin(angle) * BULLET_SPEED;
        super(x, y, vx, vy, BULLET_SIZE, BULLET_COLOR);
    }
    
    /**
     * 任务开始时触发的方法
     */
    @Override
    protected void onTaskStart() {
        // 子弹开始时的逻辑
    }
    
    /**
     * 任务结束时触发的方法
     */
    @Override
    protected void onTaskEnd() {
        // 子弹结束时的逻辑
    }
}
```

## 扩展建议
1. **添加更多子弹类型**：可以创建更多不同类型的子弹，如跟踪弹、激光、散弹等。
2. **实现子弹特效**：可以为子弹添加特效，如子弹轨迹、爆炸效果等。
3. **添加子弹属性**：可以为子弹添加更多属性，如伤害、穿透、反弹等。
4. **实现子弹AI**：可以为敌人子弹添加AI，如跟踪玩家、躲避障碍物等。
5. **添加子弹动画**：可以为子弹添加动画效果，如旋转、缩放等。

## 关键方法

### DefaultPlayerMainBullet 类
- `DefaultPlayerMainBullet(float x, float y)`：构造函数，创建默认玩家主子弹对象，设置初始位置。
- `DefaultPlayerMainBullet(float x, float y, float size)`：构造函数，创建默认玩家主子弹对象，设置初始位置和大小。
- `DefaultPlayerMainBullet(float x, float y, float size, Color color)`：构造函数，创建默认玩家主子弹对象，设置初始位置、大小和颜色。
- `onTaskStart()`：任务开始时触发的方法（空实现）。
- `onTaskEnd()`：任务结束时触发的方法（空实现）。

### SimpleBullet 类
- `SimpleBullet(float x, float y, float vx, float vy, float size, Color color)`：构造函数，创建简单子弹对象，设置初始位置、速度、大小和颜色。
- `onTaskStart()`：任务开始时触发的方法（空实现）。
- `onTaskEnd()`：任务结束时触发的方法（空实现）。