# 对象池系统使用说明

## 概述

对象池系统用于管理游戏中频繁创建和销毁的对象，如子弹、敌人、道具等。通过对象复用，减少 GC 压力，提升游戏性能。

## 核心组件

- **Obj.create()**: 从对象池获取实例，或在对象池未初始化时直接创建
- **Obj.release()**: 回收对象到对象池
- **Obj.initializeObjectPools()**: 初始化所有对象池

## 使用方法

### 1. 初始化对象池

在游戏启动时调用：

```java
// 在 GameCanvas 构造函数或游戏初始化方法中
Obj.initializeObjectPools();
```

### 2. 创建对象

使用 `Obj.create()` 替代 `new` 关键字：

```java
// 创建子弹
DefaultPlayerMainBullet bullet = Obj.create(DefaultPlayerMainBullet.class, x, y);
bullet.setDamage(10);
gameWorld.addPlayerBullet(bullet);

// 创建敌人
__FairyEnemy enemy = Obj.create(__FairyEnemy.class, x, y);
enemy.setGameWorld(gameWorld);
gameWorld.addEnemy(enemy);
```

### 3. 回收对象

当对象不再需要时（如超出边界、生命值为0等），使用 `Obj.release()` 回收：

```java
// 在 GameWorld 更新方法中
if (!enemy.isAlive() || enemy.isOutOfBounds()) {
    enemies.remove(enemy);
    Obj.release(enemy);
}
```

## 注意事项

### 构造函数参数

`Obj.create()` 会尝试查找匹配参数数量的构造函数。如果找不到匹配的构造函数，会抛出异常。

### 异常处理

- 当对象池未初始化时，`Obj.create()` 会回退到直接创建对象
- 当对象池未初始化时，`Obj.release()` 会忽略异常，对象会被 GC 回收

### 线程安全

- 对象池管理器使用 `ConcurrentHashMap` 存储对象池
- 初始化过程使用双重检查锁定确保线程安全
- 对象池实现使用 `ConcurrentLinkedQueue` 确保线程安全

## 性能优化

### 对象池容量配置

默认容量配置：
- **Bullet**: 初始100，最大500
- **Enemy**: 初始20，最大100
- **Item**: 初始50，最大200

根据游戏实际情况，可以调整这些值以获得最佳性能。

### 最佳实践

1. **尽早初始化**：在游戏启动时就初始化对象池
2. **合理使用**：只对频繁创建/销毁的对象使用对象池
3. **正确回收**：确保所有不再使用的对象都被回收
4. **状态重置**：确保 `resetState()` 方法重置所有必要的字段
5. **监控性能**：定期检查对象池的使用情况和性能

## 示例代码

### 完整示例

```java
// 1. 初始化对象池
Obj.initializeObjectPools();

// 2. 创建对象
Enemy enemy = Obj.create(Enemy.class, 100, 100);
enemy.setGameWorld(gameWorld);
gameWorld.addEnemy(enemy);

// 3. 使用对象
enemy.update();

// 4. 回收对象
if (!enemy.isAlive()) {
    gameWorld.removeEnemy(enemy);
    Obj.release(enemy);
}
```

## 故障排除

### 常见问题

1. **对象池未初始化**
   - 症状：创建对象时回退到直接创建
   - 解决：确保在使用对象池前调用 `Obj.initializeObjectPools()`

2. **构造函数参数不匹配**
   - 症状：抛出 "No suitable constructor found" 异常
   - 解决：确保提供的参数与构造函数匹配

3. **对象状态未重置**
   - 症状：回收的对象保持旧状态
   - 解决：完善 `resetState()` 方法实现

4. **内存泄漏**
   - 症状：内存使用量持续增长
   - 解决：确保所有对象都被正确回收，检查对象池容量是否合理

## 版本历史

- **2026-02-21**: 实现 Obj 类对象池集成
- **2026-02-20**: 初始实现对象池核心组件

## 贡献

欢迎提交问题和改进建议！