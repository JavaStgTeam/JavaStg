# user.spellcard 包

## 功能描述
user.spellcard 包是 JavaStg 游戏引擎的用户自定义符卡包，提供了用户自定义的符卡类。符卡是游戏中的重要元素，通常由Boss使用，具有独特的弹幕模式和视觉效果。

## 包含文件
1. **testSpellcard_1.java**：测试符卡1（无名），继承自 `EnemySpellcard` 类，用于测试符卡功能。
2. **testSpellcard_2.java**：测试符卡2，继承自 `EnemySpellcard` 类，用于测试符卡功能。
3. **testSpellcard_3.java**：测试符卡3，继承自 `EnemySpellcard` 类，用于测试符卡功能。
4. **testSpellcard_4.java**：测试符卡4，继承自 `EnemySpellcard` 类，用于测试符卡功能。
5. **testSpellcard_5.java**：测试符卡5，继承自 `EnemySpellcard` 类，用于测试符卡功能。

## 核心功能
1. **符卡创建**：允许用户创建自定义符卡类，设置符卡的名称、难度、所属Boss、生命值等属性。
2. **符卡逻辑**：实现符卡的开始、结束和更新逻辑，用于控制符卡的行为。
3. **弹幕系统**：支持符卡的弹幕系统，实现不同的弹幕模式和视觉效果。
4. **生命值管理**：管理符卡的生命值，处理符卡的结束逻辑。
5. **状态管理**：管理符卡的状态，如当前帧数、是否激活等。

## 设计理念
user.spellcard 包采用了继承和扩展的设计模式，通过继承游戏引擎提供的 `EnemySpellcard` 基类来实现自定义功能：
- **继承基类**：所有自定义符卡类都继承自 `stg.game.enemy.EnemySpellcard` 类。
- **重写方法**：通过重写基类的方法来实现自定义行为，如 `onStart()`、`onEnd()` 和 `updateLogic()` 方法。
- **模块化设计**：将不同类型的符卡分别放在不同的类中，便于管理和扩展。

这种设计使得用户可以方便地创建和管理自定义符卡，实现不同的弹幕模式和视觉效果。

## 依赖关系
- 依赖 `stg.game.enemy.EnemySpellcard` 类，用于创建自定义符卡。
- 依赖 `stg.game.enemy.Boss` 类，用于符卡的所属关系。

## 使用示例

### 创建自定义符卡
```java
package user.spellcard;

import stg.game.enemy.Boss;
import stg.game.enemy.EnemySpellcard;

public class MySpellcard extends EnemySpellcard {
    
    /**
     * 构造函数
     * @param name 符卡名称
     * @param difficulty 符卡难度
     * @param boss 所属Boss
     * @param hp 符卡生命值
     */
    public MySpellcard(String name, int difficulty, Boss boss, int hp) {
        super(name, difficulty, boss, hp);
    }
    
    /**
     * 符卡开始时触发的方法
     */
    @Override
    protected void onStart() {
        // 符卡开始时的逻辑
        System.out.println("符卡 " + getName() + " 开始");
    }
    
    /**
     * 符卡结束时触发的方法
     */
    @Override
    protected void onEnd() {
        // 符卡结束时的逻辑
        System.out.println("符卡 " + getName() + " 结束");
    }
    
    /**
     * 符卡更新逻辑
     */
    @Override
    protected void updateLogic() {
        // 符卡更新逻辑
        if (getCurrentFrame() % 10 == 0) {
            // 每10帧发射一次子弹
            shoot();
        }
    }
    
    /**
     * 符卡射击逻辑
     */
    private void shoot() {
        // 实现符卡的射击逻辑
        Boss boss = getOwner();
        stg.game.GameWorld gameWorld = boss.getGameWorld();
        if (gameWorld != null) {
            // 创建并添加子弹
            user.bullet.MyBullet bullet = new user.bullet.MyBullet(boss.getX(), boss.getY());
            gameWorld.addEnemyBullet(bullet);
        }
    }
}
```

## 扩展建议
1. **添加更多符卡类型**：可以创建更多不同类型的符卡，如不同弹幕模式、难度的符卡等。
2. **实现复杂弹幕**：可以为符卡添加复杂的弹幕模式，如圆形弹幕、螺旋弹幕、跟踪弹幕等。
3. **添加符卡特效**：可以为符卡添加特效，如符卡开始时的特效、弹幕的视觉效果等。
4. **实现符卡动画**：可以为符卡添加动画效果，如符卡名称的显示动画、弹幕的移动动画等。
5. **添加符卡音效**：可以为符卡添加音效，如符卡开始时的音效、弹幕发射的音效等。
6. **实现符卡难度系统**：可以为符卡添加难度系统，根据难度调整弹幕的数量、速度、密度等。
7. **添加符卡成就**：可以为符卡添加成就系统，如无损伤通过符卡、在特定时间内通过符卡等。

## 关键方法

### testSpellcard 类
- `testSpellcard(Boss boss)`：构造函数，创建测试符卡对象，设置所属Boss。
- `onStart()`：符卡开始时触发的方法（空实现）。
- `onEnd()`：符卡结束时触发的方法（空实现）。
- `updateLogic()`：符卡更新逻辑（空实现）。