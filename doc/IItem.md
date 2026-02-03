# IItem 接口文档

## 接口概述
`IItem` 接口定义了游戏中物品的行为和属性，继承自 `IGameObject` 接口。

## 方法说明

### 1. onCollect()
**用途**：物品被收集时触发的方法。
**参数**：无
**返回值**：无

### 2. applyAttraction()
**用途**：应用吸引力效果，使物品向玩家移动。
**参数**：无
**返回值**：无

### 3. isOutOfBounds(int width, int height)
**用途**：检查物品是否超出游戏边界。
**参数**：
- `width` (int)：游戏边界的宽度
- `height` (int)：游戏边界的高度
**返回值**：boolean - 物品是否超出边界

### 4. getType()
**用途**：获取物品的类型。
**参数**：无
**返回值**：String - 物品类型

## 继承的方法

### 从 IGameObject 继承
- `update()`：更新物品状态
- `render(Graphics2D g)`：渲染物品
- `isActive()`：检查物品是否活跃
- `getX()`：获取物品X坐标
- `getY()`：获取物品Y坐标
- `getSize()`：获取物品大小
- `getHitboxRadius()`：获取物品碰撞检测半径
- `setActive(boolean active)`：设置物品是否活跃