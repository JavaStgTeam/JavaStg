package user.player;

import java.awt.Color;
import stg.entity.player.Player;
import user.bullet.DefaultPlayerMainBullet;

/**
 * 默认自机类 - 发射两个主炮，方向竖直向上
 * @since 2026-02-07
 */
public class DefaultPlayer extends Player {
    private static final Color BULLET_COLOR = new Color(255, 255, 255); // 子弹颜色
    private static final float BULLET_SPEED = 48.0f; // 子弹速度
    private static final float BULLET_SIZE = 4.0f; // 子弹大小
    
    public DefaultPlayer() {
        super(0, 0, 5.0f, 2.0f, 20);
    }
    
    public DefaultPlayer(float x, float y) {
        super(x, y, 5.0f, 2.0f, 20);
    }
    
    /**
     * 发射子弹 - 实现发射两个主炮，方向竖直向上
     */
    @Override
    protected void shoot() {
        // 计算两个子弹的发射位置（自机左右两侧）
        float bulletOffset = 10.0f; // 子弹偏移量
        
        // 左侧子弹
        float leftBulletX = getX() - bulletOffset;
        float leftBulletY = getY() + getSize(); // 从自机顶部发射
        
        // 右侧子弹
        float rightBulletX = getX() + bulletOffset;
        float rightBulletY = getY() + getSize(); // 从自机顶部发射
        
        // 获取游戏世界引用
        stg.core.GameWorld gameWorld = getGameWorld();
        if (gameWorld != null) {
            // 创建左侧子弹
            DefaultPlayerMainBullet leftBullet = new DefaultPlayerMainBullet(leftBulletX, leftBulletY);
            leftBullet.setDamage(getBulletDamage());
            gameWorld.addPlayerBullet(leftBullet);
            
            // 创建右侧子弹
            DefaultPlayerMainBullet rightBullet = new DefaultPlayerMainBullet(rightBulletX, rightBulletY);
            rightBullet.setDamage(getBulletDamage());
            gameWorld.addPlayerBullet(rightBullet);
        }
        
        // 打印发射信息
        System.out.println("发射子弹: 左侧 (" + leftBulletX + ", " + leftBulletY + ")，右侧 (" + rightBulletX + ", " + rightBulletY + ")");
    }
}