# Boss 类文档

## 类概述
`Boss` 是所有Boss的基类，继承自 `Enemy` 类。实现了Boss的入场、退场、符卡系统和多阶段管理。

## 成员变量

### 1. spellcards (List<EnemySpellcard>)
**用途**：Boss的符卡列表

### 2. currentSpellcard (EnemySpellcard)
**用途**：当前激活的符卡

### 3. currentPhase (int)
**用途**：当前阶段

### 4. maxPhase (int)
**用途**：最大阶段数

### 5. isEntering (boolean)
**用途**：是否正在入场

### 6. isExiting (boolean)
**用途**：是否正在退场

### 7. enterFrameCount (int)
**用途**：入场动画帧数

### 8. exitFrameCount (int)
**用途**：退场动画帧数

### 9. ENTER_DURATION (int)
**用途**：入场动画持续帧数
**默认值**：120

### 10. EXIT_DURATION (int)
**用途**：退场动画持续帧数
**默认值**：90

## 构造方法

### 1. Boss(float x, float y, float size, Color color)
**用途**：创建Boss对象
**参数**：
- `x` (float)：Boss的X坐标
- `y` (float)：Boss的Y坐标
- `size` (float)：Boss的大小
- `color` (Color)：Boss的颜色
**说明**：
- 速度默认值：0, 0
- 生命值由符卡管理，不再作为构造参数

## 方法说明

### 1. initSpellcards()
**用途**：初始化符卡列表
**参数**：无
**返回值**：无
**说明**：抽象方法，子类必须实现，添加所有符卡

### 2. update(int canvasWidth, int canvasHeight)
**用途**：更新Boss状态
**参数**：
- `canvasWidth` (int)：画布宽度
- `canvasHeight` (int)：画布高度
**返回值**：无
**重写**：重写了 `Enemy` 类的方法

### 3. updateEnterLogic()
**用途**：更新入场逻辑
**参数**：无
**返回值**：无

### 4. updateExitLogic()
**用途**：更新退场逻辑
**参数**：无
**返回值**：无

### 5. startNextSpellcard()
**用途**：开始下一个符卡
**参数**：无
**返回值**：无

### 6. startExit()
**用途**：开始退场
**参数**：无
**返回值**：无

### 7. addSpellcard(EnemySpellcard spellcard)
**用途**：添加符卡
**参数**：
- `spellcard` (EnemySpellcard)：符卡对象
**返回值**：无

### 8. takeDamage(int damage)
**用途**：使Boss承受伤害，传递给当前符卡
**参数**：
- `damage` (int)：Boss承受的伤害值
**返回值**：无
**重写**：重写了 `Enemy` 类的方法

### 9. renderHealthBar(Graphics2D g, float screenX, float screenY)
**用途**：渲染Boss的生命值条，显示当前符卡的生命值
**参数**：
- `g` (Graphics2D)：用于绘制的图形上下文对象
- `screenX` (float)：Boss在屏幕上的X坐标
- `screenY` (float)：Boss在屏幕上的Y坐标
**返回值**：无
**重写**：重写了 `Enemy` 类的方法

### 10. getCurrentSpellcard()
**用途**：获取当前符卡
**参数**：无
**返回值**：EnemySpellcard - 当前符卡

### 11. getCurrentPhase()
**用途**：获取当前阶段
**参数**：无
**返回值**：int - 当前阶段

### 12. getMaxPhase()
**用途**：获取最大阶段
**参数**：无
**返回值**：int - 最大阶段

### 13. isEntering()
**用途**：检查是否正在入场
**参数**：无
**返回值**：boolean - 是否正在入场

### 14. isExiting()
**用途**：检查是否正在退场
**参数**：无
**返回值**：boolean - 是否正在退场

### 15. onTaskStart()
**用途**：任务开始时触发的方法
**参数**：无
**返回值**：无
**重写**：重写了 `Enemy` 类的方法

### 16. onTaskEnd()
**用途**：任务结束时触发的方法
**参数**：无
**返回值**：无
**重写**：重写了 `Enemy` 类的方法

## 继承的方法

### 从 Enemy 类继承
- `hp`：生命值（由符卡管理，不再使用）
- `maxHp`：最大生命值（由符卡管理，不再使用）
- `render(Graphics2D g)`：渲染敌人
- `isOutOfBounds(int canvasWidth, int canvasHeight)`：检查是否越界
- `isAlive()`：检查是否存活

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
- `setColor(Color color)`：设置颜色
- `isActive()`：检查是否活跃
- `setActive(boolean active)`：设置是否活跃
- `getHitboxRadius()`：获取碰撞检测半径
- `toScreenCoords(float x, float y)`：将游戏坐标转换为屏幕坐标
