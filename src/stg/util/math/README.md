# stg.util.math 包

## 功能描述
stg.util.math 包是 JavaStg 游戏引擎的数学工具包，提供了游戏开发中常用的数学工具类，包括数学工具函数、随机数生成器和二维向量类。这些工具类为游戏引擎的其他部分提供了基础的数学计算支持，使得游戏开发更加高效和便捷。

## 包含文件
1. **MathUtils.java**：数学工具类，提供常用的数学计算方法，如角度转换、线性插值、限制值范围、距离计算等。
2. **RandomGenerator.java**：随机数生成器类，提供各种类型的随机数生成方法，如整数、浮点数、布尔值、向量等。
3. **Vector2.java**：二维向量类，提供向量的基本运算方法，如加法、减法、乘法、除法、归一化、点积、叉积等。

## 核心功能
1. **数学计算**：提供角度转换、线性插值、限制值范围、距离计算、碰撞检测等常用数学计算方法。
2. **随机数生成**：提供各种类型的随机数生成方法，支持设置种子，确保随机数的可重复性。
3. **向量运算**：提供二维向量的基本运算方法，支持向量的创建、复制、加减乘除、归一化、点积、叉积、旋转等操作。
4. **碰撞检测**：提供点在矩形内、点在圆形内、两个圆是否相交等碰撞检测方法。
5. **平滑过渡**：提供平滑阻尼方法，用于实现值的平滑过渡，如相机跟踪、角色移动等。

## 设计理念
stg.util.math 包采用了工具类设计模式，通过静态方法和实例方法提供功能：
- **静态方法**：对于不需要维护状态的工具类（如 `MathUtils`），使用静态方法提供功能。
- **实例方法**：对于需要维护状态的工具类（如 `RandomGenerator`），使用实例方法提供功能。
- **对象导向**：对于需要封装数据和行为的类（如 `Vector2`），使用对象导向的设计模式。
- **性能优化**：提供距离的平方计算方法（`distanceSquared`），比直接计算距离更快，适用于距离比较。

这种设计使得数学工具包具有良好的可访问性和可扩展性，可以方便地在游戏引擎的各个部分使用。

## 依赖关系
- **MathUtils**：依赖 `stg.util.math.Vector2` 类，用于向量相关的计算。
- **RandomGenerator**：依赖 `stg.util.math.Vector2` 类，用于生成随机向量。
- **Vector2**：无外部依赖。

## 使用示例

### 使用 MathUtils
```java
// 角度转换
float radians = MathUtils.toRadians(45); // 45度转弧度
float degrees = MathUtils.toDegrees(radians); // 弧度转角度

// 线性插值
float value = MathUtils.lerp(0, 100, 0.5f); // 在0和100之间插值，t=0.5

// 限制值范围
float clampedValue = MathUtils.clamp(150, 0, 100); // 将150限制在0-100之间

// 距离计算
float distance = MathUtils.distance(0, 0, 100, 100); // 计算两点间距离
float distanceSquared = MathUtils.distanceSquared(0, 0, 100, 100); // 计算两点间距离的平方

// 碰撞检测
boolean pointInCircle = MathUtils.pointInCircle(5, 5, 0, 0, 10); // 检查点是否在圆形内
boolean circleCollision = MathUtils.circleCollision(0, 0, 5, 10, 0, 5); // 检查两个圆是否相交

// 平滑阻尼
float currentValue = 0;
float targetValue = 100;
float smoothedValue = MathUtils.damp(currentValue, targetValue, 0.1f); // 平滑过渡到目标值
```

### 使用 RandomGenerator
```java
// 创建随机数生成器
RandomGenerator random = new RandomGenerator();

// 设置种子
random.setSeed(12345);

// 生成随机整数
int randomInt = random.randomInt(0, 100); // 生成0-100的随机整数
int randomIntMax = random.randomInt(100); // 生成0-100的随机整数

// 生成随机浮点数
float randomFloat = random.randomRange(0, 1); // 生成0-1的随机浮点数
float randomFloatMax = random.randomFloat(1); // 生成0-1的随机浮点数

// 生成随机布尔值
boolean randomBoolean = random.randomBoolean(); // 生成随机布尔值

// 生成随机向量
Vector2 randomVector = random.randomVector2(10, 10); // 生成x和y在-10到10之间的随机向量

// 生成随机颜色
java.awt.Color randomColor = random.randomColor(); // 生成随机颜色
java.awt.Color randomColorAlpha = random.randomColorAlpha(); // 生成带透明度的随机颜色

// 从数组中随机选择元素
String[] array = {"a", "b", "c"};
String randomElement = random.choice(array); // 从数组中随机选择一个元素
```

### 使用 Vector2
```java
// 创建向量
Vector2 position = new Vector2(10, 20);
Vector2 velocity = new Vector2(1, 0);

// 向量运算
Vector2 newPosition = position.add(velocity.multiply(10)); // 位置加上速度乘以时间
Vector2 direction = new Vector2(100, 200).subtract(position).normalize(); // 计算从当前位置到目标位置的单位方向向量

// 向量属性
float length = velocity.length(); // 获取向量长度
Vector2 normalizedVelocity = velocity.normalize(); // 归一化向量

// 向量点积和叉积
float dotProduct = position.dot(velocity); // 计算点积
float crossProduct = position.cross(velocity); // 计算叉积

// 向量旋转
Vector2 rotatedVector = velocity.rotate(MathUtils.toRadians(90)); // 将向量旋转90度

// 向量距离
float distance = position.distanceTo(new Vector2(100, 200)); // 计算两个向量之间的距离

// 从角度创建向量
Vector2 directionFromAngle = Vector2.fromAngle(MathUtils.toRadians(45)); // 从45度角创建单位向量
```

## 扩展建议
1. **添加更多数学工具**：可以添加更多数学工具类，如矩阵运算、四元数运算等，用于处理更复杂的数学问题。
2. **优化性能**：对于频繁调用的数学方法，可以进行性能优化，如使用查表法、避免重复计算等。
3. **添加更多向量类型**：可以添加三维向量类（Vector3），用于处理3D游戏中的向量运算。
4. **添加几何工具**：可以添加几何工具类，如线段、矩形、圆形等，提供更复杂的几何运算和碰撞检测方法。
5. **添加噪声生成**：可以添加噪声生成类，如Perlin噪声、 simplex噪声等，用于生成自然的随机效果，如地形、云彩等。
6. **添加物理工具**：可以添加物理工具类，如刚体、碰撞体、关节等，用于实现简单的物理模拟。

## 关键方法

### MathUtils 类
- `toRadians(float degrees)`：将角度从度转换为弧度。
- `toDegrees(float radians)`：将角度从弧度转换为度。
- `lerp(float a, float b, float t)`：线性插值，在a和b之间根据t值插值。
- `clamp(float value, float min, float max)`：限制值在指定范围内。
- `distance(float x1, float y1, float x2, float y2)`：计算两点间的距离。
- `distanceSquared(float x1, float y1, float x2, float y2)`：计算两点间的平方距离。
- `pointInCircle(float px, float py, float cx, float cy, float radius)`：检查点是否在圆形内。
- `circleCollision(float x1, float y1, float r1, float x2, float y2, float r2)`：检查两个圆是否相交。
- `damp(float current, float target, float factor)`：平滑阻尼，用于实现值的平滑过渡。

### RandomGenerator 类
- `randomInt(int min, int max)`：生成min到max之间的随机整数。
- `randomRange(float min, float max)`：生成min到max之间的随机浮点数。
- `randomBoolean()`：生成随机布尔值。
- `randomVector2(float xRange, float yRange)`：生成x和y在指定范围内的随机向量。
- `randomColor()`：生成随机颜色。
- `choice(T[] array)`：从数组中随机选择一个元素。
- `setSeed(long seed)`：设置随机数生成器的种子。

### Vector2 类
- `Vector2(float x, float y)`：构造函数，创建二维向量对象。
- `copy()`：复制向量。
- `add(Vector2 other)`：向量加法。
- `subtract(Vector2 other)`：向量减法。
- `multiply(float scalar)`：向量乘法（标量）。
- `divide(float scalar)`：向量除法（标量）。
- `length()`：获取向量长度。
- `normalize()`：归一化向量。
- `dot(Vector2 other)`：计算向量点积。
- `cross(Vector2 other)`：计算向量叉积。
- `distanceTo(Vector2 other)`：计算与另一个向量的距离。
- `rotate(float angle)`：旋转向量（弧度）。
- `fromAngle(float angle)`：从角度创建向量（弧度）。