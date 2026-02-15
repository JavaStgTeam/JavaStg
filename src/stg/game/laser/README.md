# stg.game.laser 包

## 功能描述
stg.game.laser 包是 JavaStg 游戏引擎的激光系统包，提供了激光的基本实现。激光是游戏中的重要攻击手段，特别是在 Boss 战中，通常具有高伤害和预警效果。

## 包含文件
1. **Laser.java**：激光基类，实现了激光的基本功能，如预警、渲染、碰撞检测等。

## 核心功能
1. **激光基本属性**：位置、角度、长度、宽度、颜色、伤害值等。
2. **预警系统**：激光发射前显示预警线，给玩家反应时间。
3. **渲染系统**：渲染激光的预警线和实际激光效果，包括核心和高光。
4. **碰撞检测**：检测玩家或其他游戏对象是否与激光碰撞。
5. **边界检测**：检查激光是否超出游戏边界。
6. **任务系统**：提供任务开始和结束时的回调方法。

## 设计理念
stg.game.laser 包采用了抽象类设计，通过 `Laser` 抽象类提供基本实现，子类需要实现特定的行为逻辑。这种设计使得激光系统具有良好的扩展性，可以方便地添加新的激光类型和行为。

## 依赖关系
- 依赖 `stg.game.ui.GameCanvas` 类，用于坐标转换。
- 依赖 `stg.util.RenderUtils` 类，用于渲染优化。

## 使用示例

### 创建激光
```java
// 创建激光
Laser laser = new Laser(0, 0, 0, 500, 5, Color.RED) {
    @Override
    protected void initBehavior() {
        // 初始化激光行为
    }
    
    @Override
    protected void onUpdate() {
        // 自定义更新逻辑
    }
    
    @Override
    protected void onMove() {
        // 自定义移动逻辑
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

// 设置游戏画布引用
laser.setGameCanvas(gameCanvas);

// 添加激光到游戏中
gameCanvas.addLaser(laser);
```

### 激光更新和渲染
```java
// 在游戏循环中更新激光
laser.update();

// 渲染激光
laser.render(graphics);

// 检查激光与玩家的碰撞
if (laser.checkCollision(player.getX(), player.getY())) {
    player.takeDamage(laser.getDamage());
}
```

## 扩展建议
1. **添加新的激光类型**：可以通过继承 `Laser` 类来创建新的激光类型，如旋转激光、扫描激光、脉冲激光等。
2. **实现激光动画效果**：可以在 `Laser` 类的基础上添加动画效果，如激光宽度变化、颜色渐变、闪烁效果等。
3. **扩展激光行为**：可以实现更复杂的激光行为，如跟踪玩家、多段激光、激光网等。
4. **添加激光音效**：可以为激光添加发射、预警、命中时的音效，增强游戏体验。
5. **实现激光粒子效果**：可以在激光周围添加粒子效果，如火花、烟雾等，增强视觉效果。

## 关键方法

### Laser 类
- `Laser(float x, float y, float angle, float length, float width, Color color)`：构造函数，创建激光对象。
- `Laser(float x, float y, float angle, float length, float width, Color color, int warningTime, int damage)`：完整构造函数，创建激光对象。
- `initBehavior()`：初始化行为参数（抽象方法，需要子类实现）。
- `onUpdate()`：实现每帧的自定义更新逻辑（抽象方法，需要子类实现）。
- `onMove()`：实现自定义移动逻辑（抽象方法，需要子类实现）。
- `update()`：更新激光状态。
- `render(Graphics g)`：渲染激光。
- `checkCollision(float px, float py)`：检查点是否在激光碰撞体内。
- `isOutOfBounds(int width, int height)`：检查激光是否超出边界。
- `reset()`：重置激光状态。
- `setGameCanvas(GameCanvas gameCanvas)`：设置画布引用。
- `onTaskStart()`：任务开始时触发的方法（抽象方法，需要子类实现）。
- `onTaskEnd()`：任务结束时触发的方法（抽象方法，需要子类实现）。

### 主要属性访问方法
- `getX()`, `setX(float x)`：获取/设置激光起点X坐标。
- `getY()`, `setY(float y)`：获取/设置激光起点Y坐标。
- `getAngle()`, `setAngle(float angle)`：获取/设置激光角度。
- `getLength()`, `setLength(float length)`：获取/设置激光长度。
- `getWidth()`, `setWidth(float width)`：获取/设置激光宽度。
- `getColor()`, `setColor(Color color)`：获取/设置激光颜色。
- `getDamage()`, `setDamage(int damage)`：获取/设置激光伤害值。
- `isActive()`：检查激光是否激活（预警结束后）。
- `isVisible()`, `setVisible(boolean visible)`：获取/设置激光是否可见。