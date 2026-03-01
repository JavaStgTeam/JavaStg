# 未使用类清单

> 生成时间：2026-03-01  
> 分析范围：src/ 目录下所有 Java 类  
> 统计标准：除自身外，无其他代码引用（import、new、extends、implements）

---

## 一、工具类（stg.util）

| 类名 | 文件路径 | 状态 | 说明 |
|------|----------|------|------|
| `ALErrorChecker` | `stg/util/ALErrorChecker.java` | ❌ 未使用 | OpenAL 错误检查工具 |
| `GLErrorChecker` | `stg/util/GLErrorChecker.java` | ❌ 未使用 | OpenGL 错误检查工具 |
| `PerformanceMonitor` | `stg/util/PerformanceMonitor.java` | ❌ 未使用 | 性能监控工具 |
| `EventBus` | `stg/util/EventBus.java` | ❌ 未使用 | 事件总线 |
| `ResourceManager` | `stg/util/ResourceManager.java` | ❌ 未使用 | 资源管理器 |
| `MathUtils` | `stg/util/math/MathUtils.java` | ❌ 未使用 | 数学工具类 |
| `RandomGenerator` | `stg/util/math/RandomGenerator.java` | ❌ 未使用 | 随机数生成器 |
| `RenderUtils` | `stg/util/RenderUtils.java` | ⚠️ 间接未使用 | 仅被 Laser 类使用 |

---

## 二、服务框架（stg.service）

| 类名 | 文件路径 | 状态 | 说明 |
|------|----------|------|------|
| `ServiceManager` | `stg/service/core/ServiceManager.java` | ❌ 未使用 | 服务管理器 |
| `DependencyContainer` | `stg/service/di/DependencyContainer.java` | ❌ 未使用 | 依赖注入容器 |
| `IAudioManager` | `stg/service/audio/IAudioManager.java` | ⚠️ 仅被实现 | 仅 ALAudioManager 实现 |

---

## 三、对象池（stg.util.objectpool）

| 类名 | 文件路径 | 状态 | 说明 |
|------|----------|------|------|
| `GenericObjectPoolManager` | `stg/util/objectpool/GenericObjectPoolManager.java` | ❌ 未使用 | 泛型对象池管理器 |
| `PoolConfig` | `stg/util/objectpool/PoolConfig.java` | ❌ 未使用 | 池配置类 |
| `ObjectFactory` | `stg/util/objectpool/ObjectFactory.java` | ❌ 未使用 | 对象工厂接口 |

---

## 四、事件系统（stg.event）

| 类名 | 文件路径 | 状态 | 说明 |
|------|----------|------|------|
| `ItemCollectedEvent` | `stg/event/ItemCollectedEvent.java` | ❌ 未使用 | 道具收集事件 |
| `EnemySpawnedEvent` | `stg/event/EnemySpawnedEvent.java` | ❌ 未使用 | 敌人生成事件 |
| `EnemyDestroyedEvent` | `stg/event/EnemyDestroyedEvent.java` | ❌ 未使用 | 敌人销毁事件 |
| `BulletFiredEvent` | `stg/event/BulletFiredEvent.java` | ❌ 未使用 | 子弹发射事件 |

---

## 五、核心系统（stg.core）

| 类名 | 文件路径 | 状态 | 说明 |
|------|----------|------|------|
| `CollisionSystem` | `stg/core/CollisionSystem.java` | ❌ 未使用 | 碰撞检测系统 |
| `GameStateManager` | `stg/core/GameStateManager.java` | ❌ 未使用 | 游戏状态管理器 |
| `IGameWorld` | `stg/core/IGameWorld.java` | ❌ 未使用 | 游戏世界接口 |
| `IGameObject` | `stg/core/IGameObject.java` | ❌ 未使用 | 游戏对象接口 |

---

## 六、实体接口与类（stg.entity）

| 类名 | 文件路径 | 状态 | 说明 |
|------|----------|------|------|
| `IPlayer` | `stg/entity/player/IPlayer.java` | ❌ 未使用 | 玩家接口 |
| `IItem` | `stg/entity/item/IItem.java` | ❌ 未使用 | 道具接口 |
| `IEnemy` | `stg/entity/enemy/IEnemy.java` | ❌ 未使用 | 敌人接口 |
| `IBullet` | `stg/entity/bullet/IBullet.java` | ❌ 未使用 | 子弹接口 |
| `Laser` | `stg/entity/laser/Laser.java` | ❌ 未使用 | 激光实体类 |

---

## 七、Demo 类（user.demo）

| 类名 | 文件路径 | 状态 | 说明 |
|------|----------|------|------|
| `TriangleMusicDemo` | `user/demo/TriangleMusicDemo.java` | ❌ 未使用 | 音乐演示程序 |
| `ChineseFontDemo` | `user/demo/ChineseFontDemo.java` | ❌ 未使用 | 字体演示程序 |

---

## 删除建议

### 第一批：安全删除（13 个）
建议优先删除，无依赖风险：

```
stg/util/ALErrorChecker.java
stg/util/GLErrorChecker.java
stg/util/PerformanceMonitor.java
stg/util/EventBus.java
stg/util/ResourceManager.java
stg/util/math/MathUtils.java
stg/util/math/RandomGenerator.java
stg/util/objectpool/GenericObjectPoolManager.java
stg/util/objectpool/PoolConfig.java
stg/util/objectpool/ObjectFactory.java
stg/event/ItemCollectedEvent.java
stg/event/EnemySpawnedEvent.java
stg/event/EnemyDestroyedEvent.java
stg/event/BulletFiredEvent.java
user/demo/TriangleMusicDemo.java
user/demo/ChineseFontDemo.java
```

### 第二批：谨慎删除（7 个）
框架类，删除可能影响未来扩展：

```
stg/service/core/ServiceManager.java
stg/service/di/DependencyContainer.java
stg/service/audio/IAudioManager.java
stg/core/CollisionSystem.java
stg/core/GameStateManager.java
stg/core/IGameWorld.java
stg/core/IGameObject.java
```

### 第三批：接口类（7 个）
影响范围大，需评估接口设计：

```
stg/entity/player/IPlayer.java
stg/entity/item/IItem.java
stg/entity/enemy/IEnemy.java
stg/entity/bullet/IBullet.java
stg/entity/laser/Laser.java
stg/util/RenderUtils.java
```

---

## 备注

- 统计方式：通过 `grep` 搜索 `import`、`new`、`extends`、`implements` 引用
- 文档引用（README.md 等）不计入实际使用
- 部分类可能为预留接口，删除前请确认设计意图
