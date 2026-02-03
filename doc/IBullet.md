# IBullet 接口文档

## 接口概述
`IBullet` 接口定义了游戏中子弹的行为和属性，继承自 `IGameObject` 接口。

## 方法说明

### 1. getDamage()
**用途**：获取子弹的伤害值。
**参数**：无
**返回值**：int - 子弹的伤害值

### 2. setDamage(int damage)
**用途**：设置子弹的伤害值。
**参数**：
- `damage` (int)：子弹的伤害值
**返回值**：无

### 3. isOutOfBounds(int width, int height)
**用途**：检查子弹是否超出游戏边界。
**参数**：
- `width` (int)：游戏边界的宽度
- `height` (int)：游戏边界的高度
**返回值**：boolean - 子弹是否超出边界

### 4. getSpeed()
**用途**：获取子弹的移动速度。
**参数**：无
**返回值**：float - 子弹的移动速度

### 5. setSpeed(float speed)
**用途**：设置子弹的移动速度。
**参数**：
- `speed` (float)：子弹的移动速度
**返回值**：无

### 6. getDirection()
**用途**：获取子弹的移动方向（角度）。
**参数**：无
**返回值**：float - 子弹的移动方向，单位为度

### 7. setDirection(float direction)
**用途**：设置子弹的移动方向（角度）。
**参数**：
- `direction` (float)：子弹的移动方向，单位为度
**返回值**：无

## 继承的方法

### 从 IGameObject 继承
- `update()`：更新子弹状态
- `render(Graphics2D g)`：渲染子弹
- `isActive()`：检查子弹是否活跃
- `getX()`：获取子弹X坐标
- `getY()`：获取子弹Y坐标
- `getSize()`：获取子弹大小
- `getHitboxRadius()`：获取子弹碰撞检测半径
- `setActive(boolean active)`：设置子弹是否活跃