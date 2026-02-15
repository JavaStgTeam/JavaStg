# stg.game.bullet 包

## 功能描述
stg.game.bullet 包是 JavaStg 游戏引擎的子弹相关类包，提供了子弹的基本实现和接口定义。子弹是游戏中的重要元素，包括玩家发射的子弹和敌人发射的子弹。

## 包含文件
1. **Bullet.java**：子弹抽象类，继承自 `Obj` 类，实现了子弹的基本功能。
2. **IBullet.java**：子弹接口，定义了子弹的行为和属性。

## 核心功能
1. **子弹基本属性**：位置、速度、大小、颜色、伤害等。
2. **子弹行为**：移动、碰撞检测、越界检查等。
3. **伤害系统**：设置和获取子弹的伤害值。
4. **任务系统**：提供任务开始和结束时的回调方法。

## 设计理念
stg.game.bullet 包采用了抽象类和接口相结合的设计模式，通过 `Bullet` 抽象类提供基本实现，通过 `IBullet` 接口定义统一的行为和属性。这种设计使得子弹系统具有良好的扩展性，可以方便地添加新的子弹类型。

## 依赖关系
- 依赖 `stg.game.obj.Obj` 类，子弹是游戏对象的一种。
- 依赖 `stg.game.IGameObject` 接口，子弹实现了游戏对象的基本接口。

## 使用示例
```java
// 创建玩家子弹
Bullet playerBullet = new Bullet(0, 0, 0, -5, 5, Color.BLUE) {
    @Override
    protected void onTaskStart() {
        // 任务开始时的处理
    }
    
    @Override
    protected void onTaskEnd() {
        // 任务结束时的处理
    }
};
playerBullet.setDamage(10);

// 创建敌人子弹
Bullet enemyBullet = new Bullet(100, 100, 0, 3, 4, Color.RED) {
    @Override
    protected void onTaskStart() {
        // 任务开始时的处理
    }
    
    @Override
    protected void onTaskEnd() {
        // 任务结束时的处理
    }
};
enemyBullet.setDamage(5);

// 添加子弹到游戏世界
gameWorld.addObject(playerBullet);
gameWorld.addObject(enemyBullet);
```

## 扩展建议
1. **添加新的子弹类型**：可以通过继承 `Bullet` 类来创建新的子弹类型，如跟踪子弹、散弹、激光子弹等。
2. **实现子弹特效**：可以在 `Bullet` 类的基础上添加粒子效果、轨迹效果等。
3. **添加子弹池**：为了提高性能，可以实现子弹池，避免频繁创建和销毁子弹对象。
4. **实现子弹模式**：可以创建子弹模式类，定义敌人的弹幕模式。

## 关键方法
1. **Bullet 类**：
   - `Bullet(float x, float y, float vx, float vy, float size, Color color)`：构造函数，创建子弹对象。
   - `getDamage()`：获取子弹伤害值。
   - `setDamage(int damage)`：设置子弹伤害值。
   - `onTaskStart()`：任务开始时触发的方法（抽象方法，需要子类实现）。
   - `onTaskEnd()`：任务结束时触发的方法（抽象方法，需要子类实现）。

2. **IBullet 接口**：
   - `getDamage()`：获取子弹伤害值。
   - `setDamage(int damage)`：设置子弹伤害值。
   - `isOutOfBounds(int width, int height)`：检查子弹是否越界。
   - `getSpeed()`：获取子弹速度。
   - `setSpeed(float speed)`：设置子弹速度。
   - `getDirection()`：获取子弹方向。
   - `setDirection(float direction)`：设置子弹方向。