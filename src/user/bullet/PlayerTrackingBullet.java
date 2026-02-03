package user.bullet;

import java.awt.Color;
import stg.game.GameWorld;
import stg.game.bullet.Bullet;
import stg.game.enemy.Enemy;

/**
 * 玩家追踪子弹类- 会追踪敌人的子弹
 * 用于灵梦子机等需要自动瞄准敌人的场景
 */
public class PlayerTrackingBullet extends Bullet {
	private final float speed;
	private final float turnSpeed;
	private int trackingFrame;
	private int delayFrames;

	/**
	 * 构造函数
	 * @param x 初始X坐标
	 * @param y 初始Y坐标
	 * @param speed 子弹速度
	 * @param initialAngle 初始角度
	 * @param turnSpeed 转向速度（弧度/帧）
	 * @param size 子弹大小
	 * @param color 子弹颜色
	 */
	public PlayerTrackingBullet(float x, float y, float speed, float initialAngle,
			float turnSpeed, float size, Color color) {
		super(x, y, (float)Math.cos(initialAngle) * speed, (float)Math.sin(initialAngle) * speed, size, color);
		this.speed = speed;
		this.turnSpeed = turnSpeed;
		this.trackingFrame = 0;
		this.delayFrames = 0;
	}

	/**
	 * 设置延迟帧数（延迟后才开始追踪）
	 * @param delayFrames 延迟帧数
	 */
	public void setDelayFrames(int delayFrames) {
		this.delayFrames = delayFrames;
	}

	/**
	 * 更新子弹位置
	 */
	@Override
	public void update() {
		trackingFrame++;

		if (trackingFrame > delayFrames && gameCanvas != null) {
			Enemy nearestEnemy = findNearestEnemy();
			if (nearestEnemy != null) {
				float targetX = nearestEnemy.getX();
				float targetY = nearestEnemy.getY();

				float dx = targetX - x;
				float dy = targetY - y;
				float targetAngle = (float)Math.atan2(dy, dx);

				float currentAngle = (float)Math.atan2(vy, vx);
				float angleDiff = targetAngle - currentAngle;

				while (angleDiff > Math.PI) angleDiff -= 2 * Math.PI;
				while (angleDiff < -Math.PI) angleDiff += 2 * Math.PI;

				if (angleDiff > turnSpeed) {
					angleDiff = turnSpeed;
				} else if (angleDiff < -turnSpeed) {
					angleDiff = -turnSpeed;
				}

				float newAngle = currentAngle + angleDiff;
				vx = (float)Math.cos(newAngle) * speed;
				vy = (float)Math.sin(newAngle) * speed;
			}
		}

		x += vx;
		y += vy;
	}

	/**
	 * 查找最近的敌人
	 * @return 最近的敌人，如果没有敌人则返回null
	 */
	private Enemy findNearestEnemy() {
		if (gameCanvas == null) return null;
		
		Object worldObj = gameCanvas.getWorld();
		if (!(worldObj instanceof GameWorld)) return null;
		
		GameWorld gameWorld = (GameWorld) worldObj;

		Enemy nearest = null;
		float minDistance = Float.MAX_VALUE;

		for (Enemy enemy : gameWorld.getEnemies()) {
			if (!enemy.isAlive()) continue;

			float dx = enemy.getX() - x;
			float dy = enemy.getY() - y;
			float distance = dx * dx + dy * dy;

			if (distance < minDistance) {
				minDistance = distance;
				nearest = enemy;
			}
		}

		return nearest;
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

