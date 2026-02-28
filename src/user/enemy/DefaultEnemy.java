package user.enemy;

import java.awt.Color;
import stg.entity.enemy.Enemy;
import stg.util.objectpool.Pooled;

/**
 * 默认敌人类 - 水平移动并在碰到版边时反弹
 * @since 2026-02-13
 * @date 2026-02-22 添加@Pooled注解支持对象池
 */
@Pooled(initialCapacity = 10, maxCapacity = 50, name = "DefaultEnemyPool")
public class DefaultEnemy extends Enemy {
    private static final float ENEMY_SPEED = 5.0f; // 敌人移动速度
    private static final float ENEMY_SIZE = 20.0f; // 敌人大小
    private static final Color ENEMY_COLOR = new Color(255, 0, 0); // 敌人颜色：红色
    private static final int ENEMY_HP = 200; // 敌人生命值
    
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
        // 先检查边界，再更新位置
        float x = getX();
        float vx = getVx();
        
        // 使用游戏逻辑坐标系的固定边界
        stg.entity.base.Obj.requireCoordinateSystem();
        stg.util.CoordinateSystem cs = stg.entity.base.Obj.getSharedCoordinateSystem();
        float leftBound = cs.getLeftBound() + getSize();
        float rightBound = cs.getRightBound() - getSize();
        
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
        super.update();
    }
    
    /**
     * 更新敌人状态
     * 处理水平移动和边界反弹
     * @param canvasWidth 画布宽度（兼容参数，不使用）
     * @param canvasHeight 画布高度（兼容参数，不使用）
     */
    @Override
    public void update(int canvasWidth, int canvasHeight) {
        update(); // 调用无参数版本
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