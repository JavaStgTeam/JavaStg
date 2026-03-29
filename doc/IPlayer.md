# IPlayer 接口文档

## 接口概述
`IPlayer` 是玩家接口，定义了玩家的行为和属性。所有玩家类都应该实现这个接口。

## 方法说明

### 1. moveUp()
**用途**：向上移动
**参数**：无
**返回值**：无

### 2. moveDown()
**用途**：向下移动
**参数**：无
**返回值**：无

### 3. moveLeft()
**用途**：向左移动
**参数**：无
**返回值**：无

### 4. moveRight()
**用途**：向右移动
**参数**：无
**返回值**：无

### 5. stopHorizontal()
**用途**：停止水平移动
**参数**：无
**返回值**：无

### 6. stopVertical()
**用途**：停止垂直移动
**参数**：无
**返回值**：无

### 7. shoot()
**用途**：射击
**参数**：无
**返回值**：无

### 8. setShooting(boolean shooting)
**用途**：设置是否射击
**参数**：
- `shooting` (boolean)：是否射击
**返回值**：无

### 9. setSlowMode(boolean slow)
**用途**：设置是否低速模式
**参数**：
- `slow` (boolean)：是否低速模式
**返回值**：无

### 10. isSlowMode()
**用途**：检查是否处于低速模式
**参数**：无
**返回值**：boolean - 是否低速模式

### 11. isInvincible()
**用途**：检查是否无敌
**参数**：无
**返回值**：boolean - 是否无敌

### 12. onHit()
**用途**：被击中时调用
**参数**：无
**返回值**：无

### 13. reset()
**用途**：重置玩家状态
**参数**：无
**返回值**：无

### 14. getShootInterval()
**用途**：获取射击间隔
**参数**：无
**返回值**：int - 射击间隔（帧数）

### 15. setShootInterval(int interval)
**用途**：设置射击间隔
**参数**：
- `interval` (int)：射击间隔（帧数）
**返回值**：无

### 16. getBulletDamage()
**用途**：获取子弹伤害
**参数**：无
**返回值**：int - 子弹伤害值

### 17. setBulletDamage(int damage)
**用途**：设置子弹伤害
**参数**：
- `damage` (int)：子弹伤害值
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
