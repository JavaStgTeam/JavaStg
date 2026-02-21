package stg.renderer;

import java.awt.Color;

/**
 * 渲染器接口 - 统一的渲染抽象层
 * 支持不同的渲染后端（Java2D、OpenGL等）
 * @since 2026-02-21
 */
public interface IRenderer {
    /**
     * 初始化渲染器
     */
    void init();
    
    /**
     * 开始渲染帧
     */
    void beginFrame();
    
    /**
     * 结束渲染帧
     */
    void endFrame();
    
    /**
     * 绘制圆形
     * @param x 圆心X坐标（游戏逻辑坐标）
     * @param y 圆心Y坐标（游戏逻辑坐标）
     * @param radius 半径
     * @param color 颜色
     */
    void drawCircle(float x, float y, float radius, Color color);
    
    /**
     * 绘制矩形
     * @param x 左上角X坐标（游戏逻辑坐标）
     * @param y 左上角Y坐标（游戏逻辑坐标）
     * @param width 宽度
     * @param height 高度
     * @param color 颜色
     */
    void drawRect(float x, float y, float width, float height, Color color);
    
    /**
     * 绘制线段
     * @param x1 起点X坐标（游戏逻辑坐标）
     * @param y1 起点Y坐标（游戏逻辑坐标）
     * @param x2 终点X坐标（游戏逻辑坐标）
     * @param y2 终点Y坐标（游戏逻辑坐标）
     * @param width 线宽
     * @param color 颜色
     */
    void drawLine(float x1, float y1, float x2, float y2, float width, Color color);
    
    /**
     * 绘制文字
     * @param text 文字内容
     * @param x X坐标（游戏逻辑坐标）
     * @param y Y坐标（游戏逻辑坐标）
     * @param fontSize 字体大小
     * @param color 颜色
     */
    void drawText(String text, float x, float y, float fontSize, Color color);
    
    /**
     * 清理渲染器资源
     */
    void cleanup();
    
    /**
     * 启用抗锯齿
     */
    void enableAntiAliasing();
    
    /**
     * 禁用抗锯齿
     */
    void disableAntiAliasing();
}
