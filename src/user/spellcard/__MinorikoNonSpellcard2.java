package user.spellcard;

import java.awt.Color;
import stg.game.enemy.Boss;
import stg.game.enemy.EnemySpellcard;
import user.bullet.SimpleBullet;

public class __MinorikoNonSpellcard2 extends EnemySpellcard {
    private int shootTimer = 0;
    private static final int SHOOT_INTERVAL = 25;
    private float moveDirection = 1.0f;
    private float moveTimer = 0;

    public __MinorikoNonSpellcard2(Boss boss) {
        super("", 3, boss, 2000);
    }

    @Override
    protected void onStart() {
        shootTimer = 0;
        moveTimer = 0;
        moveDirection = 1.0f;
    }

    @Override
    protected void onEnd() {
    }

    @Override
    protected void updateLogic() {
        Boss boss = getBoss();
        
        moveTimer++;
        if (moveTimer >= 60) {
            moveDirection *= -1;
            moveTimer = 0;
        }
        
        boss.setVx(moveDirection * 1.5f);
        
        shootTimer++;
        if (shootTimer >= SHOOT_INTERVAL) {
            shoot();
            shootTimer = 0;
        }
    }

    private void shoot() {
        Boss boss = getBoss();
        float bulletSpeed = 3.5f;
        int bulletCount = 6;
        
        for (int i = 0; i < bulletCount; i++) {
            float angle = (float) Math.PI / 2 + (i - bulletCount / 2) * 0.3f;
            
            SimpleBullet bullet = new SimpleBullet(
                boss.getX(),
                boss.getY(),
                (float) (bulletSpeed * Math.cos(angle)),
                (float) (bulletSpeed * Math.sin(angle)),
                5.0f,
                Color.YELLOW
            );
            
            if (boss.getGameWorld() != null) {
                boss.getGameWorld().addEnemyBullet(bullet);
            }
        }
    }
}
