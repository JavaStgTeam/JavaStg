package user.spellcard;

import stg.entity.enemy.Boss;
import stg.entity.enemy.EnemySpellcard;

/**
 * 测试符卡2（有名）
 * @since 2026-02-15
 */
public class TestSpellcard_2 extends EnemySpellcard {
    
    /**
     * 构造函数
     * @param boss 所属Boss
     */
    public TestSpellcard_2(Boss boss) {
        super("测试符卡2", 2, boss, 5000, 3600); // 3600帧 = 60秒
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