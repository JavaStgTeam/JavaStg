package stg.render;

import stg.service.render.IRenderer;

/**
 * 可渲染接口
 * 游戏中需要被渲染的实体应该实现此接口
 */
public interface IRenderable {
    /**
     * 获取渲染层级
     * @return 渲染层级
     */
    RenderLayer getRenderLayer();
    
    /**
     * 渲染实体
     * @param renderer 渲染器
     */
    void render(IRenderer renderer);
    
    /**
     * 检查实体是否活跃
     * @return 是否活跃，只有活跃的实体才会被渲染
     */
    boolean isActive();
}