# EnemySpellcard 类文档

## 类概述
`EnemySpellcard` 是敌方符卡基类，用于定义Boss的攻击模式和阶段。实现了 `ISpellcard` 接口，管理符卡的生命周期、生命值和持续时间。

## 成员变量

### 1. name (String)
**用途**：符卡名称，空字符串表示非符卡阶段

### 2. phase (int)
**用途**：对应阶段

### 3. boss (IBoss)
**用途**：所属Boss

### 4. active (boolean)
**用途**：是否激活

### 5. duration (int)
**用途**：符卡持续时间（帧数），0表示无时间限制

### 6. currentFrame (int)
**用途**：当前帧数

### 7. hp (int)
**用途**：当前阶段生命值

### 8. maxHp (int)
**用途**：当前阶段最大生命值

## 构造方法

### 1. EnemySpellcard(String name, int phase, IBoss boss, int hp)
**用途**：创建符卡对象
**参数**：
- `name` (String)：符卡名称，空字符串表示非符卡阶段
- `phase` (int)：对应阶段
- `boss` (IBoss)：所属Boss
- `hp` (int)：阶段生命值

### 2. EnemySpellcard(String name, int phase, IBoss boss, int hp, int duration)
**用途**：创建符卡对象（带持续时间）
**参数**：
- `name` (String)：符卡名称，空字符串表示非符卡阶段
- `phase` (int)：对应阶段
- `boss` (IBoss)：所属Boss
- `hp` (int)：阶段生命值
- `duration` (int)：符卡持续时间（帧数）

## 方法说明

### 1. start(IBoss boss)
**用途**：开始符卡
**参数**：
- `boss` (IBoss)：Boss实例
**返回值**：无
**重写**：实现了 `ISpellcard` 接口的方法
**说明**：
- 设置active为true
- 重置currentFrame为0
- 重置hp为maxHp
- 调用onStart()

### 2. start()
**用途**：开始符卡（兼容旧方法）
**参数**：无
**返回值**：无
**说明**：调用start(this.boss)

### 3. end()
**用途**：结束符卡
**参数**：无
**返回值**：无
**说明**：
- 设置active为false
- 调用onEnd()

### 4. update()
**用途**：更新符卡逻辑
**参数**：无
**返回值**：无
**说明**：
- 如果未激活则直接返回
- 增加currentFrame
- 检查持续时间，如果超过则结束符卡
- 调用updateLogic()

### 5. onStart()
**用途**：符卡开始时调用
**参数**：无
**返回值**：无
**说明**：抽象方法，子类必须实现

### 6. onEnd()
**用途**：符卡结束时调用
**参数**：无
**返回值**：无
**说明**：抽象方法，子类必须实现

### 7. updateLogic()
**用途**：更新符卡逻辑
**参数**：无
**返回值**：无
**说明**：抽象方法，子类必须实现

### 8. isSpellcardPhase()
**用途**：检查是否为符卡阶段
**参数**：无
**返回值**：boolean - 是否为符卡阶段（name不为空）

### 9. getName()
**用途**：获取符卡名称
**参数**：无
**返回值**：String - 符卡名称

### 10. getPhase()
**用途**：获取对应阶段
**参数**：无
**返回值**：int - 对应阶段

### 11. getBoss()
**用途**：获取所属Boss
**参数**：无
**返回值**：IBoss - 所属Boss

### 12. isActive()
**用途**：检查是否激活
**参数**：无
**返回值**：boolean - 是否激活

### 13. getCurrentFrame()
**用途**：获取当前帧数
**参数**：无
**返回值**：int - 当前帧数

### 14. getDuration()
**用途**：获取符卡持续时间
**参数**：无
**返回值**：int - 符卡持续时间

### 15. takeDamage(int damage)
**用途**：受到伤害
**参数**：
- `damage` (int)：伤害值
**返回值**：无
**重写**：实现了 `ISpellcard` 接口的方法
**说明**：减少hp，最小为0

### 16. takeDamageWithReturn(int damage)
**用途**：受到伤害（兼容旧方法）
**参数**：
- `damage` (int)：伤害值
**返回值**：boolean - 是否被击败

### 17. getHp()
**用途**：获取当前生命值
**参数**：无
**返回值**：int - 当前生命值

### 18. getMaxHp()
**用途**：获取最大生命值
**参数**：无
**返回值**：int - 最大生命值

### 19. setHp(int hp)
**用途**：设置生命值
**参数**：
- `hp` (int)：生命值
**返回值**：无

### 20. isDefeated()
**用途**：检查是否被击败
**参数**：无
**返回值**：boolean - 是否被击败（hp <= 0）

## 实现的接口

### ISpellcard 接口
- `start(IBoss boss)`：开始符卡
- `takeDamage(int damage)`：承受伤害
- `getHp()`：获取当前生命值
- `getMaxHp()`：获取最大生命值
- `isDefeated()`：检查是否被击败
- `isSpellcardPhase()`：检查是否为符卡阶段
- `getName()`：获取符卡名称
