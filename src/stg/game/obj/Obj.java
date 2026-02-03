package stg.game.obj;

import java.awt.*;
import stg.game.ui.GameCanvas;

/**
 * 游戏物体基类 - 所有游戏中的物体都继承自此类
 * @since 2026-01-19
 * @since 2026-01-20 将类移动到stg.game.obj
 * @since 2026-01-29
 * @author JavaSTG Team
 */
public abstract class Obj {
    protected float x; // X坐标
    protected float y; // Y坐标
    protected float vx; // X方向速度
    protected float vy; // Y方向速度
    protected float size; // 物体大小
    protected Color color; // 物体颜色
    protected GameCanvas gameCanvas; // 游戏画布引用
    protected float hitboxRadius; // 碰撞判定半径
    protected boolean active; // 激活状态
    protected int frame; // 帧计数器
    
    // 默认画布尺寸常量
    protected static final float DEFAULT_CANVAS_WIDTH = 548;
    protected static final float DEFAULT_CANVAS_HEIGHT = 921;

    /**
     * 将游戏坐标转换为屏幕坐标
     * @param worldX 游戏世界X坐标
     * @param worldY 游戏世界Y坐标
     * @return 屏幕坐标数组 [x, y]
     */
    protected float[] toScreenCoords(float worldX, float worldY) {
        if (gameCanvas != null) {
            return gameCanvas.getCoordinateSystem().toScreenCoords(worldX, worldY);
        }
        return new float[]{
            worldX + DEFAULT_CANVAS_WIDTH / 2.0f,
            DEFAULT_CANVAS_HEIGHT / 2.0f - worldY
        };
    }

    /**
     * 构造函数
     * @param x 初始X坐标
     * @param y 初始Y坐标
     * @param vx X方向速度
     * @param vy Y方向速度
     * @param size 物体大小
     * @param color 物体颜色
     * @param gameCanvas 游戏画布引用
     */
    public Obj(float x, float y, float vx, float vy, float size, Color color, GameCanvas gameCanvas) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.size = size;
        this.color = color;
        this.gameCanvas = gameCanvas;
        this.hitboxRadius = size;
        this.active = true;
        this.frame = 0;
        initBehavior();
    }

    /**
     * 初始化行为参数
     * 在构造函数中调用，用于初始化行为参数
     */
    protected void initBehavior() {
        // 子类可以重写此方法初始化行为参数
    }

    /**
     * 实现每帧的自定义更新逻辑
     */
    protected void onUpdate() {
        // 子类可以重写此方法实现每帧的自定义更新逻辑
    }

    /**
     * 实现自定义移动逻辑
     */
    protected void onMove() {
        // 子类可以重写此方法实现自定义移动逻辑
    }

    /**
     * 更新物体状态
     */
    public void update() {
        frame++;

        // 调用自定义更新逻辑
        onUpdate();

        // 调用自定义移动逻辑
        onMove();

        // 更新位置
        x += vx;
        y += vy;
    }

    /**
     * 渲染物体
     * @param g 图形上下文
     */
    public void render(Graphics2D g) {
        if (!active) return;

        float[] screenCoords = toScreenCoords(x, y);
        float screenX = screenCoords[0];
        float screenY = screenCoords[1];

        g.setColor(color);
        g.fillOval((int)(screenX - size/2), (int)(screenY - size/2), (int)size, (int)size);
    }

    /**
     * 检查物体是否超出边界
     * @param width 画布宽度
     * @param height 画布高度
     * @return 是否超出边界
     */
    public boolean isOutOfBounds(int width, int height) {
        float leftBound = -width / 2.0f - size;
        float rightBound = width / 2.0f + size;
        float topBound = -height / 2.0f - size;
        float bottomBound = height / 2.0f + size;
        return x < leftBound || x > rightBound || y < topBound || y > bottomBound;
    }

    /**
     * 移动到指定坐标
     * @param x 目标X坐标
     * @param y 目标Y坐标
     */
    public void moveTo(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * 检查物体是否激活
     * @return 是否激活
     */
    public boolean isActive() {
        return active;
    }

    /**
     * 设置物体激活状态
     * @param active 是否激活
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * 获取X坐标
     * @return X坐标
     */
    public float getX() {
        return x;
    }

    /**
     * 获取Y坐标
     * @return Y坐标
     */
    public float getY() {
        return y;
    }

    /**
     * 获取物体大小
     * @return 物体大小
     */
    public float getSize() {
        return size;
    }

    /**
     * 获取物体颜色
     * @return 物体颜色
     */
    public Color getColor() {
        return color;
    }

    /**
     * 重置物体状态
     */
    public void reset() {
        this.active = true;
        this.frame = 0;
    }

    /**
     * 获取游戏画布引用
     * @return 游戏画布引用
     */
    public GameCanvas getGameCanvas() {
        return gameCanvas;
    }

    /**
     * 设置X坐标
     * @param x X坐标
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * 设置Y坐标
     * @param y Y坐标
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * 设置X方向速度
     * @param vx X方向速度
     */
    public void setVx(float vx) {
        this.vx = vx;
    }

    /**
     * 设置Y方向速度
     * @param vy Y方向速度
     */
    public void setVy(float vy) {
        this.vy = vy;
    }

    /**
     * 获取X方向速度
     * @return X方向速度
     */
    public float getVx() {
        return vx;
    }

    /**
     * 获取Y方向速度
     * @return Y方向速度
     */
    public float getVy() {
        return vy;
    }
}