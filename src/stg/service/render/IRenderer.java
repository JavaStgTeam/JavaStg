package stg.service.render;

import java.awt.Color;
import java.awt.Font;

/**
 * 渲染器接口 - 统一的渲染抽象层
 * 支持绘制几何图形、文本和图像，包含批量渲染优化
 * @since 2026-02-22
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
     * 开始批量渲染
     * 用于优化多个渲染操作的性能
     */
    void beginBatch();
    
    /**
     * 结束批量渲染并执行实际渲染
     * 批量渲染结束后会清空批处理队列
     */
    void endBatch();
    
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
     * @param color 颜色
     */
    void drawLine(float x1, float y1, float x2, float y2, Color color);
    
    /**
     * 绘制线段（带线宽）
     * @param x1 起点X坐标（游戏逻辑坐标）
     * @param y1 起点Y坐标（游戏逻辑坐标）
     * @param x2 终点X坐标（游戏逻辑坐标）
     * @param y2 终点Y坐标（游戏逻辑坐标）
     * @param width 线宽
     * @param color 颜色
     */
    void drawLine(float x1, float y1, float x2, float y2, float width, Color color);
    
    /**
     * 绘制文字（使用指定字体）
     * @param text 文字内容
     * @param x X坐标（游戏逻辑坐标）
     * @param y Y坐标（游戏逻辑坐标）
     * @param font 字体
     * @param color 颜色
     */
    void drawText(String text, float x, float y, Font font, Color color);
    
    /**
     * 绘制文字（使用默认字体）
     * @param text 文字内容
     * @param x X坐标（游戏逻辑坐标）
     * @param y Y坐标（游戏逻辑坐标）
     * @param fontSize 字体大小
     * @param color 颜色
     */
    void drawText(String text, float x, float y, float fontSize, Color color);
    
    /**
     * 绘制图像
     * @param image 图像对象
     * @param x X坐标（游戏逻辑坐标）
     * @param y Y坐标（游戏逻辑坐标）
     * @param width 宽度
     * @param height 高度
     */
    void drawImage(Object image, float x, float y, float width, float height);
    
    /**
     * 绘制图像（带透明度）
     * @param image 图像对象
     * @param x X坐标（游戏逻辑坐标）
     * @param y Y坐标（游戏逻辑坐标）
     * @param width 宽度
     * @param height 高度
     * @param alpha 透明度（0.0-1.0）
     */
    void drawImage(Object image, float x, float y, float width, float height, float alpha);
    
    /**
     * 绘制图像（带旋转和透明度）
     * @param image 图像对象
     * @param x X坐标（游戏逻辑坐标）
     * @param y Y坐标（游戏逻辑坐标）
     * @param width 宽度
     * @param height 高度
     * @param rotation 旋转角度（弧度）
     * @param alpha 透明度（0.0-1.0）
     */
    void drawImage(Object image, float x, float y, float width, float height, float rotation, float alpha);
    
    /**
     * 设置当前颜色
     * @param color 颜色
     */
    void setColor(Color color);
    
    /**
     * 设置当前字体
     * @param font 字体
     */
    void setFont(Font font);
    
    /**
     * 启用抗锯齿
     */
    void enableAntiAliasing();
    
    /**
     * 禁用抗锯齿
     */
    void disableAntiAliasing();
    
    /**
     * 清理渲染器资源
     */
    void cleanup();
    
    /**
     * 检查渲染器是否已初始化
     * @return 是否已初始化
     */
    boolean isInitialized();
}