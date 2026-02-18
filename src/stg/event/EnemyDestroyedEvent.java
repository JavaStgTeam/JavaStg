package stg.event;

import stg.entity.enemy.IEnemy;

/**
 * 敌人被销毁事件
 * 当敌人被销毁时触发
 */
public class EnemyDestroyedEvent {
    private final IEnemy enemy;
    private final int score;
    
    /**
     * 构造函数
     */
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
