package stg.render;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import stg.service.render.IRenderer;

/**
 * 渲染管理器
 * 负责管理游戏中所有可渲染对象，按照层级顺序进行渲染
 */
public class RenderManager {
    private final EnumMap<RenderLayer, List<IRenderable>> renderLayers;
    
    /**
     * 构造函数
     */
    public RenderManager() {
        renderLayers = new EnumMap<>(RenderLayer.class);
        
        // 初始化所有层级的列表
        for (RenderLayer layer : RenderLayer.values()) {
            renderLayers.put(layer, new ArrayList<>());
        }
    }
    
    /**
     * 添加可渲染对象
     * @param renderable 可渲染对象
     */
    public void addRenderable(IRenderable renderable) {
        if (renderable != null) {
            RenderLayer layer = renderable.getRenderLayer();
            renderLayers.get(layer).add(renderable);
        }
    }
    
    /**
     * 移除可渲染对象
     * @param renderable 可渲染对象
     */
    public void removeRenderable(IRenderable renderable) {
        if (renderable != null) {
            RenderLayer layer = renderable.getRenderLayer();
            renderLayers.get(layer).remove(renderable);
        }
    }
    
    /**
     * 清空所有可渲染对象
     */
    public void clearAll() {
        for (List<IRenderable> layerList : renderLayers.values()) {
            layerList.clear();
        }
    }
    
    /**
     * 按照层级顺序渲染所有对象
     * @param renderer 渲染器
     */
    public void renderAll(IRenderer renderer) {
        // 按照层级从低到高的顺序渲染
        for (RenderLayer layer : RenderLayer.values()) {
            List<IRenderable> layerObjects = renderLayers.get(layer);
            
            // 遍历并渲染该层级的所有对象
            for (int i = 0; i < layerObjects.size(); i++) {
                IRenderable renderable = layerObjects.get(i);
                if (renderable != null && renderable.isActive()) {
                    renderable.render(renderer);
                }
            }
        }
    }
    
    /**
     * 获取指定层级的可渲染对象数量
     * @param layer 渲染层级
     * @return 对象数量
     */
    public int getRenderableCount(RenderLayer layer) {
        return renderLayers.get(layer).size();
    }
    
    /**
     * 获取所有层级的可渲染对象总数
     * @return 对象总数
     */
    public int getTotalRenderableCount() {
        int total = 0;
        for (List<IRenderable> layerList : renderLayers.values()) {
            total += layerList.size();
        }
        return total;
    }
}