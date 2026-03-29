# IBullet 接口文档

## 接口概述
`IBullet` 是子弹接口，定义了子弹的行为和属性。所有子弹类都应该实现这个接口。

## 方法说明

### 1. getDamage()
**用途**：获取子弹伤害
**参数**：无
**返回值**：int - 子弹伤害值

### 2. setDamage(int damage)
**用途**：设置子弹伤害
**参数**：
- `damage` (int)：伤害值
**返回值**：无

### 3. isOutOfBounds(int width, int height)
**用途**：检查子弹是否越界
**参数**：
- `width` (int)：游戏区域宽度
- `height` (int)：游戏区域高度
**返回值**：boolean - 是否越界

### 4. getSpeed()
**用途**：获取子弹速度
**参数**：无
**返回值**：float - 子弹速度

### 5. setSpeed(float speed)
**用途**：设置子弹速度
**参数**：
- `speed` (float)：速度值
**返回值**：无

### 6. getDirection()
**用途**：获取子弹方向
**参数**：无
**返回值**：float - 子弹方向（角度，单位：度）

### 7. setDirection(float direction)
**用途**：设置子弹方向
**参数**：
- `direction` (float)：方向角度（单位：度）
**返回值**：无

## 继承的方法

### 从 IGameObject 接口继承
- `update()`：更新对象状态
- `render(Graphics2D g)`：渲染对象
- `isActive()`：检查对象是否活跃
- `getX()`：获取X坐标
- `getY()`：获取Y坐标
- `getSize()`：获取对象大小
- `getHitboxRadius()`：获取碰撞检测半径
- `setActive(boolean active)`：设置对象是否活跃
