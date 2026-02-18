# entity/base 包说明

## 功能概述

**entity/base 包**是游戏实体的基础包，包含了所有游戏实体的基类 `Obj`。这个类定义了游戏实体的基本属性和方法，是整个实体继承体系的基础。

## 包含的类

| 类名 | 功能描述 |
|------|----------|
| Obj | 游戏实体基类，定义了实体的基本属性和方法 |

## Obj 类的核心功能

**Obj 类**作为所有游戏实体的基类，提供了以下核心功能：

### 1. 位置和运动管理
- **位置属性**：x, y 坐标
- **速度属性**：vx, vy 速度向量
- **运动方法**：updatePosition() 方法更新位置

### 2. 碰撞检测
- **碰撞盒**：定义实体的碰撞范围
- **碰撞方法**：collidesWith() 方法检测碰撞
- **碰撞响应**：onCollision() 方法处理碰撞事件

### 3. 生命周期管理
- **激活状态**：isActive 属性控制实体是否活跃
- **删除标记**：isToBeRemoved 属性标记实体是否需要被删除
- **生命周期方法**：init()、destroy() 等方法管理生命周期

### 4. 渲染系统
- **渲染方法**：render() 方法负责实体的绘制
- **可见性**：isVisible 属性控制实体是否可见

### 5. 基本更新逻辑
- **更新方法**：update() 方法处理实体的更新逻辑
- **时间增量**：支持基于时间的更新，确保不同帧率下行为一致

## 类结构

```java
public class Obj implements IGameObject {
    // 位置和运动属性
    protected double x, y;
    protected double vx, vy;
    
    // 碰撞属性
    protected double width, height;
    
    // 生命周期属性
    protected boolean isActive;
    protected boolean isToBeRemoved;
    protected boolean isVisible;
    
    // 方法
    public void update(double deltaTime) { ... }
    public void render(Graphics2D g) { ... }
    public boolean collidesWith(Obj other) { ... }
    public void onCollision(Obj other) { ... }
    // 其他方法...
}
```

## 使用示例

### 创建自定义实体

```java
// 创建一个简单的自定义实体
public class MyEntity extends Obj {
    public MyEntity(double x, double y) {
        super();
        this.x = x;
        this.y = y;
        this.width = 32;
        this.height = 32;
        this.vx = 1.0;
        this.vy = 0.0;
    }
    
    @Override
    public void update(double deltaTime) {
        // 调用父类的更新方法
        super.update(deltaTime);
        
        // 自定义更新逻辑
        // 例如：边界检测
        if (x < 0 || x > 800) {
            vx = -vx; // 反弹
        }
    }
    
    @Override
    public void render(Graphics2D g) {
        // 自定义渲染逻辑
        g.setColor(Color.RED);
        g.fillRect((int)x, (int)y, (int)width, (int)height);
    }
}
```

### 使用实体

```java
// 创建实体
MyEntity entity = new MyEntity(100, 100);

// 添加到游戏世界
gameWorld.addObject(entity);

// 手动更新实体
entity.update(deltaTime);

// 手动渲染实体
entity.render(graphics);

// 检测碰撞
if (entity.collidesWith(player)) {
    System.out.println("碰撞发生！");
}

// 标记实体为删除
entity.setToBeRemoved(true);
```

## 设计说明

1. **基类设计**：通过抽象出所有实体的共同属性和方法，减少代码重复
2. **接口实现**：实现 IGameObject 接口，确保所有实体都符合游戏对象的标准
3. **可扩展性**：提供 protected 访问修饰符，便于子类重写和扩展
4. **性能考虑**：属性直接访问，方法简洁高效

## 开发建议

- 当需要创建新的游戏实体时，继承 Obj 类
- 当需要修改实体的基础行为时，修改 Obj 类
- 当需要为特定类型的实体添加共同功能时，考虑在 Obj 和具体实体之间添加中间基类
- 保持 Obj 类的简洁性，只包含所有实体都需要的基本功能
- 为重要的方法添加详细的注释，说明其用途和实现细节