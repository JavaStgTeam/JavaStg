package stg.game;

import java.awt.Graphics2D;

/**
 * 游戏对象接口 - 所有游戏对象的基础接口
 */
public interface IGameObject {
    /**
     * 更新对象状态
     */
    void update();
    
    /**
     * 渲染对象
     */
    void render(Graphics2D g);
    
    /**
     * 检查对象是否活跃
     */
    boolean isActive();
    
    /**
     * 获取X坐标
     */
    float getX();
    
    /**
     * 获取Y坐标
     */
    float getY();
    
    /**
     * 获取对象大小
     */
    float getSize();
    
    /**
     * 获取碰撞检测半径
     */
    float getHitboxRadius();
    
    /**
     * 设置对象是否活跃
     */
    void setActive(boolean active);
}
