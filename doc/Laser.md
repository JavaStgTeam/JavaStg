# Laser 类文档

## 类概述
`Laser` 是激光基类，所有激光的父类。定义了激光的基本属性和行为，包括预警、激活、碰撞检测等功能。

## 成员变量

### 1. x (float)
**用途**：激光起点X坐标

### 2. y (float)
**用途**：激光起点Y坐标

### 3. angle (float)
**用途**：激光角度（弧度）

### 4. length (float)
**用途**：激光长度

### 5. width (float)
**用途**：激光宽度

### 6. color (Color)
**用途**：激光颜色

### 7. warningOnly (boolean)
**用途**：是否仅显示预警线

### 8. warningTime (int)
**用途**：预警持续时间（帧数）

### 9. warningTimer (int)
**用途**：预警计时器

### 10. active (boolean)
**用途**：激光是否激活（预警结束后）

### 11. visible (boolean)
**用途**：激光是否可见

### 12. damage (int)
**用途**：伤害值

## 构造方法

### 1. Laser(float x, float y, float angle, float length, float width, Color color)
**用途**：创建激光对象
**参数**：
- `x` (float)：起点X坐标
- `y` (float)：起点Y坐标
- `angle` (float)：角度（弧度）
- `length` (float)：长度
- `width` (float)：宽度
- `color` (Color)：颜色
**说明**：默认预警时间60帧，伤害10

### 2. Laser(float x, float y, float angle, float length, float width, Color color, int warningTime, int damage)
**用途**：创建激光对象（完整参数）
**参数**：
- `x` (float)：起点X坐标
- `y` (float)：起点Y坐标
- `angle` (float)：角度（弧度）
- `length` (float)：长度
- `width` (float)：宽度
- `color` (Color)：颜色
- `warningTime` (int)：预警时间（帧数）
- `damage` (int)：伤害值

## 方法说明

### 1. initBehavior()
**用途**：初始化行为参数
**参数**：无
**返回值**：无
**说明**：抽象方法，在构造函数中调用，子类实现

### 2. onUpdate()
**用途**：实现每帧的自定义更新逻辑
**参数**：无
**返回值**：无
**说明**：抽象方法，子类必须实现

### 3. onMove()
**用途**：实现自定义移动逻辑
**参数**：无
**返回值**：无
**说明**：抽象方法，子类必须实现

### 4. update()
**用途**：更新激光状态
**参数**：无
**返回值**：无
**说明**：
- 调用自定义更新逻辑
- 调用自定义移动逻辑
- 处理预警计时器
- 预警结束后激活激光

### 5. render(Graphics g)
**用途**：渲染激光（Java2D版本）
**参数**：
- `g` (Graphics)：图形上下文
**返回值**：无
**说明**：根据active状态渲染预警线或实际激光

### 6. render(IRenderer renderer)
**用途**：渲染激光（IRenderer版本，支持OpenGL）
**参数**：
- `renderer` (IRenderer)：渲染器
**返回值**：无

### 7. renderWarningLine(Graphics2D g2d)
**用途**：渲染预警线
**参数**：
- `g2d` (Graphics2D)：图形上下文
**返回值**：无

### 8. renderLaser(Graphics2D g2d)
**用途**：渲染实际激光
**参数**：
- `g2d` (Graphics2D)：图形上下文
**返回值**：无

### 9. renderWarningLineGL(IRenderer renderer)
**用途**：渲染预警线（IRenderer版本）
**参数**：
- `renderer` (IRenderer)：渲染器
**返回值**：无

### 10. renderLaserGL(IRenderer renderer)
**用途**：渲染实际激光（IRenderer版本）
**参数**：
- `renderer` (IRenderer)：渲染器
**返回值**：无

### 11. checkCollision(float px, float py)
**用途**：检查点是否在激光碰撞体内
**参数**：
- `px` (float)：点X坐标
- `py` (float)：点Y坐标
**返回值**：boolean - 是否碰撞

### 12. isOutOfBounds(int width, int height)
**用途**：检查激光是否超出边界
**参数**：
- `width` (int)：画布宽度
- `height` (int)：画布高度
**返回值**：boolean - 是否超出边界

### 13. pointToLineDistance(float px, float py, float lx, float ly, float angle, float len)
**用途**：计算点到线段的距离
**参数**：
- `px` (float)：点X
- `py` (float)：点Y
- `lx` (float)：线段起点X
- `ly` (float)：线段起点Y
- `angle` (float)：线段角度
- `len` (float)：线段长度
**返回值**：float - 距离

### 14. reset()
**用途**：重置激光状态
**参数**：无
**返回值**：无
**说明**：
- 重置预警计时器
- 设置为未激活
- 设置为可见
- 重新初始化行为

### 15. onTaskStart()
**用途**：任务开始时触发的方法
**参数**：无
**返回值**：无
**说明**：抽象方法，用于处理开局对话等

### 16. onTaskEnd()
**用途**：任务结束时触发的方法
**参数**：无
**返回值**：无
**说明**：抽象方法，用于处理boss击破对话和道具掉落

## Getter和Setter方法

- `getX()`：获取X坐标
- `getY()`：获取Y坐标
- `getAngle()`：获取角度
- `getLength()`：获取长度
- `getWidth()`：获取宽度
- `getColor()`：获取颜色
- `isWarningOnly()`：是否仅预警
- `getWarningTime()`：获取预警时间
- `isActive()`：是否激活
- `isVisible()`：是否可见
- `getDamage()`：获取伤害值
- `setX(float x)`：设置X坐标
- `setY(float y)`：设置Y坐标
- `setAngle(float angle)`：设置角度
- `setLength(float length)`：设置长度
- `setWidth(float width)`：设置宽度
- `setColor(Color color)`：设置颜色
- `setWarningOnly(boolean warningOnly)`：设置是否仅预警
- `setWarningTime(int warningTime)`：设置预警时间
- `setVisible(boolean visible)`：设置是否可见
- `setDamage(int damage)`：设置伤害值
