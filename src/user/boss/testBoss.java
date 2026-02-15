package user.boss;

import java.awt.Color;
import stg.game.enemy.Boss;
import user.spellcard.TestSpellcard_1;
import user.spellcard.TestSpellcard_2;
import user.spellcard.TestSpellcard_3;
import user.spellcard.TestSpellcard_4;
import user.spellcard.TestSpellcard_5;

/**
 * 测试Boss类
 * @since 2026-02-15
 */
public class TestBoss extends Boss {
    
    /**
     * 构造函数
     * @param x 初始X坐标
     * @param y 初始Y坐标
     */
    public TestBoss(float x, float y) {
        super(x, y, 50, Color.RED);
    }
    
    @Override
    protected void initSpellcards() {
        // 添加五个符卡，按照无名->有名->无名的顺序
        addSpellcard(new TestSpellcard_1(this));
        addSpellcard(new TestSpellcard_2(this));
        addSpellcard(new TestSpellcard_3(this));
        addSpellcard(new TestSpellcard_4(this));
        addSpellcard(new TestSpellcard_5(this));
    }
}