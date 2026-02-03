package stg.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;

    /**
 * 渲染工具类 - 提供统一的渲染设置和优化方法
 * @since 2026-01-30
 */
public class RenderUtils {
    private RenderUtils() {}
    
    /**
     * 启用抗锯齿渲染
     * @param g 图形上下文
     */
    public static void enableAntiAliasing(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }
    
    /**
     * 启用高质量渲染设置
     * @param g 图形上下文
     */
    public static void enableHighQualityRendering(Graphics2D g) {
        enableAntiAliasing(g);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }
    
    /**
     * 重置渲染设置为默认值
     * @param g 图形上下文
     */
    public static void resetRenderingHints(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_DEFAULT);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
    }
}

