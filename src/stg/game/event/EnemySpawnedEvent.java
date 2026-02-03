package stg.game.event;

import stg.game.enemy.IEnemy;

/**
 * 敌人生成事件 - 当敌人被生成时触�? */
public class EnemySpawnedEvent {
    private final IEnemy enemy;
    
    /**
     * 构造函�?     */
    public EnemySpawnedEvent(IEnemy enemy) {
        this.enemy = enemy;
    }
    
    /**
     * 获取敌人
     */
    public IEnemy getEnemy() {
        return enemy;
    }
}
