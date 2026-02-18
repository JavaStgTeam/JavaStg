package stg.event;

import stg.entity.bullet.IBullet;
import stg.entity.player.IPlayer;

/**
 * 子弹发射事件
 * 当子弹被发射时触发
 */
public class BulletFiredEvent {
    private final IBullet bullet;
    private final IPlayer player;
    
    /**
     * 构造函数
     */
    public BulletFiredEvent(IBullet bullet, IPlayer player) {
        this.bullet = bullet;
        this.player = player;
    }
    
    /**
     * 获取子弹
     */
    public IBullet getBullet() {
        return bullet;
    }
    
    /**
     * 获取玩家
     */
    public IPlayer getPlayer() {
        return player;
    }
}
