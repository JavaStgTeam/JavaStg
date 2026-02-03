package user.laser;

import java.awt.*;

/**
 * 敌人曲线激光类 - 继承自EnemyLaser和CurvedLaser
 * @since 2026-01-21
 */
public class EnemyCurvedLaser extends EnemyLaser {
    private final CurvedLaser curvedLaser; // 实际的曲线激光

    /**
     * 构造函数
     * @param x 起点X坐标
     * @param y 起点Y坐标
     * @param angle 角度(弧度)
     * @param length 长度
     * @param width 宽度
     * @param color 颜色
     * @param vx X方向速度
     * @param vy Y方向速度
     * @param maxTrailLength 最大拖尾长度
     */
    public EnemyCurvedLaser(float x, float y, float angle, float length, float width, Color color,
                           float vx, float vy, int maxTrailLength) {
        super(x, y, angle, length, width, color);
        this.curvedLaser = new CurvedLaser(x, y, angle, length, width, color, vx, vy, maxTrailLength);
    }

    /**
     * 完整构造函数
     * @param x 起点X坐标
     * @param y 起点Y坐标
     * @param angle 角度(弧度)
     * @param length 长度
     * @param width 宽度
     * @param color 颜色
     * @param warningTime 预警时间
     * @param damage 伤害值
     * @param vx X方向速度
     * @param vy Y方向速度
     * @param maxTrailLength 最大拖尾长度
     */
    public EnemyCurvedLaser(float x, float y, float angle, float length, float width, Color color, int warningTime, int damage, float vx, float vy, int maxTrailLength) {
        super(x, y, angle, length, width, color, warningTime, damage);
        this.curvedLaser = new CurvedLaser(x, y, angle, length, width, color, warningTime, damage, vx, vy, maxTrailLength);
    }

    /**
     * 初始化行为参数
     */
    protected void initBehavior() {
        // 初始化行为参数
    }

    /**
     * 实现每帧的自定义更新逻辑
     */
    protected void onUpdate() {
        // 激光的更新逻辑
    }

    /**
     * 实现自定义移动逻辑
     */
    protected void onMove() {
        // 移动逻辑由curvedLaser处理
    }

    public void update() {
        super.update();
        curvedLaser.update();
        // 同步位置和状态
        this.x = curvedLaser.getX();
        this.y = curvedLaser.getY();
        this.angle = curvedLaser.getAngle();
        this.active = curvedLaser.isActive();
    }

    public void render(Graphics2D g) {
        curvedLaser.render(g);
    }

    public boolean checkCollision(float px, float py) {
        return curvedLaser.checkCollision(px, py);
    }

    public boolean isOutOfBounds(int width, int height) {
        return curvedLaser.isOutOfBounds(width, height);
    }

    // 委托方法到curvedLaser
    public float getVx() {
        return curvedLaser.getVx();
    }

    public float getVy() {
        return curvedLaser.getVy();
    }

    public void setVx(float vx) {
        curvedLaser.setVx(vx);
    }

    public void setVy(float vy) {
        curvedLaser.setVy(vy);
    }

    public void setSpeed(float speed, float angle) {
        curvedLaser.setSpeed(speed, angle);
    }

    public int getMaxTrailLength() {
        return curvedLaser.getMaxTrailLength();
    }

    public void setMaxTrailLength(int maxTrailLength) {
        curvedLaser.setMaxTrailLength(maxTrailLength);
    }

    public float getTrailWidth() {
        return curvedLaser.getTrailWidth();
    }

    public void setTrailWidth(float trailWidth) {
        curvedLaser.setTrailWidth(trailWidth);
    }

    public int getCurrentTrailLength() {
        return curvedLaser.getCurrentTrailLength();
    }

    /**
     * 任务开始时触发的方法
     */
    protected void onTaskStart() {
        // 空实现
    }

    /**
     * 任务结束时触发的方法
     */
    protected void onTaskEnd() {
        // 空实现
    }
}