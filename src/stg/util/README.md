# util 包说明

## 功能概述

**util 包**是游戏的工具类包，包含了各种工具类和辅助功能。这些工具类为游戏的其他模块提供支持，如数学计算、资源管理、事件总线等。

## 包结构

```
util/
├── math/          # 数学工具包
├── AnnotationScanner.java  # 注解扫描器
├── AudioManager.java       # 音频管理器
├── CoordinateSystem.java   # 坐标系统
├── EventBus.java           # 事件总线
├── GameConstants.java      # 游戏常量
├── LogUtil.java            # 日志工具
├── RenderUtils.java        # 渲染工具
├── ResourceManager.java    # 资源管理器
└── README.md               # 包说明文档
```

## 包含的类

| 类名 | 功能描述 |
|------|----------|
| AnnotationScanner | 注解扫描器，用于扫描和处理注解 |
| AudioManager | 音频管理器，用于加载和播放音频 |
| CoordinateSystem | 坐标系统，处理游戏中的坐标转换 |
| EventBus | 事件总线，用于事件的分发和处理 |
| GameConstants | 游戏常量，定义游戏中的常量值 |
| LogUtil | 日志工具，用于记录游戏日志 |
| RenderUtils | 渲染工具，提供渲染相关的辅助方法 |
| ResourceManager | 资源管理器，用于加载和管理游戏资源 |

## 主要功能

### AnnotationScanner 类
- **注解扫描**：扫描指定包中的注解
- **反射机制**：使用反射机制处理注解
- **类加载**：动态加载带有特定注解的类
- **扩展性**：支持自定义注解的处理

### AudioManager 类
- **音频加载**：加载游戏中的音频文件
- **音频播放**：播放背景音乐和音效
- **音量控制**：调节音频的音量
- **音频管理**：管理音频的播放状态

### CoordinateSystem 类
- **坐标转换**：处理游戏世界坐标和屏幕坐标的转换
- **缩放处理**：处理不同分辨率下的坐标缩放
- **边界检查**：检查坐标是否在有效范围内
- **坐标系定义**：定义游戏的坐标系

### EventBus 类
- **事件分发**：分发游戏中的事件
- **事件监听**：管理事件监听器
- **事件处理**：处理事件的触发和响应
- **松耦合**：实现组件间的松耦合通信

### GameConstants 类
- **常量定义**：定义游戏中的常量值
- **配置管理**：集中管理游戏配置
- **可维护性**：便于统一修改和管理常量

### LogUtil 类
- **日志记录**：记录游戏中的日志信息
- **日志级别**：支持不同级别的日志
- **日志输出**：控制日志的输出方式
- **调试辅助**：辅助游戏的调试

### RenderUtils 类
- **渲染辅助**：提供渲染相关的辅助方法
- **图形处理**：处理图形的绘制和变换
- **视觉效果**：实现各种视觉效果
- **性能优化**：优化渲染性能

### ResourceManager 类
- **资源加载**：加载游戏中的各种资源
- **资源缓存**：缓存已加载的资源
- **资源释放**：释放不再使用的资源
- **资源管理**：管理资源的生命周期

## math 子包

### 功能概述

**math 子包**是游戏的数学工具包，包含了数学计算相关的类。这些工具类为游戏的物理模拟、碰撞检测、运动计算等提供支持。

### 包含的类

| 类名 | 功能描述 |
|------|----------|
| MathUtils | 数学工具类，提供各种数学计算方法 |
| RandomGenerator | 随机数生成器，提供随机数生成功能 |
| Vector2 | 二维向量类，实现二维向量的运算 |

### MathUtils 类
- **数学计算**：提供各种数学计算方法
- **角度转换**：处理角度和弧度的转换
- **插值计算**：实现线性插值等插值方法
- **边界限制**：限制值在指定范围内

### RandomGenerator 类
- **随机数生成**：生成各种类型的随机数
- **种子管理**：管理随机数生成的种子
- **概率分布**：支持不同的概率分布
- **随机选择**：从集合中随机选择元素

### Vector2 类
- **向量运算**：实现二维向量的各种运算
- **向量属性**：计算向量的长度、角度等属性
- **向量变换**：实现向量的旋转、缩放等变换
- **碰撞检测**：辅助实现基于向量的碰撞检测

## 使用示例

### 使用资源管理器

```java
// 加载图片资源
BufferedImage playerImage = ResourceManager.getInstance().loadImage("player.png");

// 加载音频资源
Clip soundEffect = ResourceManager.getInstance().loadSound("shoot.wav");

// 播放音频
AudioManager.getInstance().playSound(soundEffect);

// 加载背景音乐
AudioManager.getInstance().playMusic("bgm.wav");
```

### 使用事件总线

```java
// 注册事件监听器
eventBus.registerListener(EnemyDestroyedEvent.class, event -> {
    // 处理敌人被摧毁事件
    System.out.println("Enemy destroyed! Score: " + event.getScore());
});

// 触发事件
eventBus.fireEvent(new EnemyDestroyedEvent(enemy, player, 100));
```

### 使用数学工具

```java
// 使用向量
Vector2 position = new Vector2(100, 200);
Vector2 velocity = new Vector2(1, 0);
Vector2 newPosition = position.add(velocity.multiply(10));

// 使用随机数
int randomX = RandomGenerator.getInstance().nextInt(800);
int randomY = RandomGenerator.getInstance().nextInt(600);

// 使用数学工具
double radians = MathUtils.degToRad(45);
double interpolatedValue = MathUtils.lerp(0, 100, 0.5);
```

### 使用坐标系统

```java
// 创建坐标系统
CoordinateSystem coords = new CoordinateSystem(800, 600);

// 世界坐标转屏幕坐标
Vector2 screenPos = coords.worldToScreen(worldPos);

// 屏幕坐标转世界坐标
Vector2 worldPos = coords.screenToWorld(screenPos);
```

### 使用日志工具

```java
// 记录不同级别的日志
LogUtil.debug("Game", "Debug message");
LogUtil.info("Game", "Info message");
LogUtil.warn("Game", "Warning message");
LogUtil.error("Game", "Error message", exception);
```

## 设计说明

1. **工具类设计**：提供静态方法或单例模式的工具类
2. **功能模块化**：将不同功能的工具类分开管理
3. **性能优化**：考虑工具类的性能影响
4. **可扩展性**：支持工具类的扩展和自定义
5. **依赖管理**：最小化工具类之间的依赖

## 开发建议

- 当需要使用工具类时，直接调用相应的静态方法或获取单例实例
- 当需要添加新的工具方法时，添加到相应的工具类中
- 考虑工具类的性能，避免在高频调用的地方使用复杂的工具方法
- 为工具类添加适当的注释和文档
- 可以根据需要创建新的工具类，保持工具类的职责单一