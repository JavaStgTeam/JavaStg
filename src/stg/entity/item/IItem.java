package stg.entity.item;

import stg.core.IGameObject;

/**
 * 物品接口
 * 定义物品的行为和属性
 * 
 * @author JavaSTG Team
 * @since 2026-02-17
 */
public interface IItem extends IGameObject {
    /**
     * 物品被收集
     */
    void onCollect();
    
    /**
     * 应用吸引力效果
     */
    void applyAttraction();
    
    /**
     * 检查物品是否越界
     */
    boolean isOutOfBounds(int width, int height);
    
    /**
     * 获取物品类型
     */
    String getType();
}
