package user.enemy;

import java.awt.Color;
import stg.entity.enemy.Enemy;
import user.bullet.SimpleBullet;

public class __MidFairyEnemy extends Enemy {
    private static final float MID_FAIRY_SIZE = 30.0f;
    private static final Color MID_FAIRY_COLOR = new Color(255, 150, 150);
    private static final int MID_FAIRY_HP = 300;
    private static final float MID_FAIRY_SPEED = 2.0f;
    private static final int SHOOT_INTERVAL = 45;
    private int shootTimer = 0;
    private float moveDirection;

    public __MidFairyEnemy(float x, float y) {
        super(x, y, 0, MID_FAIRY_SPEED, MID_FAIRY_SIZE, MID_FAIRY_COLOR, MID_FAIRY_HP);
        this.moveDirection = 1.0f;
    }

    @Override
    public void update(int canvasWidth, int canvasHeight) {
        float x = getX();
        float vx = getVx();
        
        float leftBound = -canvasWidth / 2.0f + getSize();
        float rightBound = canvasWidth / 2.0f - getSize();
        
        if (x <= leftBound && vx < 0) {
            setVx(2.0f);
            setX(leftBound);
        } else if (x >= rightBound && vx > 0) {
            setVx(-2.0f);
            setX(rightBound);
        }
        
        super.update(canvasWidth, canvasHeight);
        
        if (getGameWorld() != null) {
            shootTimer++;
            if (shootTimer >= SHOOT_INTERVAL) {
                shoot();
                shootTimer = 0;
            }
        }
    }

    private void shoot() {
        float bulletSpeed = 3.0f;
        int bulletCount = 5;
        
        for (int i = 0; i < bulletCount; i++) {
            float angle = (float) Math.PI / 2 + (i - bulletCount / 2) * 0.2f;
            
            SimpleBullet bullet = new SimpleBullet(
                getX(),
                getY(),
                (float) (bulletSpeed * Math.cos(angle)),
                (float) (bulletSpeed * Math.sin(angle)),
                6.0f,
                Color.ORANGE
            );
            
            if (getGameWorld() != null) {
                getGameWorld().addEnemyBullet(bullet);
            }
        }
    }

    @Override
    protected void onTaskStart() {
    }

    @Override
    protected void onTaskEnd() {
    }
}
