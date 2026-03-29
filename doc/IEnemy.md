# IEnemy 接口文档

## 接口概述
`IEnemy` 是敌人接口，定义了敌人的行为和属性。所有敌人类都应该实现这个接口。

## 方法说明

### 1. takeDamage(int damage)
**用途**：承受伤害
**参数**：
- `damage` (int)：伤害值
**返回值**：无

### 2. isAlive()
**用途**：检查敌人是否存活
**参数**：无
**返回值**：boolean - 是否存活

### 3. getHp()
**用途**：获取当前生命值
**参数**：无
**返回值**：int - 当前生命值

### 4. getMaxHp()
**用途**：获取最大生命值
**参数**：无
**返回值**：int - 最大生命值

### 5. setHp(int hp)
**用途**：设置生命值
**参数**：
- `hp` (int)：生命值
**返回值**：无

### 6. isOutOfBounds(int width, int height)
**用途**：检查敌人是否越界
**参数**：
- `width` (int)：游戏区域宽度
- `height` (int)：游戏区域高度
**返回值**：boolean - 是否越界

### 7. getType()
**用途**：获取敌人类型
**参数**：无
**返回值**：String - 敌人类型

### 8. setSpeed(float speed)
**用途**：设置敌人速度
**参数**：
- `speed` (float)：速度值
**返回值**：无

### 9. getSpeed()
**用途**：获取敌人速度
**参数**：无
**返回值**：float - 速度值

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
