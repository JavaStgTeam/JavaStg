package user.laser;

import java.awt.Color;
import stg.game.laser.Laser;

/**
 * 简单激光类 - 提供空的task实现
 * 用于不需要特殊task行为的激光
 * @since 2026-01-23 创建默认实现
 */
public class SimpleLaser extends Laser {

    /**
     * 构造函数
     * @param x 起点X坐标
     * @param y 起点Y坐标
     * @param angle 角度(弧度)
     * @param length 长度
     * @param width 宽度
     * @param color 颜色
     */
    public SimpleLaser(float x, float y, float angle, float length, float width, Color color) {
        super(x, y, angle, length, width, color);
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
     */
    public SimpleLaser(float x, float y, float angle, float length, float width, Color color, int warningTime, int damage) {
        super(x, y, angle, length, width, color, warningTime, damage);
    }

    /**
     * 初始化行为参数
     */
    @Override
    protected void initBehavior() {
        // 初始化行为参数
    }

    /**
     * 实现每帧的自定义更新逻辑
     */
    @Override
    protected void onUpdate() {
        // 简单激光的更新逻辑
    }

    /**
     * 实现自定义移动逻辑
     */
    @Override
    protected void onMove() {
        // 简单激光的移动逻辑
    }

    /**
     * 任务开始时触发的方法 - 空实现
     */
    @Override
    protected void onTaskStart() {
        // 空实现，不需要特殊行为
    }

    /**
     * 任务结束时触发的方法 - 空实现
     */
    @Override
    protected void onTaskEnd() {
        // 空实现，不需要特殊行为
    }
}