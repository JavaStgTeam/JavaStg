package stg.game.enemy;

import java.awt.*;
import stg.game.obj.Obj;
import stg.game.ui.GameCanvas;

/**
 * 敌方单位- 所有敌人的基类
 * @since 2026-01-19
 */
public abstract class Enemy extends Obj {
	protected int hp; // 生命值
	protected int maxHp; // 最大生命值

	public Enemy(int x, int y) {
		this(x, y, 0, 0, 20, Color.BLUE, 10, null);
	}

	public Enemy(float x, float y) {
		this(x, y, 0, 0, 20, Color.BLUE, 10, null);
	}

	public Enemy(float x, float y, float vx, float vy, float size, Color color, int hp, GameCanvas gameCanvas) {
		super(x, y, vx, vy, size, color, gameCanvas);
		this.hp = hp;
		this.maxHp = hp;
	}

	/**
	 * 更新敌人状态
	 * @since 2026-01-19 基类提供基本移动和存活检测
	 */
	@Override
	public void update() {
		super.update();

		// 检查生命值
		if (hp <= 0) {
			setActive(false);
		}
	}

	/**
	 * 渲染敌人 - @since 2026-01-19 使用中心原点坐标系
	 * @param g 图形上下文
	 * @since 2026-01-19 基类提供基本渲染,子类可自定义渲染
	 */
	@Override
	public void render(Graphics2D g) {
		if (!isActive()) return;

		float[] screenCoords = toScreenCoords(getX(), getY());
		float screenX = screenCoords[0];
		float screenY = screenCoords[1];

		g.setColor(getColor());
		g.fillOval((int)(screenX - getSize()), (int)(screenY - getSize()), (int)(getSize() * 2), (int)(getSize() * 2));

		renderHealthBar(g, screenX, screenY);
	}

	/**
	 * 渲染生命值条 - @since 2026-01-19 使用屏幕坐标
	 * @param g 图形上下文
	 * @param screenX 屏幕X坐标
	 * @param screenY 屏幕Y坐标
	 * @since 2026-01-19 在敌人上方显示生命值
	 */
	protected void renderHealthBar(Graphics2D g, float screenX, float screenY) {
		int barWidth = (int)(getSize() * 2);
		int barHeight = 4;
		int barX = (int)(screenX - getSize());
		int barY = (int)(screenY - getSize() - 8);

		// 背景
		g.setColor(Color.GRAY);
		g.fillRect(barX, barY, barWidth, barHeight);

		// 生命值
		float hpPercent = (float)hp / maxHp;
		g.setColor(Color.RED);
		g.fillRect(barX, barY, (int)(barWidth * hpPercent), barHeight);
	}

	/**
	 * 受到伤害
	 * @param damage 伤害值
	 * @since 2026-01-19 处理伤害逻辑
	 */
	public void takeDamage(int damage) {
		hp -= damage;
		if (hp <= 0) {
			hp = 0;
			setActive(false);
			onDeath(); // 调用死亡回调
		}
	}

	/**
	 * 死亡回调 - 子类可重写
	 * @since 2026-01-19 敌人死亡时触发
	 */
	protected void onDeath() {
		// 子类可以重写此方法添加死亡特效、掉落物品等
	}

	/**
	 * 检查是否越界- @since 2026-01-19 使用中心原点坐标系
	 * @param canvasWidth 画布宽度
	 * @param canvasHeight 画布高度
	 * @return 是否越界
	 */
	@Override
	public boolean isOutOfBounds(int canvasWidth, int canvasHeight) {
		if (canvasWidth <= 0 || canvasHeight <= 0) {
			return false;
		}

		float leftBound = -canvasWidth / 2.0f - getSize() * 2;
		float rightBound = canvasWidth / 2.0f + getSize() * 2;
		float topBound = -canvasHeight / 2.0f - getSize() * 2;
		float bottomBound = canvasHeight / 2.0f + getSize() * 2;

		return getX() < leftBound || getX() > rightBound ||
		       getY() < topBound || getY() > bottomBound;
	}

	/**
	 * 检查是否存活
	 * @return 是否存活
	 */
	public boolean isAlive() {
		return isActive();
	}

	/**
	 * 获取当前生命值
	 * @return 生命值
	 */
	public int getHp() {
		return hp;
	}

	/**
	 * 设置生命值
	 * @param hp 生命值
	 */
	public void setHp(int hp) {
		this.hp = hp;
	}

	/**
	 * 获取最大生命值
	 * @return 最大生命值
	 */
	public int getMaxHp() {
		return maxHp;
	}

	/**
	 * 重置敌人状态
	 */
	@Override
	public void reset() {
		super.reset();
		this.hp = maxHp;
	}

	/**
	 * 任务开始时触发的方法- 用于处理开局对话等
	 */
	protected abstract void onTaskStart();

	/**
	 * 任务结束时触发的方法 - 用于处理boss击破对话和道具掉落
	 */
	protected abstract void onTaskEnd();
}