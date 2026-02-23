package user.player;

import java.awt.Color;
import stg.entity.base.Obj;
import stg.entity.player.Player;

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
        System.out.println("DefaultPlayer shoot");
    }
}