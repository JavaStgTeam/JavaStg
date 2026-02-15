# stg.game.obj 包

## 功能描述
stg.game.obj 包是 JavaStg 游戏引擎的游戏对象基础包，提供了所有游戏对象的基类 `Obj`。这个类是游戏中所有实体的基础，包括玩家、敌人、子弹、道具等，提供了统一的属性和行为接口。

## 包含文件
1. **Obj.java**：游戏物体基类，实现了游戏对象的基本功能，如位置管理、速度控制、渲染、边界检测等。

## 核心功能
1. **基本属性管理**：位置、速度、大小、颜色、碰撞判定半径等。
2. **坐标系统**：提供游戏世界坐标到屏幕坐标的转换功能。
3. **更新系统**：实现游戏对象的更新逻辑，包括帧计数、位置更新等。
4. **渲染系统**：提供基本的渲染功能，将游戏对象绘制到屏幕上。
5. **边界检测**：检查游戏对象是否超出游戏边界。
6. **状态管理**：管理游戏对象的激活状态，提供重置功能。

## 设计理念
stg.game.obj 包采用了基类设计，通过 `Obj` 类为所有游戏对象提供统一的基础实现。这种设计使得游戏对象系统具有良好的一致性和可扩展性，子类可以通过重写方法来实现特定的行为。

## 依赖关系
- 依赖 `stg.util.CoordinateSystem` 类，用于坐标转换。

## 使用示例

### 创建自定义游戏对象
```java
// 创建自定义游戏对象
class CustomObject extends Obj {
    public CustomObject(float x, float y) {
        super(x, y, 0, 0, 20, Color.GREEN);
    }
    
    @Override
    protected void onUpdate() {
        // 自定义更新逻辑
        if (frame % 60 == 0) {
            // 每60帧执行一次
            System.out.println("CustomObject updated");
        }
    }
    
    @Override
    protected void onMove() {
        // 自定义移动逻辑
        setVx((float)Math.sin(frame * 0.01f) * 2);
        setVy((float)Math.cos(frame * 0.01f) * 2);
    }
    
    @Override
    public void render(Graphics2D g) {
        if (!isActive()) return;
        
        // 自定义渲染逻辑
        float[] screenCoords = toScreenCoords(getX(), getY());
        float screenX = screenCoords[0];
        float screenY = screenCoords[1];
        
        g.setColor(getColor());
        g.fillRect((int)(screenX - getSize()/2), (int)(screenY - getSize()/2), (int)getSize(), (int)getSize());
    }
}

// 使用自定义游戏对象
CustomObject obj = new CustomObject(0, 0);

// 在游戏循环中更新和渲染
while (gameRunning) {
    obj.update();
    obj.render(graphics);
}
```

### 坐标系统设置
```java
// 创建并设置坐标系统
CoordinateSystem coordinateSystem = new CoordinateSystem(800, 600);
Obj.setSharedCoordinateSystem(coordinateSystem);

// 现在所有游戏对象都会使用这个坐标系统进行坐标转换
```

## 扩展建议
1. **添加新的游戏对象类型**：可以通过继承 `Obj` 类来创建新的游戏对象类型，如特殊敌人、道具、环境对象等。
2. **扩展渲染功能**：可以在 `Obj` 类的基础上添加更复杂的渲染功能，如动画、粒子效果、阴影等。
3. **实现物理系统**：可以扩展 `Obj` 类，添加物理属性和行为，如重力、碰撞响应、摩擦力等。
4. **添加状态系统**：可以为游戏对象添加状态系统，实现更复杂的行为逻辑，如敌人的不同状态、道具的不同效果等。
5. **实现对象池**：为了提高性能，可以实现对象池，重用频繁创建和销毁的游戏对象，如子弹、粒子等。

## 关键方法

### Obj 类
- `Obj(float x, float y, float vx, float vy, float size, Color color)`：构造函数，创建游戏对象。
- `initBehavior()`：初始化行为参数（可重写）。
- `onUpdate()`：实现每帧的自定义更新逻辑（可重写）。
- `onMove()`：实现自定义移动逻辑（可重写）。
- `update()`：更新物体状态。
- `render(Graphics2D g)`：渲染物体（可重写）。
- `isOutOfBounds(int width, int height)`：检查物体是否超出边界。
- `moveTo(float x, float y)`：移动到指定坐标。
- `reset()`：重置物体状态。
- `toScreenCoords(float worldX, float worldY)`：将游戏坐标转换为屏幕坐标。

### 静态方法
- `setSharedCoordinateSystem(CoordinateSystem coordinateSystem)`：设置共享的坐标系统。
- `getSharedCoordinateSystem()`：获取共享的坐标系统。

### 属性访问方法
- `getX()`, `setX(float x)`：获取/设置X坐标。
- `getY()`, `setY(float y)`：获取/设置Y坐标。
- `getVx()`, `setVx(float vx)`：获取/设置X方向速度。
- `getVy()`, `setVy(float vy)`：获取/设置Y方向速度。
- `getSize()`：获取物体大小。
- `getColor()`：获取物体颜色。
- `isActive()`, `setActive(boolean active)`：获取/设置物体激活状态。
- `getHitboxRadius()`, `setHitboxRadius(float hitboxRadius)`：获取/设置碰撞判定半径。