# Boss 类文档

## 类概述
`Boss` 是所有Boss的基类，继承自 `Enemy` 类，实现了 `IBoss` 和 `IRenderable` 接口。管理Boss的入场、退场和符卡系统。

## 成员变量

### 1. spellcards (List<ISpellcard>)
**用途**：Boss的符卡列表

### 2. currentSpellcard (ISpellcard)
**用途**：当前激活的符卡

### 3. currentPhase (int)
**用途**：当前阶段

### 4. maxPhase (int)
**用途**：最大阶段数

### 5. state (BossState)
**用途**：Boss当前状态（ENTERING/ACTIVE/EXITING）

### 6. enterFrameCount (int)
**用途**：入场动画帧数计数

### 7. exitFrameCount (int)
**用途**：退场动画帧数计数

### 8. ENTER_DURATION (int)
**用途**：入场动画持续帧数
**默认值**：120

### 9. EXIT_DURATION (int)
**用途**：退场动画持续帧数
**默认值**：90

### 10. spriteTextureId (int)
**用途**：精灵图纹理ID
**默认值**：-1（表示无精灵图）

### 11. spriteX (float)
**用途**：精灵素材X坐标

### 12. spriteY (float)
**用途**：精灵素材Y坐标

### 13. spriteWidth (float)
**用途**：精灵素材宽度

### 14. spriteHeight (float)
**用途**：精灵素材高度

### 15. imgWidth (float)
**用途**：图片总宽度

### 16. imgHeight (float)
**用途**：图片总高度

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
- 生命值默认：100

## 方法说明

### 1. initSpellcards()
**用途**：初始化符卡列表
**参数**：无
**返回值**：无
**说明**：抽象方法，子类必须实现，添加所有符卡

### 2. start()
**用途**：开始Boss，初始化符卡
**参数**：无
**返回值**：无
**重写**：实现了 `IBoss` 接口的方法

### 3. update()
**用途**：更新Boss状态
**参数**：无
**返回值**：无
**重写**：重写了 `Enemy` 类的方法
**说明**：根据当前状态（入场/活跃/退场）调用对应的更新逻辑

### 4. render(IRenderer renderer)
**用途**：渲染Boss
**参数**：
- `renderer` (IRenderer)：渲染器
**返回值**：无
**重写**：实现了 `IRenderable` 接口的方法

### 5. getRenderLayer()
**用途**：获取渲染层级
**参数**：无
**返回值**：int - 渲染层级（返回4，低于玩家的5）
**重写**：实现了 `IRenderable` 接口的方法

### 6. renderOnScreen(IRenderer renderer)
**用途**：在屏幕中渲染Boss
**参数**：
- `renderer` (IRenderer)：渲染器
**返回值**：无

### 7. takeDamage(int damage)
**用途**：使Boss承受伤害，传递给当前符卡
**参数**：
- `damage` (int)：Boss承受的伤害值
**返回值**：无
**重写**：重写了 `Enemy` 类的方法

### 8. getState()
**用途**：获取Boss状态
**参数**：无
**返回值**：BossState - Boss当前状态
**重写**：实现了 `IBoss` 接口的方法

### 9. loadSprite(String path, float x, float y, float width, float height)
**用途**：加载精灵图资源
**参数**：
- `path` (String)：图片路径
- `x` (float)：素材X坐标
- `y` (float)：素材Y坐标
- `width` (float)：素材宽度
- `height` (float)：素材高度
**返回值**：无
**重写**：实现了 `IBoss` 接口的方法

### 10. updateEnterLogic()
**用途**：更新入场逻辑
**参数**：无
**返回值**：无
**说明**：处理入场动画，从屏幕上方移动到指定位置

### 11. updateActiveLogic()
**用途**：更新活跃逻辑
**参数**：无
**返回值**：无
**说明**：更新当前符卡，检查符卡是否被击败

### 12. updateExitLogic()
**用途**：更新退场逻辑
**参数**：无
**返回值**：无
**说明**：处理退场动画，向屏幕上方移动

### 13. startNextSpellcard()
**用途**：开始下一个符卡
**参数**：无
**返回值**：无

### 14. addSpellcard(ISpellcard spellcard)
**用途**：添加符卡
**参数**：
- `spellcard` (ISpellcard)：符卡对象
**返回值**：无

### 15. renderHealthBar(IRenderer renderer)
**用途**：渲染Boss的生命值条，显示当前符卡的生命值
**参数**：
- `renderer` (IRenderer)：渲染器
**返回值**：无

### 16. getCurrentSpellcard()
**用途**：获取当前符卡
**参数**：无
**返回值**：ISpellcard - 当前符卡
**重写**：实现了 `IBoss` 接口的方法

### 17. getCurrentPhase()
**用途**：获取当前阶段
**参数**：无
**返回值**：int - 当前阶段

### 18. getMaxPhase()
**用途**：获取最大阶段
**参数**：无
**返回值**：int - 最大阶段

### 19. getGameWorld()
**用途**：获取游戏世界引用
**参数**：无
**返回值**：GameWorld - 游戏世界引用
**重写**：重写了 `Enemy` 类的方法

### 20. setVx(float vx)
**用途**：设置X方向速度
**参数**：
- `vx` (float)：X方向速度
**返回值**：无
**重写**：重写了 `Enemy` 类的方法

## 继承的方法

### 从 Enemy 类继承
- `hp`：生命值（由符卡管理）
- `maxHp`：最大生命值（由符卡管理）
- `gameWorld`：游戏世界引用
- `render(Graphics2D g)`：渲染敌人
- `render(IRenderer renderer)`：渲染敌人（OpenGL版本）
- `isOutOfBounds(int canvasWidth, int canvasHeight)`：检查是否越界
- `isAlive()`：检查是否存活
- `takeDamage(int damage)`：承受伤害
- `update()`：更新状态
- `resetState()`：重置状态（对象池）

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
