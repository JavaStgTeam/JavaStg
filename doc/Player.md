# Player 类文档

## 类概述
`Player` 是玩家类，自机角色。实现了 `IRenderable` 接口，管理玩家的移动、射击、生命值和符卡系统。

## 成员变量

### 基本属性

#### 1. x (float)
**用途**：X坐标

#### 2. y (float)
**用途**：Y坐标

#### 3. vx (float)
**用途**：X方向速度

#### 4. vy (float)
**用途**：Y方向速度

#### 5. size (float)
**用途**：物体大小

#### 6. color (Color)
**用途**：物体颜色

#### 7. hitboxRadius (float)
**用途**：碰撞判定半径

#### 8. active (boolean)
**用途**：激活状态

### 玩家特有属性

#### 9. speed (float)
**用途**：普通移动速度

#### 10. speedSlow (float)
**用途**：低速移动速度

#### 11. slowMode (boolean)
**用途**：低速模式标志

#### 12. shooting (boolean)
**用途**：射击标志

#### 13. shootCooldown (int)
**用途**：射击冷却时间

#### 14. SHOOT_INTERVAL (int)
**用途**：射击间隔（常量）
**默认值**：1

#### 15. respawnTimer (int)
**用途**：重生计时（帧数）

#### 16. RESPAWN_TIME (int)
**用途**：重生等待时间（帧数，常量）
**默认值**：60

#### 17. spawnX (float)
**用途**：重生X坐标

#### 18. spawnY (float)
**用途**：重生Y坐标

#### 19. respawning (boolean)
**用途**：重生动画标志

#### 20. RESPAWN_SPEED (float)
**用途**：重生移动速度（常量）
**默认值**：8.0f

#### 21. invincibleTimer (int)
**用途**：无敌时间计时（帧数）

#### 22. INVINCIBLE_TIME (int)
**用途**：无敌时间（帧数，常量）
**默认值**：120

#### 23. BULLET_DAMAGE (int)
**用途**：子弹伤害（常量）
**默认值**：2

#### 24. keyStateProvider (KeyStateProvider)
**用途**：按键状态提供者

#### 25. sharedCoordinateSystem (CoordinateSystem)
**用途**：共享坐标系统（静态）

### 生命值系统

#### 26. maxLives (int)
**用途**：最大生命值

#### 27. currentLives (int)
**用途**：当前生命值

### 符卡机制

#### 28. maxSpellCards (int)
**用途**：最大符卡数量

#### 29. currentSpellCards (int)
**用途**：当前符卡数量

## 构造方法

### 1. Player()
**用途**：默认构造函数
**说明**：创建玩家在(0, 0)位置，速度5.0f/2.0f，大小20

### 2. Player(float spawnX, float spawnY)
**用途**：指定初始位置
**参数**：
- `spawnX` (float)：初始X坐标
- `spawnY` (float)：初始Y坐标

### 3. Player(float x, float y, float speed, float speedSlow, float size)
**用途**：完整参数构造函数
**参数**：
- `x` (float)：初始X坐标
- `y` (float)：初始Y坐标
- `speed` (float)：普通移动速度
- `speedSlow` (float)：低速移动速度
- `size` (float)：玩家大小

## 方法说明

### 坐标系统相关

#### 1. setKeyStateProvider(KeyStateProvider provider)
**用途**：设置按键状态提供者
**参数**：
- `provider` (KeyStateProvider)：按键状态提供者

#### 2. setSharedCoordinateSystem(CoordinateSystem coordinateSystem)
**用途**：设置共享的坐标系统（静态）
**参数**：
- `coordinateSystem` (CoordinateSystem)：坐标系统实例

#### 3. getSharedCoordinateSystem()
**用途**：获取共享的坐标系统（静态）
**返回值**：CoordinateSystem - 坐标系统实例

#### 4. isCoordinateSystemInitialized()
**用途**：检查坐标系统是否已初始化（静态）
**返回值**：boolean - 是否已初始化

#### 5. requireCoordinateSystem()
**用途**：要求坐标系统必须已初始化（静态）
**说明**：如果未初始化则抛出IllegalStateException

#### 6. toScreenCoords(float worldX, float worldY)
**用途**：将游戏坐标转换为屏幕坐标（静态）
**参数**：
- `worldX` (float)：游戏世界X坐标
- `worldY` (float)：游戏世界Y坐标
**返回值**：float[] - 屏幕坐标数组 [x, y]

### 基本属性访问

#### 7. getX()
**用途**：获取X坐标
**返回值**：float - X坐标

#### 8. getY()
**用途**：获取Y坐标
**返回值**：float - Y坐标

#### 9. setX(float x)
**用途**：设置X坐标
**参数**：
- `x` (float)：X坐标

#### 10. setY(float y)
**用途**：设置Y坐标
**参数**：
- `y` (float)：Y坐标

#### 11. getVx()
**用途**：获取X方向速度
**返回值**：float - X方向速度

#### 12. getVy()
**用途**：获取Y方向速度
**返回值**：float - Y方向速度

#### 13. setVx(float vx)
**用途**：设置X方向速度
**参数**：
- `vx` (float)：X方向速度

#### 14. setVy(float vy)
**用途**：设置Y方向速度
**参数**：
- `vy` (float)：Y方向速度

#### 15. getHitboxRadius()
**用途**：获取碰撞判定半径
**返回值**：float - 碰撞判定半径

#### 16. getSize()
**用途**：获取物体大小
**返回值**：float - 物体大小

#### 17. getColor()
**用途**：获取物体颜色
**返回值**：Color - 物体颜色

#### 18. isActive()
**用途**：检查物体是否激活
**返回值**：boolean - 是否激活
**重写**：实现了 `IRenderable` 接口的方法

#### 19. setActive(boolean active)
**用途**：设置物体激活状态
**参数**：
- `active` (boolean)：是否激活

### 速度控制

#### 20. getVelocityX()
**用途**：获取X方向速度
**返回值**：float - X方向速度

#### 21. getVelocityY()
**用途**：获取Y方向速度
**返回值**：float - Y方向速度

#### 22. setVelocityByComponent(int component, float value)
**用途**：设置速度分量
**参数**：
- `component` (int)：分量索引 (0: X, 1: Y)
- `value` (float)：速度值

#### 23. moveOn(float dx, float dy)
**用途**：移动指定距离
**参数**：
- `dx` (float)：X方向移动距离
- `dy` (float)：Y方向移动距离

### 更新和渲染

#### 24. onUpdate()
**用途**：实现每帧的自定义更新逻辑
**说明**：子类可以重写此方法

#### 25. onMove()
**用途**：实现自定义移动逻辑
**说明**：子类可以重写此方法

#### 26. update()
**用途**：更新玩家状态
**说明**：
- 处理重生等待计时
- 处理重生动画
- 边界限制
- 键盘输入处理
- 射击冷却更新
- 无敌时间计时
- 射击逻辑

#### 27. shoot()
**用途**：发射子弹
**说明**：子类可重写此方法实现不同的射击模式

#### 28. render(IRenderer renderer)
**用途**：渲染玩家（IRenderer版本，支持OpenGL）
**参数**：
- `renderer` (IRenderer)：渲染器
**重写**：实现了 `IRenderable` 接口的方法

#### 29. renderOnScreen(IRenderer renderer)
**用途**：在屏幕中渲染玩家
**参数**：
- `renderer` (IRenderer)：渲染器

#### 30. getRenderLayer()
**用途**：获取渲染层级
**返回值**：int - 渲染层级（返回5）
**重写**：实现了 `IRenderable` 接口的方法

### 移动控制

#### 31. moveUp()
**用途**：向上移动

#### 32. moveDown()
**用途**：向下移动

#### 33. moveLeft()
**用途**：向左移动

#### 34. moveRight()
**用途**：向右移动

#### 35. stopVertical()
**用途**：停止垂直移动

#### 36. stopHorizontal()
**用途**：停止水平移动

#### 37. stopAll()
**用途**：停止所有移动

### 射击和模式控制

#### 38. startShooting()
**用途**：开始射击

#### 39. stopShooting()
**用途**：停止射击

#### 40. enterSlowMode()
**用途**：进入低速模式

#### 41. exitSlowMode()
**用途**：退出低速模式

#### 42. setShooting(boolean shooting)
**用途**：设置射击状态
**参数**：
- `shooting` (boolean)：是否射击

#### 43. setSlowMode(boolean slow)
**用途**：设置低速模式
**参数**：
- `slow` (boolean)：是否低速模式

#### 44. setPosition(float x, float y)
**用途**：设置位置
**参数**：
- `x` (float)：X坐标
- `y` (float)：Y坐标

#### 45. getSpeed()
**用途**：获取普通移动速度
**返回值**：float - 移动速度

#### 46. getSpeedSlow()
**用途**：获取低速移动速度
**返回值**：float - 低速移动速度

#### 47. setSpeed(float speed)
**用途**：设置普通移动速度
**参数**：
- `speed` (float)：移动速度

#### 48. setSpeedSlow(float speedSlow)
**用途**：设置低速移动速度
**参数**：
- `speedSlow` (float)：低速移动速度

#### 49. isSlowMode()
**用途**：是否低速模式
**返回值**：boolean - 是否低速模式

### 受击和重生

#### 50. onHit()
**用途**：受击处理
**说明**：玩家中弹后减少生命值，移动到屏幕下方，等待重生

#### 51. reset()
**用途**：重置玩家状态（用于重新开始游戏）
**说明**：
- 重置位置和速度
- 重置射击和模式状态
- 重置生命值和符卡
- 设置无敌时间

#### 52. isInvincible()
**用途**：检查玩家是否处于无敌状态
**返回值**：boolean - 是否无敌

#### 53. getInvincibleTimer()
**用途**：获取无敌计时器剩余帧数
**返回值**：int - 无敌剩余帧数

#### 54. getBulletDamage()
**用途**：获取子弹伤害
**返回值**：int - 子弹伤害

### 任务相关

#### 55. onTaskStart()
**用途**：任务开始时触发的方法
**说明**：用于处理开局对话

#### 56. onTaskEnd()
**用途**：任务结束时触发的方法
**说明**：用于处理boss击破对话和道具掉落

### 生命值系统

#### 57. getMaxLives()
**用途**：获取最大生命值
**返回值**：int - 最大生命值

#### 58. getCurrentLives()
**用途**：获取当前生命值
**返回值**：int - 当前生命值

#### 59. setMaxLives(int maxLives)
**用途**：设置最大生命值
**参数**：
- `maxLives` (int)：最大生命值

#### 60. setCurrentLives(int currentLives)
**用途**：设置当前生命值
**参数**：
- `currentLives` (int)：当前生命值

#### 61. addLives(int amount)
**用途**：增加生命值
**参数**：
- `amount` (int)：增加的生命值

#### 62. loseLives(int amount)
**用途**：减少生命值
**参数**：
- `amount` (int)：减少的生命值

#### 63. isAlive()
**用途**：检查玩家是否存活
**返回值**：boolean - 是否存活

### 符卡机制

#### 64. getMaxSpellCards()
**用途**：获取最大符卡数量
**返回值**：int - 最大符卡数量

#### 65. getCurrentSpellCards()
**用途**：获取当前符卡数量
**返回值**：int - 当前符卡数量

#### 66. setMaxSpellCards(int maxSpellCards)
**用途**：设置最大符卡数量
**参数**：
- `maxSpellCards` (int)：最大符卡数量

#### 67. setCurrentSpellCards(int currentSpellCards)
**用途**：设置当前符卡数量
**参数**：
- `currentSpellCards` (int)：当前符卡数量

#### 68. addSpellCards(int amount)
**用途**：增加符卡
**参数**：
- `amount` (int)：增加的符卡数量

#### 69. useSpellCard()
**用途**：使用符卡
**返回值**：boolean - 是否成功使用符卡

#### 70. hasSpellCards()
**用途**：检查是否有可用符卡
**返回值**：boolean - 是否有可用符卡

## 实现的接口

### IRenderable 接口
- `render(IRenderer renderer)`：渲染对象
- `getRenderLayer()`：获取渲染层级
