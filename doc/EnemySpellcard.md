# EnemySpellcard 类文档

## 类概述
`EnemySpellcard` 是所有敌方符卡的基类。用于定义Boss的攻击模式、阶段管理和生命值管理。支持符卡阶段和非符卡阶段的区分。

## 成员变量

### 1. name (String)
**用途**：符卡名称
**说明**：空字符串表示非符卡阶段，非空字符串表示符卡阶段

### 2. phase (int)
**用途**：对应阶段

### 3. boss (Boss)
**用途**：所属Boss

### 4. active (boolean)
**用途**：是否激活

### 5. duration (int)
**用途**：符卡持续时间（帧数）
**说明**：0表示无时间限制

### 6. currentFrame (int)
**用途**：当前帧数

### 7. hp (int)
**用途**：当前阶段生命值

### 8. maxHp (int)
**用途**：当前阶段最大生命值

## 构造方法

### 1. EnemySpellcard(String name, int phase, Boss boss, int hp)
**用途**：创建符卡对象
**参数**：
- `name` (String)：符卡名称，空字符串表示非符卡阶段
- `phase` (int)：对应阶段
- `boss` (Boss)：所属Boss
- `hp` (int)：阶段生命值
**默认值**：
- 持续时间：0（无时间限制）

### 2. EnemySpellcard(String name, int phase, Boss boss, int hp, int duration)
**用途**：创建符卡对象，指定持续时间
**参数**：
- `name` (String)：符卡名称，空字符串表示非符卡阶段
- `phase` (int)：对应阶段
- `boss` (Boss)：所属Boss
- `hp` (int)：阶段生命值
- `duration` (int)：符卡持续时间（帧数）

## 方法说明

### 1. start()
**用途**：开始符卡
**参数**：无
**返回值**：无

### 2. end()
**用途**：结束符卡
**参数**：无
**返回值**：无

### 3. update()
**用途**：更新符卡逻辑
**参数**：无
**返回值**：无

### 4. onStart()
**用途**：符卡开始时调用
**参数**：无
**返回值**：无
**说明**：抽象方法，子类必须实现

### 5. onEnd()
**用途**：符卡结束时调用
**参数**：无
**返回值**：无
**说明**：抽象方法，子类必须实现

### 6. updateLogic()
**用途**：更新符卡逻辑
**参数**：无
**返回值**：无
**说明**：抽象方法，子类必须实现

### 7. isSpellcardPhase()
**用途**：检查是否为符卡阶段
**参数**：无
**返回值**：boolean - 是否为符卡阶段
**说明**：空字符串名称表示非符卡阶段

### 8. takeDamage(int damage)
**用途**：受到伤害
**参数**：
- `damage` (int)：伤害值
**返回值**：boolean - 是否被击败

### 9. isDefeated()
**用途**：检查是否被击败
**参数**：无
**返回值**：boolean - 是否被击败

### 10. getName()
**用途**：获取符卡名称
**参数**：无
**返回值**：String - 符卡名称

### 11. getPhase()
**用途**：获取对应阶段
**参数**：无
**返回值**：int - 对应阶段

### 12. getBoss()
**用途**：获取所属Boss
**参数**：无
**返回值**：Boss - 所属Boss

### 13. isActive()
**用途**：检查是否激活
**参数**：无
**返回值**：boolean - 是否激活

### 14. getCurrentFrame()
**用途**：获取当前帧数
**参数**：无
**返回值**：int - 当前帧数

### 15. getDuration()
**用途**：获取符卡持续时间
**参数**：无
**返回值**：int - 符卡持续时间

### 16. getHp()
**用途**：获取当前生命值
**参数**：无
**返回值**：int - 当前生命值

### 17. getMaxHp()
**用途**：获取最大生命值
**参数**：无
**返回值**：int - 最大生命值

### 18. setHp(int hp)
**用途**：设置生命值
**参数**：
- `hp` (int)：生命值
**返回值**：无

## 典型用法

### 创建非符卡阶段
```java
// 创建非符卡阶段（空字符串名称）
EnemySpellcard nonSpellcard = new EnemySpellcard("", 1, boss, 1000);
```

### 创建符卡阶段
```java
// 创建符卡阶段，持续600帧
EnemySpellcard spellcard = new EnemySpellcard("Fireball Barrage", 2, boss, 1500, 600);
```

### 实现具体符卡
```java
class FireballBarrage extends EnemySpellcard {
    public FireballBarrage(String name, int phase, Boss boss, int hp, int duration) {
        super(name, phase, boss, hp, duration);
    }
    
    @Override
    protected void onStart() {
        // 符卡开始时的处理
    }
    
    @Override
    protected void onEnd() {
        // 符卡结束时的处理
    }
    
    @Override
    protected void updateLogic() {
        // 符卡的攻击逻辑
    }
}
```
