# stg.game.player 包

## 功能描述
stg.game.player 包是 JavaStg 游戏引擎的玩家系统包，提供了玩家的基本实现和接口定义。玩家是游戏中的核心元素，负责接收玩家输入并执行相应的动作，如移动、射击等。

## 包含文件
1. **Player.java**：玩家类，继承自 `Obj` 类，实现了玩家的基本功能，如移动、射击、重生等。
2. **IPlayer.java**：玩家接口，定义了玩家的行为和属性。

## 核心功能
1. **玩家基本属性**：位置、速度、大小、颜色、碰撞判定半径等。
2. **移动系统**：实现玩家的上下左右移动，支持普通速度和低速模式。
3. **射击系统**：实现玩家的射击功能，包括射击冷却时间管理。
4. **重生系统**：实现玩家被击中后的重生逻辑，包括重生等待和重生动画。
5. **无敌系统**：实现玩家的无敌状态，包括无敌时间管理和无敌闪烁效果。
6. **边界检测**：检查玩家是否超出游戏边界，并进行限制。
7. **任务系统**：提供任务开始和结束时的回调方法。

## 设计理念
stg.game.player 包采用了类和接口相结合的设计模式，通过 `Player` 类提供基本实现，通过 `IPlayer` 接口定义统一的行为和属性。这种设计使得玩家系统具有良好的扩展性，可以方便地添加新的玩家类型和行为。

## 依赖关系
- 依赖 `stg.game.GameWorld` 类，用于发射子弹。
- 依赖 `stg.game.obj.Obj` 类，玩家是游戏对象的一种。
- 依赖 `stg.game.IGameObject` 接口，玩家实现了游戏对象的基本接口。

## 使用示例

### 创建玩家
```java
// 创建玩家
Player player = new Player(0, 0, 5.0f, 2.0f, 20);

// 设置游戏世界引用
player.setGameWorld(gameWorld);

// 添加玩家到游戏世界
gameWorld.setPlayer(player);
```

### 玩家控制
```java
// 移动玩家
player.moveUp();     // 向上移动
player.moveDown();   // 向下移动
player.moveLeft();   // 向左移动
player.moveRight();  // 向右移动

// 停止移动
player.stopHorizontal(); // 停止水平移动
player.stopVertical();   // 停止垂直移动

// 设置射击状态
player.setShooting(true);  // 开始射击
player.setShooting(false); // 停止射击

// 设置低速模式
player.setSlowMode(true);  // 开启低速模式
player.setSlowMode(false); // 关闭低速模式
```

### 玩家状态管理
```java
// 重置玩家状态
player.reset();

// 玩家被击中
player.onHit();

// 检查玩家状态
boolean isInvincible = player.isInvincible(); // 检查是否无敌
boolean isSlowMode = player.isSlowMode();     // 检查是否低速模式
```

## 扩展建议
1. **添加新的玩家类型**：可以通过继承 `Player` 类来创建新的玩家类型，如不同角色、不同能力的玩家等。
2. **实现不同的射击模式**：可以重写 `shoot()` 方法来实现不同的射击模式，如散弹、激光、跟踪弹等。
3. **添加玩家技能**：可以为玩家添加特殊技能，如炸弹、无敌护盾、速度提升等。
4. **实现玩家升级系统**：可以添加玩家升级系统，通过收集经验值或道具来提升玩家属性。
5. **添加玩家动画**：可以为玩家添加移动、射击、被击中时的动画效果，增强游戏体验。

## 关键方法

### Player 类
- `Player(float x, float y, float speed, float speedSlow, float size)`：构造函数，创建玩家对象。
- `update()`：更新玩家状态，包括移动、射击、边界检测等。
- `render(Graphics2D g)`：渲染玩家，包括无敌闪烁效果。
- `moveUp()`：向上移动。
- `moveDown()`：向下移动。
- `moveLeft()`：向左移动。
- `moveRight()`：向右移动。
- `stopVertical()`：停止垂直移动。
- `stopHorizontal()`：停止水平移动。
- `shoot()`：发射子弹（可重写）。
- `setShooting(boolean shooting)`：设置射击状态。
- `setSlowMode(boolean slow)`：设置低速模式。
- `onHit()`：处理玩家被击中的情况。
- `reset()`：重置玩家状态。
- `isInvincible()`：检查玩家是否无敌。
- `isSlowMode()`：检查玩家是否处于低速模式。
- `setGameWorld(GameWorld gameWorld)`：设置游戏世界引用。
- `onTaskStart()`：任务开始时触发的方法（可重写）。
- `onTaskEnd()`：任务结束时触发的方法（可重写）。

### IPlayer 接口
- `moveUp()`：向上移动。
- `moveDown()`：向下移动。
- `moveLeft()`：向左移动。
- `moveRight()`：向右移动。
- `stopHorizontal()`：停止水平移动。
- `stopVertical()`：停止垂直移动。
- `shoot()`：射击。
- `setShooting(boolean shooting)`：设置是否射击。
- `setSlowMode(boolean slow)`：设置是否低速模式。
- `isSlowMode()`：检查是否处于低速模式。
- `isInvincible()`：检查是否无敌。
- `onHit()`：被击中时调用。
- `reset()`：重置玩家状态。
- `getShootInterval()`：获取射击间隔。
- `setShootInterval(int interval)`：设置射击间隔。
- `getBulletDamage()`：获取子弹伤害。
- `setBulletDamage(int damage)`：设置子弹伤害。