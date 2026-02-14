package user.spellcard;

import stg.game.enemy.Boss;
import stg.game.enemy.EnemySpellcard;

/**
 * 测试符卡4（有名）
 */
public class testSpellcard_4 extends EnemySpellcard {
    
    /**
     * 构造函数
     * @param boss 所属Boss
     */
    public testSpellcard_4(Boss boss) {
        super("测试符卡4", 4, boss, 6000);
    }
    
    @Override
    protected void onStart() {
        // 空实现
    }
    
    @Override
    protected void onEnd() {
        // 空实现
    }
    
    @Override
    protected void updateLogic() {
        // 空实现
    }
}