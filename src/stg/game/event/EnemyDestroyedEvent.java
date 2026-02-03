package stg.game.event;

import stg.game.enemy.IEnemy;

/**
 * 敌人被销毁事�?- 当敌人被销毁时触发
 */
public class EnemyDestroyedEvent {
    private final IEnemy enemy;
    private final int score;
    
    /**
     * 构造函�?     */
    public EnemyDestroyedEvent(IEnemy enemy, int score) {
        this.enemy = enemy;
        this.score = score;
    }
    
    /**
     * 获取敌人
     */
    public IEnemy getEnemy() {
        return enemy;
    }
    
    /**
     * 获取得分
     */
    public int getScore() {
        return score;
    }
}
