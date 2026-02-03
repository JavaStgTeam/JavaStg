# IPlayer 接口文档

## 接口概述
`IPlayer` 接口定义了游戏中玩家的行为和属性，继承自 `IGameObject` 接口。

## 方法说明

### 1. moveUp()
**用途**：玩家向上移动。
**参数**：无
**返回值**：无

### 2. moveDown()
**用途**：玩家向下移动。
**参数**：无
**返回值**：无

### 3. moveLeft()
**用途**：玩家向左移动。
**参数**：无
**返回值**：无

### 4. moveRight()
**用途**：玩家向右移动。
**参数**：无
**返回值**：无

### 5. stopHorizontal()
**用途**：停止水平移动。
**参数**：无
**返回值**：无

### 6. stopVertical()
**用途**：停止垂直移动。
**参数**：无
**返回值**：无

### 7. shoot()
**用途**：玩家射击。
**参数**：无
**返回值**：无

### 8. setShooting(boolean shooting)
**用途**：设置玩家是否射击。
**参数**：
- `shooting` (boolean)：是否射击
**返回值**：无

### 9. setSlowMode(boolean slow)
**用途**：设置玩家是否处于低速模式。
**参数**：
- `slow` (boolean)：是否低速模式
**返回值**：无

### 10. isSlowMode()
**用途**：检查玩家是否处于低速模式。
**参数**：无
**返回值**：boolean - 是否处于低速模式

### 11. isInvincible()
**用途**：检查玩家是否无敌。
**参数**：无
**返回值**：boolean - 是否无敌

### 12. onHit()
**用途**：玩家被击中时的处理。
**参数**：无
**返回值**：无

### 13. reset()
**用途**：重置玩家状态。
**参数**：无
**返回值**：无

### 14. getOptions()
**用途**：获取子机列表。
**参数**：无
**返回值**：List<Option> - 子机列表

### 15. addOption(Option option)
**用途**：添加子机。
**参数**：
- `option` (Option)：要添加的子机
**返回值**：无

### 16. getShootInterval()
**用途**：获取射击间隔。
**参数**：无
**返回值**：int - 射击间隔（帧）

### 17. setShootInterval(int interval)
**用途**：设置射击间隔。
**参数**：
- `interval` (int)：射击间隔（帧）
**返回值**：无

### 18. getBulletDamage()
**用途**：获取子弹伤害。
**参数**：无
**返回值**：int - 子弹伤害值

### 19. setBulletDamage(int damage)
**用途**：设置子弹伤害。
**参数**：
- `damage` (int)：子弹伤害值
**返回值**：无

## 继承的方法

### 从 IGameObject 继承
- `update()`：更新玩家状态
- `render(Graphics2D g)`：渲染玩家
- `isActive()`：检查玩家是否活跃
- `getX()`：获取玩家X坐标
- `getY()`：获取玩家Y坐标
- `getSize()`：获取玩家大小
- `getHitboxRadius()`：获取玩家碰撞检测半径
- `setActive(boolean active)`：设置玩家是否活跃