# JavaSTG 项目问题清单

> 本文档记录代码审查发现的所有问题，按严重级别分类，供团队追踪修复进度。
> 
> **最后更新**: 2026-04-10  
> **审查人**: 资深开发工程师

---

## 📋 问题统计

| 严重级别 | 数量 | 状态 |
|---------|------|------|
| 🚨 P0 严重 | 3个 | ✅ 已修复 |
| ⚠️ P1 重要 | 3个 | 待修复 |
| 💡 P2 改进 | 4个 | 待修复 |

---

## 🚨 P0 严重问题（必须立即修复）

### P0-1: GameLoop 中大量滥用反射 ✅

| 属性 | 内容 |
|------|------|
| **文件** | `src/stg/core/GameLoop.java` |
| **行数** | 82-212 |
| **影响** | 性能损耗、类型安全破坏、维护困难 |
| **状态** | ✅ 已修复（2026-04-10）|

**问题描述**:
每帧（60次/秒）通过反射访问 `Window` 的私有字段：
```java
java.lang.reflect.Field panelStateField = window.getClass().getDeclaredField("currentPanelState");
panelStateField.setAccessible(true);
Object panelState = panelStateField.get(window);
```

**后果**:
- 每帧额外 ~5000ns 反射开销
- 字段重命名会导致运行时崩溃
- 完全无法静态检查

**修复方案**:
1. 定义 `IWindowState` 接口
2. `Window` 实现 `getCurrentUpdateable()` 方法
3. `GameLoop` 直接调用接口方法，删除所有反射代码

---

### P0-2: 异常被大面积静默吞掉 ✅

| 属性 | 内容 |
|------|------|
| **文件** | `src/stg/core/GameLoop.java`, `src/stg/core/GameWorld.java` |
| **影响** | Bug 难以发现、调试困难 |
| **状态** | ✅ 已修复（2026-04-10）|

**问题描述**:
```java
try {
    // ... 反射调用
} catch (Exception ex) {
    // 空 catch，异常被吞掉
}
```

**修复方案**:
- 至少记录日志：`LogUtil.warn("...", e)`
- 不可恢复的错误应快速失败并抛出 `GameEngineException`

---

### P0-3: Obj.create() 对象池逻辑存在竞态条件 ✅

| 属性 | 内容 |
|------|------|
| **文件** | `src/stg/entity/base/Obj.java` (527-581行) |
| **影响** | 多线程下可能获取错误对象或 null |
| **状态** | ✅ 已修复（2026-04-10）|

**问题描述**:
```java
T object = (T) constructor.newInstance(args);
ObjectPoolManager.getInstance().release(object);     // 放入池
return ObjectPoolManager.getInstance().acquire(clazz); // 立刻取出
```

`release` 后 `acquire` 前，其他线程可能取走对象。

**修复方案**:
```java
public static <T extends Obj> T create(Class<T> clazz, Object... args) {
    T object = ObjectPoolManager.getInstance().tryAcquire(clazz);
    if (object != null) {
        object.reset();
        object.initWithArgs(args);
        return object;
    }
    return createNewInstance(clazz, args);
}
```

---

## ⚠️ P1 重要问题（应尽快修复）

### P1-1: CollisionSystem 碰撞逻辑有 Bug

| 属性 | 内容 |
|------|------|
| **文件** | `src/stg/core/CollisionSystem.java` |
| **影响** | 游戏功能异常（子弹穿透、玩家无敌） |
| **状态** | 🔴 未修复 |

**子问题**:

#### A. 子弹穿透问题
`break` 只退出内层循环，同一颗子弹可命中多个敌人：
```java
for (Bullet bullet : world.getPlayerBullets()) {
    for (Enemy enemy : world.getEnemies()) {
        if (checkCollision(bullet, enemy)) {
            enemy.takeDamage(damage);
            break; // ❌ 只跳出内层循环
        }
    }
}
```

#### B. 未使用 hitboxRadius
接口定义了 `getHitboxRadius()`，但实际使用 `getSize()`，导致视觉与判定不一致。

#### C. 缺少敌弹碰玩家检测
`checkCollisions()` 中没有检测敌方子弹与玩家的碰撞，玩家实际上是无敌的。

**修复方案**:
1. 命中后标记子弹为 `inactive`
2. 统一使用 `getHitboxRadius()`
3. 添加 `checkEnemyBulletsVsPlayer()` 方法

---

### P1-2: GameStateManager 是死代码

| 属性 | 内容 |
|------|------|
| **文件** | `src/stg/core/GameStateManager.java` |
| **影响** | 已开发功能未被使用，资源浪费 |
| **状态** | 🔴 未修复 |

**问题描述**:
`GameStateManager` 有完整的状态管理（分数、生命、符卡），但 `Window.java` 和 `GameLoop` 完全没有使用它。`Window` 自己维护了 `isPaused` 字段。

**修复方案**:
- 将 `Window` 的状态管理迁移到 `GameStateManager`
- 或删除 `GameStateManager` 并更新架构文档

---

### P1-3: EventBus 线程安全隐患

| 属性 | 内容 |
|------|------|
| **文件** | `src/stg/util/EventBus.java` |
| **影响** | 可能抛出 ConcurrentModificationException |
| **状态** | 🔴 未修复 |

**问题描述**:
```java
private final Map<Object, ConcurrentHashMap<Class<?>, List<Consumer<?>>>> subscriberMap = new WeakHashMap<>();
```

- `WeakHashMap` 非线程安全
- `ArrayList` 非线程安全
- 可能抛出 `ConcurrentModificationException`

**修复方案**:
```java
private final Map<Object, Map<Class<?>, List<Consumer<?>>>> subscriberMap 
    = Collections.synchronizedMap(new WeakHashMap<>());
// handlers 换用 CopyOnWriteArrayList
```

---

## 💡 P2 改进建议（最佳实践）

### P2-1: Window.java 违反单一职责原则

| 属性 | 内容 |
|------|------|
| **文件** | `src/stg/base/Window.java` (756行) |
| **影响** | 可维护性降低 |
| **状态** | 🟡 待改进 |

**问题描述**:
`Window.java` 承载了：
- OpenGL 初始化
- 音频初始化
- 面板管理
- 状态机
- 暂停逻辑
- 游戏循环启动
- 玩家纹理预加载

超出 CODE_STYLE.md 定义的 500 行限制。

**建议拆分**:
```
Window.java (200行)       → 仅GLFW窗口/渲染/事件
GameSceneManager.java     → 面板切换状态机
GameInitializer.java      → 资源预加载
```

---

### P2-2: 魔法数字问题

| 属性 | 内容 |
|------|------|
| **文件** | `src/stg/core/GameLoop.java`, `src/stg/core/GameWorld.java` |
| **影响** | 可读性差、维护困难 |
| **状态** | 🟡 待改进 |

**问题描述**:
```java
updateMethod.invoke(gameWorld, 720, 960); // 魔法数字
```

`GameConstants.java` 已存在但未被使用。

**修复方案**:
```java
gameWorld.update(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT);
```

---

### P2-3: Obj.reset() 不完整

| 属性 | 内容 |
|------|------|
| **文件** | `src/stg/entity/base/Obj.java` |
| **影响** | 可能产生难以发现的视觉 bug |
| **状态** | 🟡 待改进 |

**问题描述**:
`reset()` 没有重置 `color`、`size`、`hitboxRadius`，子弹从池中取出后可能继承上一个对象的属性。

**修复方案**:
在 `reset()` 中重置所有可变状态字段。

---

### P2-4: GameLoop.start() 阻塞主线程

| 属性 | 内容 |
|------|------|
| **文件** | `src/stg/core/GameLoop.java` |
| **影响** | 容易被误用 |
| **状态** | 🟡 待改进 |

**问题描述**:
```java
public void start() {
    if (!running) {
        running = true;
        run(); // 阻塞调用线程
    }
}
```

**修复方案**:
- 添加注释说明阻塞行为
- 或改为标准 `Runnable` 模式，由调用方创建线程

---

## ✅ 修复进度追踪

| 问题ID | 描述 | 负责人 | 计划完成 | 实际完成 |
|--------|------|--------|----------|----------|
| P0-1 | GameLoop 反射滥用 | AI | 2026-04-10 | ✅ 2026-04-10 |
| P0-2 | 空 catch 块 | AI | 2026-04-10 | ✅ 2026-04-10 |
| P0-3 | 对象池竞态条件 | AI | 2026-04-10 | ✅ 2026-04-10 |
| P1-1 | 碰撞系统 Bug | - | - | - |
| P1-2 | GameStateManager 死代码 | - | - | - |
| P1-3 | EventBus 线程安全 | - | - | - |
| P2-1 | Window 职责拆分 | - | - | - |
| P2-2 | 魔法数字 | - | - | - |
| P2-3 | Obj.reset() 不完整 | - | - | - |
| P2-4 | start() 阻塞问题 | - | - | - |

---

## 🎯 修复优先级建议

### 第一阶段（本周）
1. **P0-1**: 消除反射滥用（影响面最广）
2. **P0-2**: 补充异常处理日志

### 第二阶段（本月）
3. **P1-1**: 修复碰撞系统
4. **P1-2**: 接入或清理 GameStateManager
5. **P0-3**: 修复对象池逻辑

### 第三阶段（长期）
6. **P1-3**: EventBus 线程安全
7. **P2-1~4**: 代码质量改进

---

## 🏆 值得保留的设计

- ✅ 对象池系统（@Pooled 注解自动注册）
- ✅ 坐标系抽象（CoordinateSystem / BoundsUtil）
- ✅ 生命周期状态机（LifecycleState 枚举）
- ✅ 代码注释质量（Javadoc 覆盖率高）
- ✅ 代码规范文档（CODE_STYLE.md）

---

*本文档由资深开发工程师审查生成，供 JavaSTG 团队技术提升参考。*
