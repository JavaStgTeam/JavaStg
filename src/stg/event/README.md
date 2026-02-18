# event 包说明

## 功能概述

**event 包**是游戏的事件系统包，包含了游戏中各种事件的定义和处理。事件系统是游戏中不同组件之间通信的重要机制，用于处理游戏中的各种交互和状态变化。

## 包含的类

| 类名 | 功能描述 |
|------|----------|
| BulletFiredEvent | 子弹发射事件，当子弹被发射时触发 |
| EnemyDestroyedEvent | 敌人被摧毁事件，当敌人被摧毁时触发 |
| EnemySpawnedEvent | 敌人生成事件，当敌人被生成时触发 |
| ItemCollectedEvent | 物品被收集事件，当物品被玩家收集时触发 |

## 主要功能

### 事件系统的核心概念

- **事件触发**：当游戏中发生特定行为时触发相应的事件
- **事件监听**：游戏组件可以监听感兴趣的事件
- **事件处理**：当事件被触发时，执行相应的处理逻辑
- **松耦合**：通过事件系统，不同组件之间可以实现松耦合的通信

### 各事件类的功能

#### BulletFiredEvent
- **触发时机**：当任何实体（玩家或敌人）发射子弹时
- **包含信息**：子弹的类型、发射者、位置等
- **用途**：可以用于统计子弹数量、播放音效等

#### EnemyDestroyedEvent
- **触发时机**：当敌人被玩家摧毁时
- **包含信息**：被摧毁的敌人、摧毁者、获得的分数等
- **用途**：可以用于更新分数、生成物品、播放音效等

#### EnemySpawnedEvent
- **触发时机**：当新的敌人被生成时
- **包含信息**：生成的敌人、生成位置、生成原因等
- **用途**：可以用于统计敌人数量、触发警告等

#### ItemCollectedEvent
- **触发时机**：当玩家收集物品时
- **包含信息**：收集的物品、收集者、获得的效果等
- **用途**：可以用于更新玩家状态、播放音效等

## 类结构

```java
// 基础事件类（如果存在）
public class GameEvent {
    private long timestamp;
    
    public GameEvent() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    // 其他基础方法...
}

// BulletFiredEvent 类
public class BulletFiredEvent extends GameEvent {
    private Bullet bullet;
    private Obj shooter;
    
    public BulletFiredEvent(Bullet bullet, Obj shooter) {
        this.bullet = bullet;
        this.shooter = shooter;
    }
    
    public Bullet getBullet() {
        return bullet;
    }
    
    public Obj getShooter() {
        return shooter;
    }
    
    // 其他方法...
}

// EnemyDestroyedEvent 类
public class EnemyDestroyedEvent extends GameEvent {
    private Enemy enemy;
    private Obj destroyer;
    private int score;
    
    public EnemyDestroyedEvent(Enemy enemy, Obj destroyer, int score) {
        this.enemy = enemy;
        this.destroyer = destroyer;
        this.score = score;
    }
    
    // Getter方法...
}

// EnemySpawnedEvent 类
public class EnemySpawnedEvent extends GameEvent {
    private Enemy enemy;
    private double x;
    private double y;
    
    public EnemySpawnedEvent(Enemy enemy, double x, double y) {
        this.enemy = enemy;
        this.x = x;
        this.y = y;
    }
    
    // Getter方法...
}

// ItemCollectedEvent 类
public class ItemCollectedEvent extends GameEvent {
    private Item item;
    private Player player;
    private String itemType;
    private double value;
    
    public ItemCollectedEvent(Item item, Player player, String itemType, double value) {
        this.item = item;
        this.player = player;
        this.itemType = itemType;
        this.value = value;
    }
    
    // Getter方法...
}
```

## 使用示例

### 触发事件

```java
// 发射子弹时触发事件
public void fireBullet() {
    Bullet bullet = new Bullet(x, y, vx, vy, damage);
    gameWorld.addObject(bullet);
    
    // 触发子弹发射事件
    eventBus.fireEvent(new BulletFiredEvent(bullet, this));
}

// 敌人被摧毁时触发事件
public void die() {
    // 触发敌人被摧毁事件
    eventBus.fireEvent(new EnemyDestroyedEvent(this, destroyer, score));
    setToBeRemoved(true);
}

// 生成敌人时触发事件
public void spawnEnemy() {
    Enemy enemy = new Enemy(x, y, health, speed);
    gameWorld.addObject(enemy);
    
    // 触发敌人生成事件
    eventBus.fireEvent(new EnemySpawnedEvent(enemy, x, y));
}

// 收集物品时触发事件
public void collectItem(Item item) {
    // 处理物品效果
    item.onCollected(this);
    
    // 触发物品被收集事件
    eventBus.fireEvent(new ItemCollectedEvent(item, this, item.getType(), item.getValue()));
    item.setToBeRemoved(true);
}
```

### 监听事件

```java
// 监听子弹发射事件
eventBus.registerListener(BulletFiredEvent.class, event -> {
    // 处理子弹发射事件
    Bullet bullet = event.getBullet();
    Obj shooter = event.getShooter();
    
    // 例如：播放子弹发射音效
    if (shooter instanceof Player) {
        audioManager.playSound("player_shoot");
    } else if (shooter instanceof Enemy) {
        audioManager.playSound("enemy_shoot");
    }
});

// 监听敌人被摧毁事件
eventBus.registerListener(EnemyDestroyedEvent.class, event -> {
    // 处理敌人被摧毁事件
    Enemy enemy = event.getEnemy();
    int score = event.getScore();
    
    // 例如：更新玩家分数
    player.addScore(score);
    
    // 例如：生成物品
    if (Math.random() < 0.3) { // 30%的概率生成物品
        Item item = new Item(enemy.getX(), enemy.getY(), "score", 100);
        gameWorld.addObject(item);
    }
});
```

## 设计说明

1. **事件驱动**：通过事件系统实现游戏组件之间的通信
2. **松耦合**：事件发送者和接收者之间不需要直接引用
3. **可扩展性**：可以轻松添加新的事件类型
4. **灵活性**：可以根据需要注册和取消注册事件监听器

## 开发建议

- 当需要在游戏组件之间传递消息时，使用事件系统
- 当需要处理游戏中的特定行为时，创建相应的事件类
- 为事件添加足够的信息，以便监听器能够做出适当的响应
- 合理使用事件系统，避免过度使用导致性能问题
- 考虑为频繁触发的事件添加缓存机制，提高性能
- 为事件处理逻辑添加适当的错误处理，确保游戏稳定性