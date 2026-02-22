package stg.render;

/**
 * 渲染层级枚举
 * 定义游戏中不同类型的渲染层级，用于控制渲染顺序
 */
public enum RenderLayer {
    /**
     * 背景层 - 最底层，通常是静态背景
     */
    BACKGROUND(0),
    
    /**
     * 远景层 - 背景物体，如远处的山、云等
     */
    FAR(1),
    
    /**
     * 中景层 - 游戏世界中的主要实体
     */
    MIDDLE(2),
    
    /**
     * 近景层 - 靠近玩家的物体
     */
    NEAR(3),
    
    /**
     * 玩家层 - 玩家角色
     */
    PLAYER(4),
    
    /**
     * UI层 - 用户界面元素
     */
    UI(5),
    
    /**
     * 顶层 - 最上层的元素，如提示信息、菜单等
     */
    TOP(6);
    
    private final int zIndex;
    
    RenderLayer(int zIndex) {
        this.zIndex = zIndex;
    }
    
    /**
     * 获取层级的Z轴索引
     * @return Z轴索引，值越小渲染越靠后
     */
    public int getZIndex() {
        return zIndex;
    }
    
    /**
     * 根据Z轴索引获取渲染层级
     * @param zIndex Z轴索引
     * @return 对应的渲染层级，如果没有匹配的则返回MIDDLE
     */
    public static RenderLayer fromZIndex(int zIndex) {
        for (RenderLayer layer : values()) {
            if (layer.zIndex == zIndex) {
                return layer;
            }
        }
        return MIDDLE;
    }
}