package stg.game.player;

import stg.game.IGameObject;
import user.player.Option;

/**
 * 玩家接口 - 定义玩家的行为和属性
 */
public interface IPlayer extends IGameObject {
    /**
     * 向上移动
     */
    void moveUp();
    
    /**
     * 向下移动
     */
    void moveDown();
    
    /**
     * 向左移动
     */
    void moveLeft();
    
    /**
     * 向右移动
     */
    void moveRight();
    
    /**
     * 停止水平移动
     */
    void stopHorizontal();
    
    /**
     * 停止垂直移动
     */
    void stopVertical();
    
    /**
     * 射击
     */
    void shoot();
    
    /**
     * 设置是否射击
     */
    void setShooting(boolean shooting);
    
    /**
     * 设置是否低速模式
     */
    void setSlowMode(boolean slow);
    
    /**
     * 检查是否处于低速模式
     */
    boolean isSlowMode();
    
    /**
     * 检查是否无敌
     */
    boolean isInvincible();
    
    /**
     * 被击中时调用
     */
    void onHit();
    
    /**
     * 重置玩家状态
     */
    void reset();
    
    /**
     * 获取子机列表
     */
    java.util.List<Option> getOptions();
    
    /**
     * 添加子机
     */
    void addOption(Option option);
    
    /**
     * 获取射击间隔
     */
    int getShootInterval();
    
    /**
     * 设置射击间隔
     */
    void setShootInterval(int interval);
    
    /**
     * 获取子弹伤害
     */
    int getBulletDamage();
    
    /**
     * 设置子弹伤害
     */
    void setBulletDamage(int damage);
}
