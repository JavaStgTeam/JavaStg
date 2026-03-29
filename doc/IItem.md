# IItem 接口文档

## 接口概述
`IItem` 是物品接口，定义了物品的行为和属性。所有物品类都应该实现这个接口。

## 方法说明

### 1. onCollect()
**用途**：物品被收集
**参数**：无
**返回值**：无

### 2. applyAttraction()
**用途**：应用吸引力效果
**参数**：无
**返回值**：无

### 3. isOutOfBounds(int width, int height)
**用途**：检查物品是否越界
**参数**：
- `width` (int)：游戏区域宽度
- `height` (int)：游戏区域高度
**返回值**：boolean - 是否越界

### 4. getType()
**用途**：获取物品类型
**参数**：无
**返回值**：String - 物品类型

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
