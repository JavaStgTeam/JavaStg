# Stage 类文档

## 类概述
`Stage` 是所有关卡的基类，定义了关卡的基本行为和属性，包括关卡状态管理、敌人管理、波次管理等功能。

## 成员变量

### 1. stageName (String)
**用途**：关卡名称

### 2. stageId (int)
**用途**：关卡ID

### 3. state (StageState)
**用途**：关卡状态
**默认值**：StageState.CREATED

### 4. gameCanvas (GameCanvas)
**用途**：游戏画布引用

### 5. completionCondition (StageCompletionCondition)
**用途**：关卡完成条件

### 6. currentFrame (int)
**用途**：当前帧数（用于波次管理）
**默认值**：0

## 构造方法

### 1. Stage(int stageId, String stageName, GameCanvas gameCanvas)
**用途**：创建关卡对象
**参数**：
- `stageId` (int)：关卡ID
- `stageName` (String)：关卡名称
- `gameCanvas` (GameCanvas)：游戏画布引用

## 方法说明

### 1. initStage()
**用途**：初始化关卡
**参数**：无
**返回值**：无
**说明**：子类可以重写此方法初始化关卡

### 2. start()
**用途**：开始关卡
**参数**：无
**返回值**：无
**说明**：
- 只有当关卡状态为LOADED时才能开始
- 开始后状态变为STARTED
- 调用onStageStart()方法

### 3. end()
**用途**：结束关卡
**参数**：无
**返回值**：无
**说明**：
- 只有当关卡状态为STARTED时才能结束
- 结束后状态变为COMPLETED
- 调用onStageEnd()方法

### 4. isActive()
**用途**：检查关卡是否激活
**参数**：无
**返回值**：boolean - 是否激活（状态为STARTED时返回true）

### 5. nextStage()
**用途**：跳转到下一关
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
**说明**：由子类在load()方法完成后调用

### 8. cleanup()
**用途**：清理关卡资源
**参数**：无
**返回值**：无

### 9. addEnemy(Enemy enemy)
**用途**：添加敌人到关卡
**参数**：
- `enemy` (Enemy)：敌人对象
**返回值**：无

### 10. removeEnemy(Enemy enemy)
**用途**：移除敌人
**参数**：
- `enemy` (Enemy)：敌人对象
**返回值**：无
**说明**：敌人移除逻辑由GameWorld负责

### 11. isCompleted()
**用途**：检查关卡是否完成
**参数**：无
**返回值**：boolean - 是否完成（状态为COMPLETED时返回true）

### 12. isStarted()
**用途**：检查关卡是否已开始
**参数**：无
**返回值**：boolean - 是否已开始（状态为STARTED时返回true）

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

### 16. getGameCanvas()
**用途**：获取游戏画布
**参数**：无
**返回值**：GameCanvas - 游戏画布

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
- 如果设置了完成条件且条件满足，调用end()方法结束关卡

### 21. update()
**用途**：更新关卡逻辑
**参数**：无
**返回值**：无
**说明**：
- 仅在关卡激活时更新
- 增加当前帧数
- 调用updateWaveLogic()方法
- 检查关卡完成条件

### 22. updateWaveLogic()
**用途**：更新波次逻辑
**参数**：无
**返回值**：无
**说明**：子类可以重写此方法实现具体的波次管理

### 23. getCurrentFrame()
**用途**：获取当前帧数
**参数**：无
**返回值**：int - 当前帧数

### 24. reset()
**用途**：重置关卡
**参数**：无
**返回值**：无
**说明**：
- 将关卡状态重置为CREATED
- 调用initStage()方法