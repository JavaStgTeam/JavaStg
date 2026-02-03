# Bullet 类文档

## 类概述
`Bullet` 是所有子弹的基类，继承自 `Obj` 类，实现了 `IBullet` 接口。定义了子弹的基本行为和属性。

## 成员变量

### 1. damage (int)
**用途**：子弹的伤害值
**默认值**：0（由玩家统一控制）

## 构造方法

### 1. Bullet(float x, float y, float vx, float vy, float size, Color color)
**用途**：创建子弹对象
**参数**：
- `x` (float)：子弹的初始X坐标
- `y` (float)：子弹的初始Y坐标
- `vx` (float)：子弹的X方向速度
- `vy` (float)：子弹的Y方向速度
- `size` (float)：子弹的大小
- `color` (Color)：子弹的颜色
**说明**：
- 碰撞判定半径默认设置为size的5倍，确保高速子弹不会穿透敌人
- 游戏画布默认设置为null

## 方法说明

### 1. getDamage()
**用途**：获取子弹的伤害值
**参数**：无
**返回值**：int - 子弹的伤害值

### 2. setDamage(int damage)
**用途**：设置子弹的伤害值
**参数**：
- `damage` (int)：子弹的伤害值
**返回值**：无

### 3. onTaskStart()
**用途**：任务开始时触发的方法，用于处理开局对话等
**参数**：无
**返回值**：无
**说明**：抽象方法，子类必须实现

### 4. onTaskEnd()
**用途**：任务结束时触发的方法，用于处理boss击破对话和道具掉落
**参数**：无
**返回值**：无
**说明**：抽象方法，子类必须实现

## 继承的方法

### 从 Obj 类继承
- `getX()`：获取X坐标
- `getY()`：获取Y坐标
- `setX(float x)`：设置X坐标
- `setY(float y)`：设置Y坐标
- `getVx()`：获取X方向速度
- `getVy()`：获取Y方向速度
- `setVx(float vx)`：设置X方向速度
- `setVy(float vy)`：设置Y方向速度
- `getSize()`：获取大小
- `setSize(float size)`：设置大小
- `getColor()`：获取颜色
- `setColor(Color color)`：设置颜色
- `isActive()`：检查是否活跃
- `setActive(boolean active)`：设置是否活跃
- `getHitboxRadius()`：获取碰撞检测半径
- `setHitboxRadius(float hitboxRadius)`：设置碰撞检测半径
- `reset()`：重置对象状态
- `toScreenCoords(float x, float y)`：将游戏坐标转换为屏幕坐标
- `getGameCanvas()`：获取游戏画布
- `setGameCanvas(GameCanvas gameCanvas)`：设置游戏画布
- `update()`：更新对象状态
- `render(Graphics2D g)`：渲染对象
- `isOutOfBounds(int canvasWidth, int canvasHeight)`：检查对象是否超出边界

### 从 IGameObject 接口继承
- `update()`：更新对象状态
- `render(Graphics2D g)`：渲染对象
- `isActive()`：检查对象是否活跃
- `getX()`：获取对象X坐标
- `getY()`：获取对象Y坐标
- `getSize()`：获取对象大小
- `getHitboxRadius()`：获取对象碰撞检测半径
- `setActive(boolean active)`：设置对象是否活跃

### 从 IBullet 接口继承
- `getDamage()`：获取子弹的伤害值
- `setDamage(int damage)`：设置子弹的伤害值
- `isOutOfBounds(int width, int height)`：检查子弹是否超出游戏边界
- `getSpeed()`：获取子弹的移动速度
- `setSpeed(float speed)`：设置子弹的移动速度
- `getDirection()`：获取子弹的移动方向
- `setDirection(float direction)`：设置子弹的移动方向