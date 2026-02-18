# entity 包说明

## 功能概述

**entity 包**是游戏的实体管理包，包含了游戏中的所有实体类，如敌人、玩家、子弹、物品、激光等。这些实体是游戏世界中的基本元素，构成了游戏的核心内容。

## 包结构

```
entity/
├── base/          # 基础实体包
├── bullet/        # 子弹实体包
├── enemy/         # 敌人实体包
├── item/          # 物品实体包
├── laser/         # 激光实体包
├── player/        # 玩家实体包
└── README.md      # 包说明文档
```

## 各子包功能说明

### base 子包
- **功能**：提供基础实体类
- **主要类**：Obj
- **说明**：所有游戏实体的基类，定义了实体的基本属性和方法

### bullet 子包
- **功能**：管理子弹实体
- **主要类**：Bullet、IBullet
- **说明**：包含子弹的实现和接口

### enemy 子包
- **功能**：管理敌人实体
- **主要类**：Enemy、Boss、EnemySpellcard、IEnemy
- **说明**：包含普通敌人、Boss和敌法术卡的实现

### item 子包
- **功能**：管理物品实体
- **主要类**：Item、IItem
- **说明**：包含游戏中可收集物品的实现

### laser 子包
- **功能**：管理激光实体
- **主要类**：Laser
- **说明**：包含激光武器的实现

### player 子包
- **功能**：管理玩家实体
- **主要类**：Player、IPlayer
- **说明**：包含玩家角色的实现

## 基础实体类 (Obj)

**Obj 类**是所有游戏实体的基类，提供了以下核心功能：

- 位置和速度管理
- 碰撞检测
- 生命周期管理
- 渲染方法
- 基本的更新逻辑

## 实体接口

### IGameObject 接口
- 所有游戏对象的通用接口
- 定义了更新和渲染方法

### 特定实体接口
- **IBullet**：子弹接口
- **IEnemy**：敌人接口
- **IItem**：物品接口
- **IPlayer**：玩家接口

## 使用示例

### 创建和使用实体

```java
// 创建玩家
Player player = new Player(x, y);

// 创建敌人
Enemy enemy = new Enemy(x, y, health, speed);

// 创建子弹
Bullet bullet = new Bullet(x, y, direction, speed, damage);

// 添加到游戏世界
gameWorld.addObject(player);
gameWorld.addObject(enemy);
gameWorld.addObject(bullet);
```

### 扩展实体类

```java
// 创建自定义敌人
public class MyEnemy extends Enemy {
    public MyEnemy(double x, double y) {
        super(x, y, 100, 2.0);
    }
    
    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);
        // 自定义更新逻辑
    }
}
```

## 设计说明

1. **继承体系**：基于 Obj 类构建实体继承体系
2. **接口分离**：通过接口定义实体行为，便于扩展
3. **功能模块化**：不同类型的实体放在不同的子包中
4. **可扩展性**：支持自定义实体的创建和扩展

## 开发建议

- 当需要创建新类型的实体时，继承相应的基类
- 当需要定义新的实体行为时，实现相应的接口
- 当需要修改实体的基础行为时，修改 Obj 类
- 保持实体类的职责单一，避免过大的类
- 为实体类添加适当的注释和文档