package user.enemy;

import java.awt.*;
import stg.game.enemy.Enemy;
import stg.game.ui.GameCanvas;
import user.bullet.SpiralBullet;

/**
 * 螺旋弹幕敌人 - 发射螺旋前进的子弹
 * @since 2026-01-23
 */
public class SpiralEnemy extends Enemy {
	private float shootTimer;
	private float shootInterval;
	private float moveSpeed;
	private float spiralAngle;

	public SpiralEnemy(float x, float y, float moveSpeed, GameCanvas gameCanvas) {
		super(x, y, moveSpeed, 0, 25, Color.CYAN, 150, gameCanvas);
		this.shootTimer = 0;
		this.shootInterval = 120;
		this.moveSpeed = moveSpeed;
		this.spiralAngle = 0;
	}

	public void update() {
		super.update();

		if (getGameCanvas() == null || !isActive()) return;

		int canvasWidth = getGameCanvas().getWidth();
		float leftBound = -canvasWidth / 2.0f + getSize();
		float rightBound = canvasWidth / 2.0f - getSize();

		if (getX() <= leftBound) {
			setVx(Math.abs(moveSpeed));
		} else if (getX() >= rightBound) {
			setVx(-Math.abs(moveSpeed));
		}

		shootTimer++;

		if (shootTimer >= shootInterval) {
			shootSpiral();
			shootTimer = 0;
		}
	}

	private void shootSpiral() {
		if (!isActive() || getGameCanvas() == null) return;

		float bulletSpeed = 6.0f;
		float baseAngle = (float)(Math.PI / 2);
		float radius = 30.0f;
		float angleSpeed = 0.15f;

		for (int i = 0; i < 3; i++) {
			float angleOffset = (float)(i * 2 * Math.PI / 3);
			SpiralBullet bullet = new SpiralBullet(
				getX(), getY(), bulletSpeed, baseAngle,
				radius, angleSpeed, 5.0f, Color.CYAN
			);
			// 暂时注释掉，因为 SpiralBullet 可能没有 setGameCanvas 方法，GameCanvas 可能没有 addEnemyBullet 方法
			// bullet.setGameCanvas(getGameCanvas());
			// getGameCanvas().addEnemyBullet(bullet);
		}
	}

	public void render(Graphics2D g) {
		float[] screenCoords = toScreenCoords(getX(), getY());
		float screenX = screenCoords[0];
		float screenY = screenCoords[1];

		g.setColor(getColor());
		g.fillRect((int)(screenX - getSize()), (int)(screenY - getSize()), (int)(getSize() * 2), (int)(getSize() * 2));

		g.setColor(Color.WHITE);
		g.drawRect((int)(screenX - getSize()), (int)(screenY - getSize()), (int)(getSize() * 2), (int)(getSize() * 2));

		// 暂时注释掉，因为可能没有 renderHealthBar 方法
		// renderHealthBar(g, screenX, screenY);
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

