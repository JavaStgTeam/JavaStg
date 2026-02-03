package stg.game.bullet;

import stg.game.IGameObject;

/**
 * 子弹接口 - 定义子弹的行为和属�? */
public interface IBullet extends IGameObject {
    /**
     * 获取子弹伤害
     */
    int getDamage();
    
    /**
     * 设置子弹伤害
     */
    void setDamage(int damage);
    
    /**
     * 检查子弹是否越�?     */
    boolean isOutOfBounds(int width, int height);
    
    /**
     * 获取子弹速度
     */
    float getSpeed();
    
    /**
     * 设置子弹速度
     */
    void setSpeed(float speed);
    
    /**
     * 获取子弹方向
     */
    float getDirection();
    
    /**
     * 设置子弹方向
     */
    void setDirection(float direction);
}
