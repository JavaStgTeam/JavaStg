# Player 类文档

## 类概述
`Player` 是玩家角色的基类，继承自 `Obj` 类，实现了 `IPlayer` 接口。定义了玩家的基本行为和属性，包括移动、射击、重生、无敌等功能。

## 成员变量

### 1. speed (float)
**用途**：普通移动速度

### 2. speedSlow (float)
**用途**：低速移动速度

### 3. slowMode (boolean)
**用途**：是否处于低速模式
**默认值**：false

### 4. shooting (boolean)
**用途**：是否正在射击
**默认值**：false

### 5. shootCooldown (int)
**用途**：射击冷却时间（帧）
**默认值**：0

### 6. shootInterval (int)
**用途**：射击间隔（帧）
**默认值**：1

### 7. respawnTimer (int)
**用途**：重生计时器
**默认值**：0

### 8. respawnTime (int)
**用途**：重生等待时间（帧数）
**默认值**：60

### 9. spawnX (float)
**用途**：重生X坐标

### 10. spawnY (float)
**用途**：重生Y坐标

### 11. respawning (boolean)
**用途**：重生动画标志
**默认值**：false

### 12. respawnSpeed (float)
**用途**：重生移动速度
**默认值**：8.0f

### 13. invincibleTimer (int)
**用途**：无敌时间计时器（帧数）
**默认值**：0

### 14. invincibleTime (int)
**用途**：无敌时间（帧数）
**默认值**：120（2秒）

### 15. bulletDamage (int)
**用途**：子弹伤害
**默认值**：2

### 16. options (List<Option>)
**用途**：子机列表

## 构造方法

### 1. Player()
**用途**：创建玩家对象，使用默认参数
**默认值**：
- 位置：(0, 0)
- 普通移动速度：5.0f
- 低速移动速度：2.0f
- 大小：20
- 游戏画布：null

### 2. Player(float spawnX, float spawnY)
**用途**：创建玩家对象，指定重生位置
**参数**：
- `spawnX` (float)：重生X坐标
- `spawnY` (float)：重生Y坐标
**默认值**：
- 普通移动速度：5.0f
- 低速移动速度：2.0f
- 大小：20
- 游戏画布：null

### 3. Player(float x, float y, float speed, float speedSlow, float size, GameCanvas gameCanvas)
**用途**：创建玩家对象，使用完整参数
**参数**：
- `x` (float)：初始X坐标
- `y` (float)：初始Y坐标
- `speed` (float)：普通移动速度
- `speedSlow` (float)：低速移动速度
- `size` (float)：玩家大小
- `gameCanvas` (GameCanvas)：游戏画布引用

## 方法说明

### 1. onUpdate()
**用途**：实现每帧的自定义更新逻辑
**参数**：无
**返回值**：无
**说明**：子类可以重写此方法实现每帧的自定义更新逻辑

### 2. onMove()
**用途**：实现自定义移动逻辑
**参数**：无
**返回值**：无
**说明**：子类可以重写此方法实现自定义移动逻辑

### 3. update()
**用途**：更新玩家状态
**参数**：无
**返回值**：无
**重写**：重写了 `Obj` 类的方法
**说明**：
- 处理重生等待和重生动画
- 更新玩家位置和边界限制
- 处理射击逻辑
- 更新子机状态

### 4. shoot()
**用途**：发射子弹
**参数**：无
**返回值**：无
**说明**：
- 向上发射子弹（Y正方向）
- 子类可重写此方法实现不同的射击模式

### 5. render(Graphics2D g)
**用途**：渲染玩家
**参数**：
- `g` (Graphics2D)：用于绘制的图形上下文对象
**返回值**：无
**重写**：重写了 `Obj` 类的方法

### 6. moveUp()
**用途**：向上移动（Y正方向）
**参数**：无
**返回值**：无

### 7. moveDown()
**用途**：向下移动（Y负方向）
**参数**：无
**返回值**：无

### 8. moveLeft()
**用途**：向左移动
**参数**：无
**返回值**：无

### 9. moveRight()
**用途**：向右移动
**参数**：无
**返回值**：无

### 10. stopVertical()
**用途**：停止垂直移动
**参数**：无
**返回值**：无

### 11. stopHorizontal()
**用途**：停止水平移动
**参数**：无
**返回值**：无

### 12. setShooting(boolean shooting)
**用途**：设置射击状态
**参数**：
- `shooting` (boolean)：是否射击
**返回值**：无

### 13. setSlowMode(boolean slow)
**用途**：设置低速模式
**参数**：
- `slow` (boolean)：是否低速模式
**返回值**：无

### 14. setPosition(float x, float y)
**用途**：设置位置
**参数**：
- `x` (float)：X坐标
- `y` (float)：Y坐标
**返回值**：无
**重写**：重写了 `Obj` 类的方法
**说明**：保存初始位置用于重生

### 15. getSpeed()
**用途**：获取普通移动速度
**参数**：无
**返回值**：float - 普通移动速度

### 16. getSpeedSlow()
**用途**：获取低速移动速度
**参数**：无
**返回值**：float - 低速移动速度

### 17. setSpeed(float speed)
**用途**：设置普通移动速度
**参数**：
- `speed` (float)：普通移动速度
**返回值**：无

### 18. setSpeedSlow(float speedSlow)
**用途**：设置低速移动速度
**参数**：
- `speedSlow` (float)：低速移动速度
**返回值**：无

### 19. setShootInterval(int interval)
**用途**：设置射击间隔
**参数**：
- `interval` (int)：射击间隔（帧数）
**返回值**：无

### 20. isSlowMode()
**用途**：检查是否处于低速模式
**参数**：无
**返回值**：boolean - 是否处于低速模式

### 21. onHit()
**用途**：受击处理
**参数**：无
**返回值**：无
**说明**：
- 玩家中弹后立即移到屏幕下方，然后等待重生

### 22. reset()
**用途**：重置玩家状态（用于重新开始游戏）
**参数**：无
**返回值**：无
**重写**：重写了 `Obj` 类的方法

### 23. isInvincible()
**用途**：检查玩家是否处于无敌状态
**参数**：无
**返回值**：boolean - 是否无敌

### 24. getInvincibleTimer()
**用途**：获取无敌计时器剩余帧数
**参数**：无
**返回值**：int - 无敌剩余帧数

### 25. setInvincibleTime(int frames)
**用途**：设置无敌时间
**参数**：
- `frames` (int)：无敌帧数
**返回值**：无

### 26. addOption(Option option)
**用途**：添加子机
**参数**：
- `option` (Option)：子机对象
**返回值**：无

### 27. removeOption(Option option)
**用途**：移除子机
**参数**：
- `option` (Option)：子机对象
**返回值**：无

### 28. getOptions()
**用途**：获取子机列表
**参数**：无
**返回值**：List<Option> - 子机列表（不可修改）

### 29. clearOptions()
**用途**：清除所有子机
**参数**：无
**返回值**：无

### 30. getBulletDamage()
**用途**：获取子弹伤害
**参数**：无
**返回值**：int - 子弹伤害值

### 31. setBulletDamage(int bulletDamage)
**用途**：设置子弹伤害
**参数**：
- `bulletDamage` (int)：子弹伤害值
**返回值**：无

### 32. onTaskStart()
**用途**：任务开始时触发的方法，用于处理开局对话等
**参数**：无
**返回值**：无

### 33. onTaskEnd()
**用途**：任务结束时触发的方法，用于处理boss击破对话和道具掉落
**参数**：无
**返回值**：无

## 继承的方法

### 从 Obj 类继承
- `getX()`：获取X坐标
- `getY()`：获取Y坐标
- `getVx()`：获取X方向速度
- `getVy()`：获取Y方向速度
- `setVx(float vx)`：设置X方向速度
- `setVy(float vy)`：设置Y方向速度
- `getSize()`：获取大小
- `setSize(float size)`：设置大小
- `getColor()`：获取颜色
- `setColor(Color color)`：设置颜色
- `isActive()`：检查是否活跃
- `setActive(boolean active)`：设置是否活跃
- `getHitboxRadius()`：获取碰撞检测半径
- `setHitboxRadius(float hitboxRadius)`：设置碰撞检测半径
- `reset()`：重置对象状态
- `toScreenCoords(float x, float y)`：将游戏坐标转换为屏幕坐标
- `getGameCanvas()`：获取游戏画布
- `setGameCanvas(GameCanvas gameCanvas)`：设置游戏画布
- `isOutOfBounds(int canvasWidth, int canvasHeight)`：检查对象是否超出边界
- `moveOn(float vx, float vy)`：移动对象
- `setVelocityByComponent(float vx, float vy)`：设置速度分量
- `getVelocityX()`：获取X方向速度
- `getVelocityY()`：获取Y方向速度

### 从 IGameObject 接口继承
- `update()`：更新对象状态
- `render(Graphics2D g)`：渲染对象
- `isActive()`：检查对象是否活跃
- `getX()`：获取对象X坐标
- `getY()`：获取对象Y坐标
- `getSize()`：获取对象大小
- `getHitboxRadius()`：获取对象碰撞检测半径
- `setActive(boolean active)`：设置对象是否活跃

### 从 IPlayer 接口继承
- `moveUp()`：向上移动
- `moveDown()`：向下移动
- `moveLeft()`：向左移动
- `moveRight()`：向右移动
- `stopHorizontal()`：停止水平移动
- `stopVertical()`：停止垂直移动
- `shoot()`：射击
- `setShooting(boolean shooting)`：设置是否射击
- `setSlowMode(boolean slow)`：设置是否低速模式
- `isSlowMode()`：检查是否处于低速模式
- `isInvincible()`：检查是否无敌
- `onHit()`：被击中
- `reset()`：重置玩家状态
- `getOptions()`：获取子机列表
- `addOption(Option option)`：添加子机
- `getShootInterval()`：获取射击间隔
- `setShootInterval(int interval)`：设置射击间隔
- `getBulletDamage()`：获取子弹伤害
- `setBulletDamage(int damage)`：设置子弹伤害