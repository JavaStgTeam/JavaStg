# stg.game.event 包

## 功能描述
stg.game.event 包是 JavaStg 游戏引擎的事件系统包，提供了游戏中各种事件的定义。事件系统是游戏中的重要组成部分，用于在不同组件之间传递信息，实现组件间的解耦。

## 包含文件
1. **BulletFiredEvent.java**：子弹发射事件，当子弹被发射时触发。
2. **EnemyDestroyedEvent.java**：敌人被销毁事件，当敌人被销毁时触发。
3. **EnemySpawnedEvent.java**：敌人生成事件，当敌人被生成时触发。
4. **ItemCollectedEvent.java**：物品被收集事件，当物品被收集时触发。

## 核心功能
1. **事件定义**：定义游戏中各种重要事件的数据结构。
2. **信息传递**：在事件触发时传递相关的游戏对象引用和附加信息。
3. **组件解耦**：通过事件系统，不同组件之间不需要直接引用，而是通过事件进行通信，实现了解耦。

## 设计理念
stg.game.event 包采用了简单的数据类设计，每个事件类都是一个纯数据类，包含了事件相关的属性和获取这些属性的方法。这种设计使得事件系统简单易用，同时也保证了事件数据的不可变性。

## 依赖关系
- 依赖 `stg.game.bullet.IBullet` 接口，用于子弹发射事件。
- 依赖 `stg.game.enemy.IEnemy` 接口，用于敌人相关事件。
- 依赖 `stg.game.item.IItem` 接口，用于物品收集事件。
- 依赖 `stg.game.player.IPlayer` 接口，用于玩家相关事件。

## 使用示例

### 事件触发示例
```java
// 触发子弹发射事件
BulletFiredEvent event = new BulletFiredEvent(bullet, player);
eventBus.post(event);

// 触发敌人被销毁事件
EnemyDestroyedEvent event = new EnemyDestroyedEvent(enemy, 100);
eventBus.post(event);

// 触发敌人生成事件
EnemySpawnedEvent event = new EnemySpawnedEvent(enemy);
eventBus.post(event);

// 触发物品被收集事件
ItemCollectedEvent event = new ItemCollectedEvent(item, player);
eventBus.post(event);
```

### 事件监听示例
```java
// 监听子弹发射事件
eventBus.register(BulletFiredEvent.class, event -> {
    IBullet bullet = event.getBullet();
    IPlayer player = event.getPlayer();
    // 处理子弹发射事件
});

// 监听敌人被销毁事件
eventBus.register(EnemyDestroyedEvent.class, event -> {
    IEnemy enemy = event.getEnemy();
    int score = event.getScore();
    // 处理敌人被销毁事件，如增加分数
});

// 监听敌人生成事件
eventBus.register(EnemySpawnedEvent.class, event -> {
    IEnemy enemy = event.getEnemy();
    // 处理敌人生成事件
});

// 监听物品被收集事件
eventBus.register(ItemCollectedEvent.class, event -> {
    IItem item = event.getItem();
    IPlayer player = event.getPlayer();
    // 处理物品被收集事件
});
```

## 扩展建议
1. **添加新的事件类型**：可以根据游戏需要添加新的事件类型，如玩家受伤事件、玩家死亡事件、关卡完成事件等。
2. **扩展事件数据**：可以为现有事件添加更多的附加信息，如事件发生的时间、位置等。
3. **实现事件优先级**：可以为事件添加优先级，确保重要事件先被处理。
4. **添加事件过滤**：可以实现事件过滤机制，让监听器只接收符合条件的事件。
5. **实现事件队列**：可以实现事件队列，确保事件按顺序被处理，避免并发问题。

## 关键方法

### 1. BulletFiredEvent 类
- `BulletFiredEvent(IBullet bullet, IPlayer player)`：构造函数，创建子弹发射事件。
- `getBullet()`：获取被发射的子弹。
- `getPlayer()`：获取发射子弹的玩家。

### 2. EnemyDestroyedEvent 类
- `EnemyDestroyedEvent(IEnemy enemy, int score)`：构造函数，创建敌人被销毁事件。
- `getEnemy()`：获取被销毁的敌人。
- `getScore()`：获取得分。

### 3. EnemySpawnedEvent 类
- `EnemySpawnedEvent(IEnemy enemy)`：构造函数，创建敌人生成事件。
- `getEnemy()`：获取生成的敌人。

### 4. ItemCollectedEvent 类
- `ItemCollectedEvent(IItem item, IPlayer player)`：构造函数，创建物品被收集事件。
- `getItem()`：获取被收集的物品。
- `getPlayer()`：获取收集物品的玩家。