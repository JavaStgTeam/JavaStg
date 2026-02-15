# stg.game 包

## 功能描述
stg.game 包是 JavaStg 游戏引擎的核心逻辑包，包含了游戏的主要功能实现，如游戏循环、碰撞检测、渲染系统、游戏状态管理等。这个包是游戏引擎的核心部分，实现了游戏的主要逻辑和功能。

## 包结构
stg.game 包包含以下子包：

### 1. 游戏对象子包
- **bullet**：子弹相关类
- **enemy**：敌人相关类
- **item**：道具相关类
- **laser**：激光相关类
- **obj**：游戏对象基类
- **player**：玩家相关类

### 2. 游戏系统子包
- **event**：游戏事件类
- **stage**：关卡相关类
- **ui**：游戏界面相关类

## 核心文件
1. **CollisionSystem.java**：碰撞检测系统，处理游戏对象间的碰撞
2. **GameLoop.java**：游戏主循环，控制游戏的更新和渲染
3. **GameRenderer.java**：游戏渲染系统，负责绘制游戏对象
4. **GameStateManager.java**：游戏状态管理器，管理游戏的各种状态
5. **GameWorld.java**：游戏世界，管理游戏中的所有对象
6. **IGameObject.java**：游戏对象接口，定义了游戏对象的基本方法
7. **IGameWorld.java**：游戏世界接口，定义了游戏世界的基本方法
8. **InputHandler.java**：输入处理器，处理玩家的键盘输入
9. **PauseMenu.java**：暂停菜单，显示游戏暂停时的界面
10. **ResourceDemoWindow.java**：资源演示窗口
11. **ResourceTest.java**：资源测试类

## 核心功能
1. **游戏循环**：通过 `GameLoop` 类实现游戏的主循环，控制游戏的更新和渲染频率
2. **碰撞检测**：通过 `CollisionSystem` 类实现游戏对象间的碰撞检测
3. **游戏渲染**：通过 `GameRenderer` 类实现游戏画面的渲染
4. **游戏状态管理**：通过 `GameStateManager` 类管理游戏的各种状态（如运行、暂停、游戏结束等）
5. **游戏世界**：通过 `GameWorld` 类管理游戏中的所有对象，包括玩家、敌人、子弹等
6. **输入处理**：通过 `InputHandler` 类处理玩家的键盘输入，转换为游戏中的操作
7. **关卡管理**：通过 `stage` 子包中的类实现关卡的创建、管理和切换
8. **游戏界面**：通过 `ui` 子包中的类实现游戏的各种界面，如标题界面、游戏状态面板等

## 设计理念
stg.game 包采用了模块化设计，将不同功能分离为不同的组件，便于维护和扩展。同时，通过接口定义（如 `IGameObject`、`IGameWorld` 等）实现了组件间的解耦，提高了代码的可测试性和可扩展性。

## 依赖关系
- 依赖 `stg.base` 包中的基础组件
- 依赖 `stg.util` 包中的工具类
- 内部子包之间存在依赖关系，如 `bullet`、`enemy` 等子包依赖 `obj` 子包中的基类

## 核心组件说明

### 1. 游戏循环 (GameLoop)
游戏循环是游戏的核心，负责控制游戏的更新和渲染频率。它通过固定的时间间隔调用游戏对象的更新方法，并在每一帧结束时调用渲染方法。

### 2. 碰撞检测系统 (CollisionSystem)
碰撞检测系统负责检测游戏对象间的碰撞，如子弹与敌人、玩家与敌人、玩家与道具等。它通过计算游戏对象间的距离来判断是否发生碰撞。

### 3. 游戏渲染系统 (GameRenderer)
游戏渲染系统负责绘制游戏中的所有对象，如玩家、敌人、子弹、背景等。它通过 Java 的 AWT 和 Swing 包实现图形渲染。

### 4. 游戏状态管理器 (GameStateManager)
游戏状态管理器负责管理游戏的各种状态，如运行、暂停、游戏结束等。它通过状态模式实现不同状态间的切换。

### 5. 游戏世界 (GameWorld)
游戏世界是游戏对象的容器，负责管理游戏中的所有对象，包括玩家、敌人、子弹等。它提供了添加、删除和获取游戏对象的方法。

### 6. 输入处理器 (InputHandler)
输入处理器负责处理玩家的键盘输入，转换为游戏中的操作，如移动、射击等。

## 使用示例
```java
// 创建游戏画布
GameCanvas gameCanvas = new GameCanvas();

// 初始化玩家
Player player = gameCanvas.getPlayer();
player.setPosition(0, 0);

// 启动游戏循环
new GameLoop(gameCanvas).start();

// 创建并添加敌人
Enemy enemy = new Enemy(100, 100);
gameCanvas.getGameWorld().addObject(enemy);

// 创建并添加子弹
Bullet bullet = new Bullet(0, 0, 0, -1, 5);
gameCanvas.getGameWorld().addObject(bullet);
```

## 扩展建议
1. **添加新的游戏对象类型**：可以通过继承 `Obj` 类或实现 `IGameObject` 接口来创建新的游戏对象类型。
2. **扩展碰撞检测系统**：可以通过修改 `CollisionSystem` 类来实现更复杂的碰撞检测算法。
3. **添加新的游戏状态**：可以通过修改 `GameStateManager` 类来添加新的游戏状态。
4. **扩展渲染系统**：可以通过修改 `GameRenderer` 类来添加新的渲染效果。