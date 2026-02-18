# entity/player 包说明

## 功能概述

**entity/player 包**是游戏的玩家实体包，包含了玩家相关的类和接口。玩家是游戏中的可控角色，是玩家与游戏交互的主要媒介。

## 包含的类

| 类名 | 功能描述 |
|------|----------|
| Player | 玩家类，实现了玩家的基本功能 |
| IPlayer | 玩家接口，定义了玩家的基本方法 |

## 主要功能

### Player 类
- **基础属性**：继承自 Obj 类，具有位置、速度等属性
- **生命值系统**：可以设置和获取生命值，处理伤害和恢复
- **移动控制**：响应玩家的键盘输入，控制移动
- **攻击系统**：实现玩家的攻击逻辑，包括普通攻击和特殊攻击
- **碰撞检测**：与敌人子弹和敌人发生碰撞时的处理
- **状态管理**：管理玩家的各种状态，如无敌、加速等
- **分数系统**：记录和更新玩家的分数

### IPlayer 接口
- **核心方法**：定义了玩家必须实现的方法
- **标准规范**：为玩家类提供统一的接口规范
- **扩展性**：便于实现不同类型的玩家角色

## 类结构

```java
// IPlayer 接口
public interface IPlayer {
    double getHealth();
    void setHealth(double health);
    void takeDamage(double damage);
    void heal(double amount);
    int getScore();
    void addScore(int score);
    // 其他玩家相关方法...
}

// Player 类
public class Player extends Obj implements IPlayer {
    private double health;
    private double maxHealth;
    private int score;
    private int bombs;
    private boolean isInvulnerable;
    private double invulnerableTime;
    
    public Player(double x, double y) {
        // 初始化...
    }
    
    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);
        // 更新移动、攻击、状态等...
    }
    
    @Override
    public void onCollision(Obj other) {
        // 处理碰撞...
    }
    
    public void fire() {
        // 普通攻击逻辑...
    }
    
    public void useBomb() {
        // 炸弹使用逻辑...
    }
    
    // 其他方法...
}
```

## 使用示例

### 创建和使用玩家

```java
// 创建玩家
Player player = new Player(
    400, 500  // 初始位置（屏幕下方中央）
);

// 设置玩家属性
player.setMaxHealth(100);
player.setHealth(100);
player.setBombs(3);

// 添加到游戏世界
gameWorld.addObject(player);

// 控制玩家移动
// 通常在游戏循环中处理键盘输入
public void handleInput() {
    if (keyboard.isKeyPressed(KeyEvent.VK_LEFT)) {
        player.moveLeft();
    }
    if (keyboard.isKeyPressed(KeyEvent.VK_RIGHT)) {
        player.moveRight();
    }
    if (keyboard.isKeyPressed(KeyEvent.VK_UP)) {
        player.moveUp();
    }
    if (keyboard.isKeyPressed(KeyEvent.VK_DOWN)) {
        player.moveDown();
    }
    if (keyboard.isKeyPressed(KeyEvent.VK_Z)) {
        player.fire();
    }
    if (keyboard.isKeyPressed(KeyEvent.VK_X)) {
        player.useBomb();
    }
}
```

### 处理玩家碰撞

```java
@Override
public void onCollision(Obj other) {
    if (other instanceof EnemyBullet && !isInvulnerable) {
        // 被敌人子弹击中
        EnemyBullet bullet = (EnemyBullet) other;
        takeDamage(bullet.getDamage());
        setInvulnerable(true); // 进入无敌状态
    } else if (other instanceof Enemy && !isInvulnerable) {
        // 与敌人碰撞
        takeDamage(20); // 碰撞伤害
        setInvulnerable(true); // 进入无敌状态
    } else if (other instanceof Item) {
        // 收集物品
        Item item = (Item) other;
        item.onCollected(this);
        item.setToBeRemoved(true);
    }
}
```

## 设计说明

1. **接口与实现分离**：通过 IPlayer 接口定义玩家的标准，Player 类提供基本实现
2. **继承体系**：Player 类继承自 Obj 类，获得基本的实体功能
3. **模块化设计**：将玩家功能拆分为多个职责明确的部分
4. **可扩展性**：支持通过继承 Player 类或实现 IPlayer 接口创建自定义玩家角色

## 开发建议

- 当需要创建新类型的玩家角色时，继承 Player 类并根据需要重写方法
- 当需要完全自定义玩家行为时，实现 IPlayer 接口
- 为玩家添加视觉效果和动画，提升游戏体验
- 考虑玩家的移动控制和攻击手感，确保操作流畅
- 平衡玩家的能力和敌人的难度，确保游戏的可玩性
- 可以添加玩家升级系统，提升游戏的成长性