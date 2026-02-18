package user.spellcard;

import stg.entity.enemy.Boss;
import stg.entity.enemy.EnemySpellcard;

/**
 * 测试符卡5（无名）
 * @since 2026-02-15
 */
public class TestSpellcard_5 extends EnemySpellcard {
    
    /**
     * 构造函数
     * @param boss 所属Boss
     */
    public TestSpellcard_5(Boss boss) {
        super("", 5, boss, 5000, 3000); // 3000帧 = 50秒
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