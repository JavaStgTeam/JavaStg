# entity/laser 包说明

## 功能概述

**entity/laser 包**是游戏的激光实体包，包含了激光相关的类。激光是游戏中一种特殊的攻击方式，通常具有穿透性、高伤害或特殊效果。

## 包含的类

| 类名 | 功能描述 |
|------|----------|
| Laser | 激光类，实现了激光的基本功能 |

## 主要功能

### Laser 类
- **基础属性**：继承自 Obj 类，具有位置、速度等属性
- **长度管理**：可以设置和调整激光的长度
- **宽度管理**：可以设置激光的宽度
- **伤害系统**：可以设置激光的伤害值
- **穿透性**：激光可以穿透多个敌人
- **生命周期**：激光的存在时间和消失逻辑
- **视觉效果**：激光的视觉表现和动画

## 类结构

```java
public class Laser extends Obj {
    private double length;
    private double width;
    private double damage;
    private double lifeTime;
    private double currentLifeTime;
    private boolean penetrates;
    
    public Laser(double x, double y, double angle, double length, double width, double damage) {
        // 初始化...
    }
    
    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);
        // 更新生命周期...
    }
    
    @Override
    public void render(Graphics2D g) {
        // 渲染激光...
    }
    
    @Override
    public void onCollision(Obj other) {
        // 处理碰撞...
        if (penetrates) {
            // 穿透逻辑，不标记为删除
        } else {
            // 非穿透逻辑，可能标记为删除
        }
    }
    
    // 其他方法...
}
```

## 使用示例

### 创建和使用激光

```java
// 创建玩家激光
Laser playerLaser = new Laser(
    player.getX(), player.getY(),  // 位置
    Math.PI / 2,  // 角度（向上）
    400,          // 长度
    5,            // 宽度
    20            // 伤害
);
playerLaser.setPenetrates(true); // 设置为穿透

// 创建敌人激光
Laser enemyLaser = new Laser(
    enemy.getX(), enemy.getY(),  // 位置
    Math.PI * 3 / 2,  // 角度（向下）
    300,              // 长度
    8,                // 宽度
    15                // 伤害
);
enemyLaser.setPenetrates(false); // 设置为非穿透

// 添加到游戏世界
gameWorld.addObject(playerLaser);
gameWorld.addObject(enemyLaser);

// 创建旋转激光
Laser rotatingLaser = new Laser(x, y, 0, 350, 6, 10) {
    private double rotationSpeed = 0.02;
    
    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);
        // 旋转激光
        setAngle(getAngle() + rotationSpeed * deltaTime * 60);
    }
};

gameWorld.addObject(rotatingLaser);
```

### 激光碰撞处理

```java
@Override
public void onCollision(Obj other) {
    if (other instanceof Enemy && this instanceof PlayerLaser) {
        // 玩家激光击中敌人
        Enemy enemy = (Enemy) other;
        enemy.takeDamage(damage);
        
        if (!penetrates) {
            setToBeRemoved(true); // 非穿透激光击中后消失
        }
    } else if (other instanceof Player && this instanceof EnemyLaser) {
        // 敌人激光击中玩家
        Player player = (Player) other;
        player.takeDamage(damage);
        
        if (!penetrates) {
            setToBeRemoved(true); // 非穿透激光击中后消失
        }
    }
}
```

## 设计说明

1. **继承体系**：Laser 类继承自 Obj 类，获得基本的实体功能
2. **特殊属性**：添加了激光特有的属性，如长度、宽度、穿透性等
3. **视觉效果**：激光的渲染需要特殊处理，通常是线条或矩形
4. **碰撞检测**：激光的碰撞检测可能需要特殊逻辑，尤其是穿透性激光

## 开发建议

- 当需要创建不同类型的激光时，继承 Laser 类并根据需要重写方法
- 为激光添加视觉效果，如颜色变化、闪烁或粒子效果
- 考虑激光的性能影响，尤其是长激光或大量激光同时存在的情况
- 可以创建激光管理器类，统一管理激光的创建和生命周期
- 为激光添加音效，提升游戏的听觉体验