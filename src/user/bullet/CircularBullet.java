package user.bullet;

import java.awt.*;
import user.enemy.EnemyBullet;

/**
 * 圆形弹幕类 - EnemyBullet的子类
 * 标准圆形弹幕,带有渐变效果
 * @since 2026-01-19
 */
public class CircularBullet extends EnemyBullet {
	private Color innerColor; // 内圈颜色

	/**
	 * 构造函数
	 * @param x X坐标
	 * @param y Y坐标
	 * @param vx X方向速度
	 * @param vy Y方向速度
	 */
	public CircularBullet(float x, float y, float vx, float vy) {
		super(x, y, vx, vy, 5, Color.CYAN, 10);
		this.innerColor = Color.WHITE;
	}

	/**
	 * 完整构造函数
	 * @param x X坐标
	 * @param y Y坐标
	 * @param vx X方向速度
	 * @param vy Y方向速度
	 * @param size 子弹大小
	 * @param color 外圈颜色
	 * @param innerColor 内圈颜色
	 * @param damage 伤害值
	 */
	public CircularBullet(float x, float y, float vx, float vy, float size, Color color, Color innerColor, int damage) {
		super(x, y, vx, vy, size, color, damage);
		this.innerColor = innerColor;
	}

	/**
	 * 重写渲染方法,添加渐变效果\n\t * @since 2026-01-19
	 */
	@Override
	public void render(Graphics2D g) {
		float[] screenCoords = toScreenCoords(x, y);
		float screenX = screenCoords[0];
		float screenY = screenCoords[1];

		g.setColor(color);
		g.fillOval((int)(screenX - size), (int)(screenY - size), (int)(size * 2), (int)(size * 2));

		g.setColor(innerColor);
		float innerSize = size * 0.6f;
		g.fillOval((int)(screenX - innerSize), (int)(screenY - innerSize), (int)(innerSize * 2), (int)(innerSize * 2));
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

