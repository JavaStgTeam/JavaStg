package user.enemy;

import java.awt.*;
import stg.game.enemy.Enemy;
import stg.game.ui.GameCanvas;
import user.bullet.TrackingBullet;

/**
 * 追踪弹幕敌人 - 发射追踪玩家的子弹
 * @since 2026-01-23
 */
public class TrackingEnemy extends Enemy {
	private float shootTimer;
	private final float shootInterval;
	private final float moveSpeed;

	public TrackingEnemy(float x, float y, float moveSpeed, GameCanvas gameCanvas) {
		super(x, y, moveSpeed, 0, 20, Color.PINK, 200, gameCanvas);
		this.shootTimer = 0;
		this.shootInterval = 150;
		this.moveSpeed = moveSpeed;
	}

	@Override
	public void update() {
		super.update();

		if (gameCanvas == null || !isAlive()) return;

		int canvasWidth = gameCanvas.getWidth();
		float leftBound = -canvasWidth / 2.0f + size;
		float rightBound = canvasWidth / 2.0f - size;

		if (x <= leftBound) {
			vx = Math.abs(moveSpeed);
		} else if (x >= rightBound) {
			vx = -Math.abs(moveSpeed);
		}

		shootTimer++;

		if (shootTimer >= shootInterval) {
			shootTracking();
			shootTimer = 0;
		}
	}

	private void shootTracking() {
		if (!isAlive() || getGameCanvas() == null) return;

		float bulletSpeed = 5.0f;
		float initialAngle = (float)(Math.PI / 2);
		float turnSpeed = 0.03f;

		TrackingBullet bullet = new TrackingBullet(
			getX(), getY(), bulletSpeed, initialAngle, turnSpeed, 6.0f, Color.PINK
		);
		stg.game.GameWorld world = (stg.game.GameWorld) getGameCanvas().getWorld();
		if (world != null) {
			world.addEnemyBullet(bullet);
		}
	}

	@Override
	public void render(Graphics2D g) {
		float[] screenCoords = toScreenCoords(x, y);
		float screenX = screenCoords[0];
		float screenY = screenCoords[1];

		g.setColor(color);
		g.fillOval((int)(screenX - size), (int)(screenY - size), (int)(size * 2), (int)(size * 2));

		g.setColor(Color.WHITE);
		g.drawOval((int)(screenX - size), (int)(screenY - size), (int)(size * 2), (int)(size * 2));

		renderHealthBar(g, screenX, screenY);
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

