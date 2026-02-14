package user.boss;

import java.awt.Color;
import stg.game.enemy.Boss;
import user.spellcard.testSpellcard_1;
import user.spellcard.testSpellcard_2;
import user.spellcard.testSpellcard_3;
import user.spellcard.testSpellcard_4;
import user.spellcard.testSpellcard_5;

/**
 * 测试Boss类
 */
public class testBoss extends Boss {
    
    /**
     * 构造函数
     * @param x 初始X坐标
     * @param y 初始Y坐标
     */
    public testBoss(float x, float y) {
        super(x, y, 50, Color.RED);
    }
    
    @Override
    protected void initSpellcards() {
        // 添加五个符卡，按照无名->有名->无名的顺序
        addSpellcard(new testSpellcard_1(this));
        addSpellcard(new testSpellcard_2(this));
        addSpellcard(new testSpellcard_3(this));
        addSpellcard(new testSpellcard_4(this));
        addSpellcard(new testSpellcard_5(this));
    }
}