package user.spellcard;

import stg.game.enemy.Boss;
import stg.game.enemy.EnemySpellcard;

/**
 * 测试符卡4（有名）
 * @since 2026-02-15
 */
public class TestSpellcard_4 extends EnemySpellcard {
    
    /**
     * 构造函数
     * @param boss 所属Boss
     */
    public TestSpellcard_4(Boss boss) {
        super("测试符卡4", 4, boss, 4500, 3600); // 3600帧 = 60秒
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