# stg.game.item 包

## 功能描述
stg.game.item 包是 JavaStg 游戏引擎的物品系统包，提供了物品的基本实现和接口定义。物品是游戏中的重要元素，包括道具、掉落物、特殊物品等，玩家可以通过收集物品获得各种效果。

## 包含文件
1. **Item.java**：物品基类，继承自 `Obj` 类，实现了物品的基本功能，如渲染、更新、被收集等。
2. **IItem.java**：物品接口，定义了物品的行为和属性。

## 核心功能
1. **物品基本属性**：位置、速度、大小、颜色等。
2. **物品行为**：移动、渲染、被收集、吸引效果等。
3. **边界检测**：检查物品是否超出游戏边界。
4. **任务系统**：提供任务开始和结束时的回调方法。
5. **吸引系统**：实现物品对玩家的吸引效果。

## 设计理念
stg.game.item 包采用了抽象类和接口相结合的设计模式，通过 `Item` 抽象类提供基本实现，通过 `IItem` 接口定义统一的行为和属性。这种设计使得物品系统具有良好的扩展性，可以方便地添加新的物品类型。

## 依赖关系
- 依赖 `stg.game.obj.Obj` 类，物品是游戏对象的一种。
- 依赖 `stg.game.IGameObject` 接口，物品实现了游戏对象的基本接口。

## 使用示例

### 创建物品
```java
// 创建物品
Item item = new Item(100, 100, 10, Color.YELLOW) {
    @Override
    protected void initBehavior() {
        // 初始化物品行为
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
    public void onCollect() {
        super.onCollect();
        // 物品被收集时的处理，如增加分数、回复生命值等
    }
};

// 添加物品到游戏世界
gameWorld.addObject(item);
```

### 物品吸引效果
```java
// 设置物品的吸引参数
item.setAttractionParams(200.0f, 4.0f); // 吸引距离200，吸引速度4

// 在游戏循环中应用吸引效果
item.applyAttraction();
```

## 扩展建议
1. **添加新的物品类型**：可以通过继承 `Item` 类来创建新的物品类型，如分数道具、生命道具、炸弹道具等。
2. **实现特殊物品效果**：可以在 `Item` 类的基础上添加特殊效果，如临时加速、无敌状态、全屏清除敌人等。
3. **扩展物品行为**：可以实现更复杂的物品行为，如物品移动轨迹、物品动画效果等。
4. **添加物品掉落系统**：可以实现敌人死亡时的物品掉落逻辑，如根据敌人类型掉落不同物品。
5. **实现物品组合效果**：可以实现多个物品组合使用时的特殊效果，增加游戏的策略性。

## 关键方法

### 1. Item 类
- `Item(float x, float y, float size, Color color)`：构造函数，创建物品对象。
- `Item(float x, float y, float vx, float vy, float size, Color color)`：构造函数，创建物品对象（带速度参数）。
- `initBehavior()`：初始化行为参数（抽象方法，需要子类实现）。
- `onUpdate()`：实现每帧的自定义更新逻辑（抽象方法，需要子类实现）。
- `onMove()`：实现自定义移动逻辑（抽象方法，需要子类实现）。
- `update()`：更新物品状态。
- `render(Graphics2D g)`：渲染物品。
- `onCollect()`：物品被玩家拾取时的处理（可重写）。
- `applyAttraction()`：应用道具吸引逻辑。
- `setAttractionParams(float distance, float speed)`：设置吸引参数。
- `onTaskStart()`：任务开始时触发的方法（可重写）。
- `onTaskEnd()`：任务结束时触发的方法（可重写）。

### 2. IItem 接口
- `onCollect()`：物品被收集。
- `applyAttraction()`：应用吸引力效果。
- `isOutOfBounds(int width, int height)`：检查物品是否越界。
- `getType()`：获取物品类型。