# core 包说明

## 功能概述

**core 包**是游戏的核心逻辑包，实现了游戏的主要运行机制，包括游戏世界管理、碰撞检测、游戏状态管理和游戏循环等核心功能。这些组件是游戏运行的核心，控制着游戏的整个流程。

## 包含的类

| 类名 | 功能描述 |
|------|----------|
| GameWorld | 游戏世界类，管理游戏中的所有对象 |
| CollisionSystem | 碰撞检测系统，处理游戏对象间的碰撞 |
| GameStateManager | 游戏状态管理器，管理游戏的不同状态 |
| GameLoop | 游戏循环类，控制游戏的更新和渲染 |
| IGameObject | 游戏对象接口，定义游戏对象的基本方法 |
| IGameWorld | 游戏世界接口，定义游戏世界的基本方法 |

## 主要功能

### GameWorld 类
- 管理游戏中的所有对象（敌人、玩家、子弹等）
- 处理对象的添加、移除和更新
- 提供对象查询和检索功能
- 与碰撞系统配合工作

### CollisionSystem 类
- 检测游戏对象间的碰撞
- 处理碰撞事件和响应
- 优化碰撞检测性能
- 支持不同类型的碰撞检测

### GameStateManager 类
- 管理游戏的不同状态（如标题、游戏中、暂停、游戏结束等）
- 处理状态之间的转换
- 提供状态查询和设置功能
- 与游戏循环配合工作

### GameLoop 类
- 实现游戏的主循环
- 控制游戏的更新频率
- 协调游戏的更新和渲染
- 处理游戏的启动和停止

### IGameObject 接口
- 定义游戏对象的基本方法
- 为所有游戏对象提供统一的接口
- 支持对象的更新和渲染

### IGameWorld 接口
- 定义游戏世界的基本方法
- 为游戏世界提供统一的接口
- 支持世界的管理和操作

## 使用示例

### 创建和使用游戏世界

```java
// 创建游戏世界
GameWorld gameWorld = new GameWorld();

// 添加游戏对象
gameWorld.addObject(player);
gameWorld.addObject(enemy);

// 更新游戏世界
gameWorld.update(deltaTime);

// 渲染游戏世界
gameWorld.render(graphics);
```

### 使用碰撞系统

```java
// 获取碰撞系统
CollisionSystem collisionSystem = gameWorld.getCollisionSystem();

// 检测碰撞
collisionSystem.checkCollisions();
```

### 启动游戏循环

```java
// 创建游戏循环
GameLoop gameLoop = new GameLoop(gameCanvas);

// 启动游戏循环
gameLoop.start();

// 停止游戏循环
GameLoop.stopAll();
```

## 设计说明

1. **核心逻辑分离**：将游戏的核心逻辑拆分为多个职责明确的类
2. **接口设计**：通过接口定义核心功能，便于扩展和测试
3. **性能优化**：碰撞检测等核心功能采用优化算法
4. **可维护性**：代码结构清晰，便于理解和修改

## 开发建议

- 当需要修改游戏核心逻辑时，修改相应的核心类
- 当需要添加新的游戏对象类型时，实现 IGameObject 接口
- 当需要扩展游戏世界功能时，考虑实现 IGameWorld 接口
- 保持核心逻辑的稳定性，避免频繁修改影响整个游戏