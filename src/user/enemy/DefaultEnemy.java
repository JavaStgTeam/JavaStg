package user.enemy;

import java.awt.Color;
import stg.game.enemy.Enemy;

/**
 * 默认敌人类 - 水平移动并在碰到版边时反弹
 * @since 2026-02-13
 */
public class DefaultEnemy extends Enemy {
    private static final float ENEMY_SPEED = 5.0f; // 敌人移动速度
    private static final float ENEMY_SIZE = 20.0f; // 敌人大小
    private static final Color ENEMY_COLOR = new Color(255, 0, 0); // 敌人颜色：红色
    private static final int ENEMY_HP = 50; // 敌人生命值
    
    private static final int DEFAULT_CANVAS_WIDTH = 800; // 默认画布宽度

    /**
     * 构造函数
     * @param x 初始X坐标
     * @param y 初始Y坐标
     */
    public DefaultEnemy(float x, float y) {
        // 初始水平向右移动（X轴正方向），Y方向速度为0
        super(x, y, ENEMY_SPEED, 0, ENEMY_SIZE, ENEMY_COLOR, ENEMY_HP);
    }

    /**
     * 构造函数
     * @param x 初始X坐标
     * @param y 初始Y坐标
     * @param size 敌人大小
     */
    public DefaultEnemy(float x, float y, float size) {
        // 初始水平向右移动（X轴正方向），Y方向速度为0
        super(x, y, ENEMY_SPEED, 0, size, ENEMY_COLOR, ENEMY_HP);
    }

    /**
     * 构造函数
     * @param x 初始X坐标
     * @param y 初始Y坐标
     * @param size 敌人大小
     * @param color 敌人颜色
     */
    public DefaultEnemy(float x, float y, float size, Color color) {
        // 初始水平向右移动（X轴正方向），Y方向速度为0
        super(x, y, ENEMY_SPEED, 0, size, color, ENEMY_HP);
    }

    /**
     * 构造函数
     * @param x 初始X坐标
     * @param y 初始Y坐标
     * @param size 敌人大小
     * @param color 敌人颜色
     * @param hp 敌人生命值
     */
    public DefaultEnemy(float x, float y, float size, Color color, int hp) {
        // 初始水平向右移动（X轴正方向），Y方向速度为0
        super(x, y, ENEMY_SPEED, 0, size, color, hp);
    }

    /**
     * 更新敌人状态
     * 处理水平移动和边界反弹
     */
    @Override
    public void update() {
        // 调用无参数版本的update
        update(800, 600); // 默认画布尺寸
    }
    
    /**
     * 更新敌人状态
     * 处理水平移动和边界反弹
     * @param canvasWidth 画布宽度
     * @param canvasHeight 画布高度
     */
    @Override
    public void update(int canvasWidth, int canvasHeight) {
        // 先检查边界，再更新位置
        float x = getX();
        float vx = getVx();
        
        // 计算边界
        float leftBound = -canvasWidth / 2.0f + getSize();
        float rightBound = canvasWidth / 2.0f - getSize();
        
        // 碰到左边界，开始向右移动
        if (x <= leftBound && vx < 0) {
            setVx(ENEMY_SPEED);
            // 调整位置，确保敌人不会卡在边界外
            setX(leftBound);
        }
        // 碰到右边界，开始向左移动
        else if (x >= rightBound && vx > 0) {
            setVx(-ENEMY_SPEED);
            // 调整位置，确保敌人不会卡在边界外
            setX(rightBound);
        }
        
        // 然后更新位置
        super.update(canvasWidth, canvasHeight);
    }

    /**
     * 任务开始时触发的方法
     */
    @Override
    protected void onTaskStart() {
        // 空实现，不需要特殊行为
    }

    /**
     * 任务结束时触发的方法
     */
    @Override
    protected void onTaskEnd() {
        // 空实现，不需要特殊行为
    }
}