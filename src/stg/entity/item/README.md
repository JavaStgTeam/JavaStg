# entity/item 包说明

## 功能概述

**entity/item 包**是游戏的物品实体包，包含了物品相关的类和接口。物品是游戏中玩家可以收集的实体，通常用于恢复生命值、增加分数或提供其他增益效果。

## 包含的类

| 类名 | 功能描述 |
|------|----------|
| Item | 物品类，实现了物品的基本功能 |
| IItem | 物品接口，定义了物品的基本方法 |

## 主要功能

### Item 类
- **基础属性**：继承自 Obj 类，具有位置、速度等属性
- **类型系统**：可以设置物品的类型（如生命恢复、分数、炸弹等）
- **效果系统**：物品被收集时产生的效果
- **吸引机制**：当玩家靠近时，物品会被吸引向玩家
- **生命周期**：物品的存在时间和消失逻辑

### IItem 接口
- **核心方法**：定义了物品必须实现的方法
- **标准规范**：为物品类提供统一的接口规范
- **扩展性**：便于实现不同类型的物品

## 类结构

```java
// IItem 接口
public interface IItem {
    String getType();
    void setType(String type);
    void onCollected(Player player);
    // 其他物品相关方法...
}

// Item 类
public class Item extends Obj implements IItem {
    private String type;
    private double value;
    private double lifeTime;
    private double currentLifeTime;
    
    public Item(double x, double y, String type, double value) {
        // 初始化...
    }
    
    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);
        // 更新生命周期和吸引逻辑...
    }
    
    @Override
    public void onCollected(Player player) {
        // 处理物品收集效果...
    }
    
    // 其他方法...
}
```

## 使用示例

### 创建和使用物品

```java
// 创建生命恢复物品
Item healthItem = new Item(
    enemy.getX(), enemy.getY(),  // 位置
    "health",  // 类型
    20         // 恢复值
);

// 创建分数物品
Item scoreItem = new Item(
    enemy.getX(), enemy.getY(),  // 位置
    "score",   // 类型
    1000       // 分数值
);

// 创建炸弹物品
Item bombItem = new Item(
    enemy.getX(), enemy.getY(),  // 位置
    "bomb",    // 类型
    1          // 炸弹数量
);

// 添加到游戏世界
gameWorld.addObject(healthItem);
gameWorld.addObject(scoreItem);
gameWorld.addObject(bombItem);
```

### 处理物品收集

```java
// 在Player类中处理物品收集
@Override
public void onCollision(Obj other) {
    if (other instanceof Item) {
        Item item = (Item) other;
        item.onCollected(this);
        item.setToBeRemoved(true); // 物品被收集后移除
    }
}

// 物品的收集效果
@Override
public void onCollected(Player player) {
    switch (type) {
        case "health":
            player.heal(value);
            break;
        case "score":
            player.addScore(value);
            break;
        case "bomb":
            player.addBomb(value);
            break;
        // 其他类型的物品效果...
    }
}
```

## 设计说明

1. **接口与实现分离**：通过 IItem 接口定义物品的标准，Item 类提供基本实现
2. **继承体系**：Item 类继承自 Obj 类，获得基本的实体功能
3. **可扩展性**：支持通过继承 Item 类或实现 IItem 接口创建自定义物品
4. **模块化设计**：物品的类型和效果可以独立扩展

## 开发建议

- 当需要创建新类型的物品时，继承 Item 类并根据需要重写方法
- 当需要完全自定义物品行为时，实现 IItem 接口
- 为物品添加视觉效果和动画，提升游戏体验
- 考虑物品的掉落率和分布，平衡游戏的难度和奖励
- 可以创建物品管理器类，统一管理物品的创建、掉落和收集