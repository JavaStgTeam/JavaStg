package user.spellcard;

import java.awt.Color;
import stg.game.enemy.Boss;
import stg.game.enemy.EnemySpellcard;
import user.bullet.SimpleBullet;

public class __MinorikoSpellcard1 extends EnemySpellcard {
    private int shootTimer = 0;
    private static final int SHOOT_INTERVAL = 20;
    private float angleOffset = 0;

    public __MinorikoSpellcard1(Boss boss) {
        super("秋之符", 2, boss, 2500, 1800);
    }

    @Override
    protected void onStart() {
        shootTimer = 0;
        angleOffset = 0;
    }

    @Override
    protected void onEnd() {
    }

    @Override
    protected void updateLogic() {
        shootTimer++;
        if (shootTimer >= SHOOT_INTERVAL) {
            shoot();
            shootTimer = 0;
            angleOffset += 0.1f;
        }
    }

    private void shoot() {
        Boss boss = getBoss();
        float bulletSpeed = 2.5f;
        int bulletCount = 12;
        
        for (int i = 0; i < bulletCount; i++) {
            float angle = (float) Math.PI / 2 + (i * 2 * (float) Math.PI / bulletCount) + angleOffset;
            
            SimpleBullet bullet = new SimpleBullet(
                boss.getX(),
                boss.getY(),
                (float) (bulletSpeed * Math.cos(angle)),
                (float) (bulletSpeed * Math.sin(angle)),
                6.0f,
                Color.ORANGE
            );
            
            // 暂时注释掉添加子弹的代码，需要先修改GameWorld类支持敌人子弹
            // TODO: 添加敌人子弹到游戏世界
        }
    }
}
