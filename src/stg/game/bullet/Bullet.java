package stg.game.bullet;

import java.awt.*;
import stg.game.obj.Obj;

/**
 * 子弹�?- @since 2026-01-19 使用中心原点坐标�? */
public abstract class Bullet extends Obj {
	protected int damage = 0; // @since 2026-01-23 子弹伤害，默�?（由玩家统一控制�?
	/**
	 * 构造函�?	 * @param x 初始X坐标
	 * @param y 初始Y坐标
	 * @param vx X方向速度
	 * @param vy Y方向速度
	 * @param size 子弹大小
	 * @param color 子弹颜色
	 */
	public Bullet(float x, float y, float vx, float vy, float size, Color color) {
		super(x, y, vx, vy, size, color, null);
		// @since 2026-01-23 设置碰撞判定半径为size�?倍，确保高速子弹不会穿透敌�?		setHitboxRadius(size * 5.0f);
	}

	// ========== 伤害相关 ==========

	/**
 * 获取子弹伤害
 * @return 子弹伤害�? * @since 2026-01-23 添加伤害获取方法
 */
	public int getDamage() {
		return damage;
	}

	/**
 * 设置子弹伤害
 * @param damage 伤害�? * @since 2026-01-23 添加伤害设置方法
 */
	public void setDamage(int damage) {
		this.damage = damage;
	}

	/**
	 * 任务开始时触发的方�?- 用于处理开局对话�?	 */
	protected abstract void onTaskStart();

	/**
	 * 任务结束时触发的方法 - 用于处理boss击破对话和道具掉�?	 */
	protected abstract void onTaskEnd();
}

