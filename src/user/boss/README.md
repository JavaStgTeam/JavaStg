# user.boss 包

## 功能描述
user.boss 包是 JavaStg 游戏引擎的用户自定义Boss包，提供了用户自定义的Boss类。Boss是游戏中的重要敌人角色，通常具有较高的生命值、复杂的攻击模式和多个符卡。

## 包含文件
1. **testBoss.java**：测试Boss类，继承自 `Boss` 类，用于测试Boss功能，包含五个符卡。

## 核心功能
1. **Boss创建**：允许用户创建自定义Boss类，设置Boss的初始位置、生命值、颜色等属性。
2. **符卡管理**：实现Boss的符卡系统，支持添加多个符卡，按照顺序切换符卡。
3. **攻击模式**：支持Boss的复杂攻击模式，通过符卡实现不同的弹幕效果。
4. **状态管理**：管理Boss的状态，如当前符卡、生命值、位置等。

## 设计理念
user.boss 包采用了继承和扩展的设计模式，通过继承游戏引擎提供的 `Boss` 基类来实现自定义功能：
- **继承基类**：所有自定义Boss类都继承自 `stg.game.enemy.Boss` 类。
- **重写方法**：通过重写基类的方法来实现自定义行为，如 `initSpellcards()` 方法用于初始化符卡。
- **模块化设计**：将不同的符卡分别放在 `user.spellcard` 包中，便于管理和扩展。

这种设计使得用户可以方便地创建和管理自定义Boss，实现复杂的Boss战。

## 依赖关系
- 依赖 `stg.game.enemy.Boss` 类，用于创建自定义Boss。
- 依赖 `user.spellcard` 包，用于创建和管理符卡。

## 使用示例

### 创建自定义Boss
```java
package user.boss;

import java.awt.Color;
import stg.game.enemy.Boss;
import user.spellcard.MySpellcard1;
import user.spellcard.MySpellcard2;

public class MyBoss extends Boss {
    
    /**
     * 构造函数
     * @param x 初始X坐标
     * @param y 初始Y坐标
     */
    public MyBoss(float x, float y) {
        super(x, y, 1000, Color.RED); // 设置生命值为1000，颜色为红色
    }
    
    @Override
    protected void initSpellcards() {
        // 添加符卡
        addSpellcard(new MySpellcard1(this));
        addSpellcard(new MySpellcard2(this));
    }
    
    @Override
    protected void onUpdate() {
        // 实现自定义更新逻辑
        super.onUpdate();
        
        // 自定义移动逻辑
        setVx(0.5f); // 向右移动
        
        // 自定义攻击逻辑
        if (getCurrentFrame() % 120 == 0) {
            nextSpellcard();
        }
    }
}
```

## 扩展建议
1. **添加更多Boss类型**：可以创建更多不同类型的Boss，如不同行为、不同攻击模式的Boss等。
2. **实现Boss阶段系统**：可以为Boss添加多个阶段，每个阶段有不同的行为和攻击模式。
3. **添加Boss动画**：可以为Boss添加动画效果，如移动动画、攻击动画等。
4. **实现Boss对话系统**：可以为Boss添加对话系统，在战斗开始、符卡切换等时刻显示对话。
5. **添加Boss特效**：可以为Boss添加特效，如出场特效、死亡特效等。

## 关键方法

### testBoss 类
- `testBoss(float x, float y)`：构造函数，创建测试Boss对象，设置初始位置。
- `initSpellcards()`：初始化符卡（重写父类方法）。