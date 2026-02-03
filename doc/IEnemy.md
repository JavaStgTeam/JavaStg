# IEnemy 接口文档

## 接口概述
`IEnemy` 接口定义了游戏中敌人的行为和属性，继承自 `IGameObject` 接口。

## 方法说明

### 1. takeDamage(int damage)
**用途**：使敌人承受指定量的伤害。
**参数**：
- `damage` (int)：敌人承受的伤害值
**返回值**：无

### 2. isAlive()
**用途**：检查敌人是否存活。
**参数**：无
**返回值**：boolean - 敌人是否存活

### 3. getHp()
**用途**：获取敌人当前的生命值。
**参数**：无
**返回值**：int - 当前生命值

### 4. getMaxHp()
**用途**：获取敌人的最大生命值。
**参数**：无
**返回值**：int - 最大生命值

### 5. setHp(int hp)
**用途**：设置敌人的生命值。
**参数**：
- `hp` (int)：要设置的生命值
**返回值**：无

### 6. isOutOfBounds(int width, int height)
**用途**：检查敌人是否超出游戏边界。
**参数**：
- `width` (int)：游戏边界的宽度
- `height` (int)：游戏边界的高度
**返回值**：boolean - 敌人是否超出边界

### 7. getType()
**用途**：获取敌人的类型。
**参数**：无
**返回值**：String - 敌人类型

### 8. setSpeed(float speed)
**用途**：设置敌人的移动速度。
**参数**：
- `speed` (float)：敌人的移动速度
**返回值**：无

### 9. getSpeed()
**用途**：获取敌人的移动速度。
**参数**：无
**返回值**：float - 敌人的移动速度

## 继承的方法

### 从 IGameObject 继承
- `update()`：更新敌人状态
- `render(Graphics2D g)`：渲染敌人
- `isActive()`：检查敌人是否活跃
- `getX()`：获取敌人X坐标
- `getY()`：获取敌人Y坐标
- `getSize()`：获取敌人大小
- `getHitboxRadius()`：获取敌人碰撞检测半径
- `setActive(boolean active)`：设置敌人是否活跃