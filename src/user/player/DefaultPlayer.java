package user.player;

import stg.game.player.Player;

/**
 * 默认自机类 - 发射两个主炮，方向竖直向上
 * @since 2026-02-07
 */
public class DefaultPlayer extends Player {
    
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
        
        // 发射两个子弹，方向竖直向上（Y轴正方向）
        System.out.println("发射子弹: 左侧 (" + leftBulletX + ", " + leftBulletY + ")，右侧 (" + rightBulletX + ", " + rightBulletY + ")");
        
        // 注意：这里只是打印日志，实际游戏中应该创建并添加子弹到游戏世界
    }
}