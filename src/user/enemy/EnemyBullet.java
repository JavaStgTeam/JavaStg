package user.enemy;

import java.awt.*;
import stg.game.bullet.Bullet;

/**
 * 敌方子弹类- 继承自Bullet,所有敌方弹幕的基类
 * @since 2026-01-29
 */
public class EnemyBullet extends Bullet {
	protected int damage; // 伤害值

	/**
	 * 空参构造函数
	 */
	public EnemyBullet() {
		super(0, 0, 0, 0, 4, Color.CYAN);
		this.damage = 10;
	}

	/**
	 * 基本构造函数
	 * @param x X坐标
	 * @param y Y坐标
	 * @param vx X方向速度
	 * @param vy Y方向速度
	 */
	public EnemyBullet(float x, float y, float vx, float vy) {
		this(x, y, vx, vy, 4, Color.CYAN, 10);
	}

	/**
	 * 完整构造函数
	 * @param x X坐标
	 * @param y Y坐标
	 * @param vx X方向速度
	 * @param vy Y方向速度
	 * @param size 子弹大小
	 * @param color 子弹颜色
	 * @param damage 伤害值
	 */
	public EnemyBullet(float x, float y, float vx, float vy, float size, Color color, int damage) {
		super(x, y, vx, vy, size, color);
		this.damage = damage;
	}

	/**
	 * 获取伤害值
	 * @return 伤害值
	 */
	public int getDamage() {
		return damage;
	}

	/**
	 * 设置伤害值
	 * @param damage 伤害值
	 */
	public void setDamage(int damage) {
		this.damage = damage;
	}

	/**
	 * 任务开始时触发的方法
	 */
	@Override
	protected void onTaskStart() {
		// 空实现
	}

	/**
	 * 任务结束时触发的方法
	 */
	@Override
	protected void onTaskEnd() {
		// 空实现
	}
}

