# entity/bullet 包说明

## 功能概述

**entity/bullet 包**是游戏的子弹实体包，包含了子弹相关的类和接口。子弹是游戏中重要的实体类型，用于实现攻击和伤害机制。

## 包含的类

| 类名 | 功能描述 |
|------|----------|
| Bullet | 子弹类，实现了子弹的基本功能 |
| IBullet | 子弹接口，定义了子弹的基本方法 |

## 主要功能

### Bullet 类
- **位置和运动**：继承自 Obj 类，具有位置、速度等属性
- **伤害系统**：可以设置和获取伤害值
- **生命周期**：可以设置子弹的存在时间
- **碰撞检测**：与其他实体发生碰撞时的处理
- **自定义行为**：支持通过重写方法实现自定义子弹行为

### IBullet 接口
- **核心方法**：定义了子弹必须实现的方法
- **标准规范**：为子弹类提供统一的接口规范
- **扩展性**：便于实现不同类型的子弹

## 类结构

```java
// IBullet 接口
public interface IBullet {
    double getDamage();
    void setDamage(double damage);
    // 其他子弹相关方法...
}

// Bullet 类
public class Bullet extends Obj implements IBullet {
    private double damage;
    private double lifeTime;
    private double currentLifeTime;
    
    public Bullet(double x, double y, double vx, double vy, double damage) {
        // 初始化...
    }
    
    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);
        // 更新生命周期...
    }
    
    @Override
    public void onCollision(Obj other) {
        // 处理碰撞...
    }
    
    // 其他方法...
}
```

## 使用示例

### 创建和使用子弹

```java
// 创建玩家子弹
Bullet playerBullet = new Bullet(
    player.getX(), player.getY(),  // 位置
    0, -5,  // 速度（向上）
    10      // 伤害
);

// 创建敌人子弹
Bullet enemyBullet = new Bullet(
    enemy.getX(), enemy.getY(),  // 位置
    0, 3,   // 速度（向下）
    5       // 伤害
);

// 添加到游戏世界
gameWorld.addObject(playerBullet);
gameWorld.addObject(enemyBullet);

// 自定义子弹行为
Bullet homingBullet = new Bullet(x, y, vx, vy, damage) {
    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);
        
        // 简单的追踪逻辑
        if (player != null) {
            double dx = player.getX() - x;
            double dy = player.getY() - y;
            double distance = Math.sqrt(dx * dx + dy * dy);
            
            if (distance > 0) {
                vx += (dx / distance) * 0.1;
                vy += (dy / distance) * 0.1;
            }
        }
    }
};
```

### 处理子弹碰撞

```java
@Override
public void onCollision(Obj other) {
    if (other instanceof Enemy && this instanceof PlayerBullet) {
        // 玩家子弹击中敌人
        Enemy enemy = (Enemy) other;
        enemy.takeDamage(getDamage());
        setToBeRemoved(true); // 子弹击中后消失
    } else if (other instanceof Player && this instanceof EnemyBullet) {
        // 敌人子弹击中玩家
        Player player = (Player) other;
        player.takeDamage(getDamage());
        setToBeRemoved(true); // 子弹击中后消失
    }
}
```

## 设计说明

1. **接口与实现分离**：通过 IBullet 接口定义子弹的标准，Bullet 类提供基本实现
2. **继承体系**：Bullet 类继承自 Obj 类，获得基本的实体功能
3. **可扩展性**：支持通过继承 Bullet 类或实现 IBullet 接口创建自定义子弹
4. **性能考虑**：子弹类设计简洁，确保在大量子弹存在时仍能保持良好性能

## 开发建议

- 当需要创建新类型的子弹时，继承 Bullet 类并根据需要重写方法
- 当需要完全自定义子弹行为时，实现 IBullet 接口
- 为不同类型的子弹创建专门的子类，如 PlayerBullet、EnemyBullet 等
- 考虑使用对象池模式管理子弹，减少内存分配和回收的开销
- 为子弹添加视觉效果，如粒子特效、轨迹等，提升游戏体验