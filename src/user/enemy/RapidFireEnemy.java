package user.enemy;

import java.awt.*;
import stg.game.GameWorld;
import stg.game.enemy.Enemy;
import stg.game.ui.GameCanvas;
import user.bullet.CircularBullet;

/**
 * 快速射击敌人- 高频率发射子弹
 * @since 2026-01-23
 */
public class RapidFireEnemy extends Enemy {
	private float shootTimer;
	private final float shootInterval;
	private final float moveSpeed;
	private final int burstCount;
	private int currentBurst;
	private final float burstDelay;

	public RapidFireEnemy(float x, float y, float moveSpeed, GameCanvas gameCanvas) {
		super(x, y, moveSpeed, 0, 18, Color.YELLOW, 100, gameCanvas);
		this.shootTimer = 0;
		this.shootInterval = 180;
		this.moveSpeed = moveSpeed;
		this.burstCount = 5;
		this.currentBurst = 0;
		this.burstDelay = 8;
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

		if (currentBurst > 0) {
			if (shootTimer >= burstDelay) {
				shootSingle();
				currentBurst--;
				shootTimer = 0;
			}
		} else if (shootTimer >= shootInterval) {
			currentBurst = burstCount;
			shootTimer = 0;
		}
	}

	private void shootSingle() {
		if (!isAlive() || gameCanvas == null) return;

		float bulletSpeed = -10.0f;
		CircularBullet bullet = new CircularBullet(x, y, 0, bulletSpeed);

		// 通过 GameWorld 添加子弹
		Object worldObj = gameCanvas.getWorld();
		if (worldObj instanceof GameWorld) {
			GameWorld gameWorld = (GameWorld) worldObj;
			gameWorld.addEnemyBullet(bullet);
		}
	}

	@Override
	public void render(Graphics2D g) {
		float[] screenCoords = toScreenCoords(x, y);
		float screenX = screenCoords[0];
		float screenY = screenCoords[1];

		g.setColor(color);
		g.fillRect((int)(screenX - size * 0.8f), (int)(screenY - size), (int)(size * 1.6f), (int)(size * 2));

		g.setColor(Color.WHITE);
		g.drawRect((int)(screenX - size * 0.8f), (int)(screenY - size), (int)(size * 1.6f), (int)(size * 2));

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

