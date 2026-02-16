package user.spellcard;

import java.awt.Color;
import stg.game.enemy.Boss;
import stg.game.enemy.EnemySpellcard;
import user.bullet.SimpleBullet;

public class __MinorikoNonSpellcard1 extends EnemySpellcard {
    private int shootTimer = 0;
    private static final int SHOOT_INTERVAL = 30;

    public __MinorikoNonSpellcard1(Boss boss) {
        super("", 1, boss, 2000);
    }

    @Override
    protected void onStart() {
        shootTimer = 0;
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
        }
    }

    private void shoot() {
        Boss boss = getBoss();
        float bulletSpeed = 3.0f;
        int bulletCount = 8;
        
        for (int i = 0; i < bulletCount; i++) {
            float angle = (float) Math.PI / 2 + (i * 2 * (float) Math.PI / bulletCount);
            
            SimpleBullet bullet = new SimpleBullet(
                boss.getX(),
                boss.getY(),
                (float) (bulletSpeed * Math.cos(angle)),
                (float) (bulletSpeed * Math.sin(angle)),
                5.0f,
                Color.RED
            );
            
            if (boss.getGameWorld() != null) {
                boss.getGameWorld().addEnemyBullet(bullet);
            }
        }
    }
}
