package user.spellcard;

import stg.game.enemy.Boss;
import stg.game.enemy.EnemySpellcard;

/**
 * 测试符卡3（无名）
 * @since 2026-02-15
 */
public class TestSpellcard_3 extends EnemySpellcard {
    
    /**
     * 构造函数
     * @param boss 所属Boss
     */
    public TestSpellcard_3(Boss boss) {
        super("", 3, boss, 4000, 3000); // 3000帧 = 50秒
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