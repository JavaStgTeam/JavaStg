# Stage 类文档

## 类概述
`Stage` 是关卡类，管理单个关卡的逻辑。包括关卡状态管理、敌人管理、波次更新等功能。

## 成员变量

### 1. stageName (String)
**用途**：关卡名称

### 2. stageId (int)
**用途**：关卡ID

### 3. state (State)
**用途**：关卡状态（CREATED/LOADED/STARTED/COMPLETED/CLEANED_UP）

### 4. gameWorld (GameWorld)
**用途**：游戏世界引用

### 5. completionCondition (StageCompletionCondition)
**用途**：关卡完成条件

### 6. currentFrame (int)
**用途**：当前帧数

## 枚举类型

### State
关卡状态枚举：
- `CREATED`：已创建
- `LOADED`：已加载
- `STARTED`：已开始
- `COMPLETED`：已完成
- `CLEANED_UP`：已清理

## 构造方法

### 1. Stage(int stageId, String stageName, GameWorld gameWorld)
**用途**：创建关卡对象
**参数**：
- `stageId` (int)：关卡ID
- `stageName` (String)：关卡名称
- `gameWorld` (GameWorld)：游戏世界引用
**说明**：
- 初始化关卡状态为CREATED
- 调用initStage()初始化关卡

## 方法说明

### 1. initStage()
**用途**：初始化关卡
**参数**：无
**返回值**：无
**说明**：
- 由子类重写，初始化关卡相关资源

### 2. start()
**用途**：开始关卡
**参数**：无
**返回值**：无
**说明**：
- 由调用者显式调用
- 如果状态为LOADED，则设置为STARTED并调用onStageStart()

### 3. end()
**用途**：结束关卡
**参数**：无
**返回值**：无
**说明**：
- 由调用者显式调用
- 如果状态为STARTED，则设置为COMPLETED并调用onStageEnd()

### 4. isActive()
**用途**：检查关卡是否激活
**参数**：无
**返回值**：boolean - 是否激活（状态为STARTED）

### 5. nextStage()
**用途**：跳转到下一关卡
**参数**：无
**返回值**：Stage - 下一关的Stage对象
**说明**：抽象方法，子类必须实现

### 6. load()
**用途**：加载关卡
**参数**：无
**返回值**：无
**说明**：抽象方法，子类必须实现

### 7. setLoaded()
**用途**：设置关卡状态为LOADED
**参数**：无
**返回值**：无
**说明**：
- 由子类在load()方法完成后调用
- 标记关卡为已加载

### 8. cleanup()
**用途**：清理关卡资源
**参数**：无
**返回值**：无
**说明**：
- 敌人清理逻辑由GameWorld负责
- 设置状态为CLEANED_UP

### 9. addEnemy(Enemy enemy)
**用途**：添加敌人到关卡
**参数**：
- `enemy` (Enemy)：敌人对象
**返回值**：无
**说明**：
- 设置敌人的游戏世界引用
- 添加敌人到游戏世界

### 10. removeEnemy(Enemy enemy)
**用途**：移除敌人
**参数**：
- `enemy` (Enemy)：敌人对象
**返回值**：无
**说明**：敌人移除逻辑由GameWorld负责

### 11. isCompleted()
**用途**：检查关卡是否完成
**参数**：无
**返回值**：boolean - 是否完成（状态为COMPLETED）

### 12. isStarted()
**用途**：检查关卡是否已开始
**参数**：无
**返回值**：boolean - 是否已开始（状态为STARTED）

### 13. getStageName()
**用途**：获取关卡名称
**参数**：无
**返回值**：String - 关卡名称

### 14. getStageId()
**用途**：获取关卡ID
**参数**：无
**返回值**：int - 关卡ID

### 15. getEnemies()
**用途**：获取当前关卡的敌人列表
**参数**：无
**返回值**：List<Enemy> - 敌人列表（不可修改）

### 16. getGameWorld()
**用途**：获取游戏世界
**参数**：无
**返回值**：GameWorld - 游戏世界

### 17. onStageStart()
**用途**：关卡开始时调用
**参数**：无
**返回值**：无
**说明**：子类可以重写此方法处理关卡开始逻辑

### 18. onStageEnd()
**用途**：关卡结束时调用
**参数**：无
**返回值**：无
**说明**：子类可以重写此方法处理关卡结束逻辑

### 19. setCompletionCondition(StageCompletionCondition condition)
**用途**：设置关卡完成条件
**参数**：
- `condition` (StageCompletionCondition)：关卡完成条件
**返回值**：无

### 20. checkCompletion()
**用途**：检查关卡完成条件
**参数**：无
**返回值**：无
**说明**：
- 如果设置了完成条件且条件满足，则调用end()

### 21. update()
**用途**：更新关卡逻辑
**参数**：无
**返回值**：无
**说明**：
- 如果关卡激活，增加currentFrame并调用updateWaveLogic()
- 检查完成条件

### 22. updateWaveLogic()
**用途**：更新波次逻辑
**参数**：无
**返回值**：无
**说明**：
- 子类可以重写此方法实现具体的波次管理

### 23. getCurrentFrame()
**用途**：获取当前帧数
**参数**：无
**返回值**：int - 当前帧数

### 24. reset()
**用途**：重置关卡
**参数**：无
**返回值**：无
**说明**：
- 设置状态为CREATED
- 调用initStage()重新初始化
