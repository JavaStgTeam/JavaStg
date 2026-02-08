package stg.game.enemy;

import stg.game.IGameObject;

/**
 * 敌人接口
 * 定义敌人的行为和属性
 */
public interface IEnemy extends IGameObject {
    /**
     * 承受伤害
     */
    void takeDamage(int damage);
    
    /**
     * 检查敌人是否存活
     */
    boolean isAlive();
    
    /**
     * 获取当前生命值
     */
    int getHp();
    
    /**
     * 获取最大生命值
     */
    int getMaxHp();
    
    /**
     * 设置生命值
     */
    void setHp(int hp);
    
    /**
     * 检查敌人是否越界
     */
    boolean isOutOfBounds(int width, int height);
    
    /**
     * 获取敌人类型
     */
    String getType();
    
    /**
     * 设置敌人速度
     */
    void setSpeed(float speed);
    
    /**
     * 获取敌人速度
     */
    float getSpeed();
}
