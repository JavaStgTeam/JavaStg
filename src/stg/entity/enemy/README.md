# entity/enemy 包说明

## 功能概述

**entity/enemy 包**是游戏的敌人实体包，包含了敌人相关的类和接口。敌人是游戏中的主要挑战对象，包括普通敌人、Boss和敌法术卡等类型。

## 包含的类

| 类名 | 功能描述 |
|------|----------|
| Enemy | 普通敌人类，实现了敌人的基本功能 |
| Boss | Boss类，实现了Boss敌人的特殊功能 |
| EnemySpellcard | 敌法术卡类，实现了敌法术卡系统 |
| IEnemy | 敌人接口，定义了敌人的基本方法 |

## 主要功能

### Enemy 类
- **基础属性**：继承自 Obj 类，具有位置、速度等属性
- **生命值系统**：可以设置和获取生命值，处理伤害
- **AI行为**：可以实现简单的敌人AI行为
- **碰撞检测**：与玩家子弹发生碰撞时的处理
- **死亡效果**：敌人死亡时的处理逻辑

### Boss 类
- **高级属性**：继承自 Enemy 类，具有更多高级属性
- **法术卡系统**：可以使用和切换法术卡
- **阶段变化**：支持多阶段战斗
- **特殊攻击**：可以使用特殊攻击方式
- **生命值条**：显示Boss的生命值

### EnemySpellcard 类
- **法术卡系统**：实现敌法术卡的逻辑
- **弹幕模式**：定义法术卡的弹幕模式
- **时间限制**：法术卡的持续时间
- **难度设置**：不同难度下的行为调整
- **奖励系统**：成功躲避法术卡后的奖励

### IEnemy 接口
- **核心方法**：定义了敌人必须实现的方法
- **标准规范**：为敌人类提供统一的接口规范
- **扩展性**：便于实现不同类型的敌人

## 类结构

```java
// IEnemy 接口
public interface IEnemy {
    double getHealth();
    void setHealth(double health);
    void takeDamage(double damage);
    boolean isDead();
    // 其他敌人相关方法...
}

// Enemy 类
public class Enemy extends Obj implements IEnemy {
    private double health;
    private double maxHealth;
    
    public Enemy(double x, double y, double health, double speed) {
        // 初始化...
    }
    
    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);
        // 更新AI行为...
    }
    
    @Override
    public void takeDamage(double damage) {
        health -= damage;
        if (health <= 0) {
            die();
        }
    }
    
    // 其他方法...
}

// Boss 类
public class Boss extends Enemy {
    private List<EnemySpellcard> spellcards;
    private EnemySpellcard currentSpellcard;
    private int phase;
    
    public Boss(double x, double y, double health) {
        super(x, y, health, 0);
        // 初始化...
    }
    
    public void useSpellcard(EnemySpellcard spellcard) {
        // 使用法术卡...
    }
    
    // 其他方法...
}

// EnemySpellcard 类
public class EnemySpellcard {
    private String name;
    private double duration;
    private Runnable spellcardLogic;
    
    public EnemySpellcard(String name, double duration, Runnable logic) {
        // 初始化...
    }
    
    public void start() {
        // 开始法术卡...
    }
    
    // 其他方法...
}
```

## 使用示例

### 创建和使用普通敌人

```java
// 创建普通敌人
Enemy enemy = new Enemy(
    400, 100,  // 位置
    50,        // 生命值
    2.0        // 速度
);

// 设置敌人AI行为
enemy.setAIBehavior(() -> {
    // 简单的左右移动
    if (enemy.getX() < 100 || enemy.getX() > 700) {
        enemy.setVx(-enemy.getVx());
    }
    
    // 每隔一段时间发射子弹
    if (enemy.getTimeSinceLastShot() > 1.0) {
        // 创建并发射子弹
        Bullet bullet = new Bullet(enemy.getX(), enemy.getY(), 0, 3, 5);
        gameWorld.addObject(bullet);
        enemy.resetLastShotTime();
    }
});

// 添加到游戏世界
gameWorld.addObject(enemy);
```

### 创建和使用Boss

```java
// 创建Boss
Boss boss = new Boss(
    400, 150,  // 位置
    1000       // 生命值
);

// 创建法术卡
EnemySpellcard spellcard1 = new EnemySpellcard(
    "弹幕洗礼",  // 法术卡名称
    30.0,       // 持续时间（秒）
    () -> {
        // 法术卡逻辑
        // 例如：发射环形弹幕
    }
);

// 添加法术卡到Boss
boss.addSpellcard(spellcard1);

// 开始使用法术卡
boss.useSpellcard(spellcard1);

// 添加到游戏世界
gameWorld.addObject(boss);
```

## 设计说明

1. **继承体系**：通过继承关系构建敌人的层次结构
2. **接口分离**：通过 IEnemy 接口定义敌人的标准
3. **模块化设计**：将敌人功能拆分为多个职责明确的类
4. **可扩展性**：支持通过继承和组合创建自定义敌人

## 开发建议

- 当需要创建普通敌人时，继承 Enemy 类并实现适当的AI行为
- 当需要创建Boss敌人时，继承 Boss 类并添加法术卡
- 当需要创建自定义敌法术卡时，创建 EnemySpellcard 实例
- 为敌人添加多样化的AI行为，提升游戏的挑战性
- 考虑敌人的视觉设计和动画效果，提升游戏的视觉体验
- 平衡敌人的难度，确保游戏的可玩性