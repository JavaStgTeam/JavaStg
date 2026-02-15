# user.enemy 包

## 功能描述
user.enemy 包是 JavaStg 游戏引擎的用户自定义敌人包，提供了用户自定义的敌人类。敌人是游戏中的重要元素，用于与玩家战斗，提供游戏的挑战性。

## 包含文件
1. **DefaultEnemy.java**：默认敌人类，继承自 `Enemy` 类，实现了水平移动并在碰到版边时反弹的功能。

## 核心功能
1. **敌人创建**：允许用户创建自定义敌人类，设置敌人的初始位置、速度、大小、颜色、生命值等属性。
2. **移动逻辑**：实现敌人的水平移动，碰到版边时自动反弹。
3. **边界检测**：检测敌人是否碰到版边，防止敌人超出屏幕范围。
4. **生命值管理**：管理敌人的生命值，处理敌人的死亡逻辑。
5. **任务系统**：支持敌人的任务系统，如敌人的开始和结束行为。

## 设计理念
user.enemy 包采用了继承和扩展的设计模式，通过继承游戏引擎提供的 `Enemy` 基类来实现自定义功能：
- **继承基类**：所有自定义敌人类都继承自 `stg.game.enemy.Enemy` 类。
- **重写方法**：通过重写基类的方法来实现自定义行为，如 `update()` 方法用于实现敌人的移动逻辑。
- **模块化设计**：将不同类型的敌人分别放在不同的类中，便于管理和扩展。

这种设计使得用户可以方便地创建和管理自定义敌人，实现不同的敌人行为和攻击模式。

## 依赖关系
- 依赖 `stg.game.enemy.Enemy` 类，用于创建自定义敌人。

## 使用示例

### 创建自定义敌人
```java
package user.enemy;

import java.awt.Color;
import stg.game.enemy.Enemy;

public class MyEnemy extends Enemy {
    private static final float ENEMY_SPEED = 3.0f;
    private static final float ENEMY_SIZE = 15.0f;
    private static final Color ENEMY_COLOR = new Color(0, 0, 255);
    private static final int ENEMY_HP = 100;
    
    /**
     * 构造函数
     * @param x 初始X坐标
     * @param y 初始Y坐标
     */
    public MyEnemy(float x, float y) {
        super(x, y, ENEMY_SPEED, 0, ENEMY_SIZE, ENEMY_COLOR, ENEMY_HP);
    }
    
    /**
     * 更新敌人状态
     * @param canvasWidth 画布宽度
     * @param canvasHeight 画布高度
     */
    @Override
    public void update(int canvasWidth, int canvasHeight) {
        // 自定义移动逻辑
        float x = getX();
        float vx = getVx();
        
        // 计算边界
        float leftBound = -canvasWidth / 2.0f + getSize();
        float rightBound = canvasWidth / 2.0f - getSize();
        
        // 碰到左边界，开始向右移动
        if (x <= leftBound && vx < 0) {
            setVx(ENEMY_SPEED);
            setX(leftBound);
        }
        // 碰到右边界，开始向左移动
        else if (x >= rightBound && vx > 0) {
            setVx(-ENEMY_SPEED);
            setX(rightBound);
        }
        
        // 然后更新位置
        super.update(canvasWidth, canvasHeight);
    }
    
    /**
     * 任务开始时触发的方法
     */
    @Override
    protected void onTaskStart() {
        // 敌人开始时的逻辑
    }
    
    /**
     * 任务结束时触发的方法
     */
    @Override
    protected void onTaskEnd() {
        // 敌人结束时的逻辑
    }
}
```

## 扩展建议
1. **添加更多敌人类型**：可以创建更多不同类型的敌人，如不同移动模式、攻击模式的敌人等。
2. **实现敌人攻击**：可以为敌人添加攻击逻辑，如发射子弹、近战攻击等。
3. **添加敌人AI**：可以为敌人添加AI，如追踪玩家、躲避子弹等。
4. **实现敌人动画**：可以为敌人添加动画效果，如移动动画、攻击动画、死亡动画等。
5. **添加敌人特效**：可以为敌人添加特效，如出场特效、受伤特效、死亡特效等。
6. **实现敌人掉落**：可以为敌人添加掉落逻辑，如死亡时掉落道具、分数等。
7. **添加敌人属性**：可以为敌人添加更多属性，如防御、速度、攻击力等。

## 关键方法

### DefaultEnemy 类
- `DefaultEnemy(float x, float y)`：构造函数，创建默认敌人对象，设置初始位置。
- `DefaultEnemy(float x, float y, float size)`：构造函数，创建默认敌人对象，设置初始位置和大小。
- `DefaultEnemy(float x, float y, float size, Color color)`：构造函数，创建默认敌人对象，设置初始位置、大小和颜色。
- `DefaultEnemy(float x, float y, float size, Color color, int hp)`：构造函数，创建默认敌人对象，设置初始位置、大小、颜色和生命值。
- `update()`：更新敌人状态（无参数版本）。
- `update(int canvasWidth, int canvasHeight)`：更新敌人状态，处理水平移动和边界反弹（重写父类方法）。
- `onTaskStart()`：任务开始时触发的方法（空实现）。
- `onTaskEnd()`：任务结束时触发的方法（空实现）。