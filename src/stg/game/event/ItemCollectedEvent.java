package stg.game.event;

import stg.game.item.IItem;
import stg.game.player.IPlayer;

/**
 * 物品被收集事�?- 当物品被收集时触�? */
public class ItemCollectedEvent {
    private final IItem item;
    private final IPlayer player;
    
    /**
     * 构造函�?     */
    public ItemCollectedEvent(IItem item, IPlayer player) {
        this.item = item;
        this.player = player;
    }
    
    /**
     * 获取物品
     */
    public IItem getItem() {
        return item;
    }
    
    /**
     * 获取玩家
     */
    public IPlayer getPlayer() {
        return player;
    }
}
