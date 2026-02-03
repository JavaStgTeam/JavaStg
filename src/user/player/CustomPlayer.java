package user.player;

import java.awt.*;
import stg.game.GameWorld;
import stg.game.player.Player;
import stg.game.ui.GameCanvas;
import user.bullet.SimpleBullet;

/**
 * 自定义玩家类 - 演示如何创建自定义玩家和子机
 */
public class CustomPlayer extends Player {
	private static final float CUSTOM_SPEED = 4.8f;
	private static final float CUSTOM_SPEED_SLOW = 2.2f;
	private static final float CUSTOM_SIZE = 17f;
	private static final int CUSTOM_SHOOT_INTERVAL = 1;
	private static final Color CUSTOM_COLOR = new Color(200, 150, 255);
	private static final Color BULLET_COLOR = new Color(150, 100, 255);
	private static final int CUSTOM_BULLET_DAMAGE = 2;

	public CustomPlayer(float spawnX, float spawnY) {
		super(spawnX, spawnY, CUSTOM_SPEED, CUSTOM_SPEED_SLOW, CUSTOM_SIZE, null);
		setShootInterval(CUSTOM_SHOOT_INTERVAL);
		this.bulletDamage = CUSTOM_BULLET_DAMAGE;
	}

	/**
	 * 初始化自定义玩家的子机
	 */
	public void initializeOptions(GameCanvas canvas) {
		// 添加3个子机，形成三角形布局
		CustomOption option1 = new CustomOption(this, -30, 10, canvas);
		CustomOption option2 = new CustomOption(this, 30, 10, canvas);
		CustomOption option3 = new CustomOption(this, 0, 25, canvas);

		addOption(option1);
		addOption(option2);
		addOption(option3);

		System.out.println("自定义玩家子机初始化完成，共3个子机");
	}

	@Override
	protected void shoot() {
		GameCanvas canvas = getGameCanvas();
		if (canvas == null) return;

		Object worldObj = canvas.getWorld();
		if (!(worldObj instanceof GameWorld)) return;

		GameWorld gameWorld = (GameWorld) worldObj;
		boolean slowMode = isSlowMode();
		float bulletSpeed = 48.0f;
		float bulletSize = slowMode ? 5.5f : 3.5f;

		if (slowMode) {
			// 低速模式：单发高伤害大子弹
			SimpleBullet bullet = new SimpleBullet(getX(), getY(), 0, bulletSpeed, bulletSize + 2, BULLET_COLOR);
			bullet.setDamage(bulletDamage * 3);
			gameWorld.addPlayerBullet(bullet);
		} else {
			// 普通模式：5发散射
			float spreadAngle = 0.12f;
			for (int i = -2; i <= 2; i++) {
				float angle = i * spreadAngle;
				float bulletVx = (float)Math.sin(angle) * bulletSpeed;
				float bulletVy = bulletSpeed;

				SimpleBullet bullet = new SimpleBullet(getX(), getY(), bulletVx, bulletVy, bulletSize, BULLET_COLOR);
				bullet.setDamage(bulletDamage);
				gameWorld.addPlayerBullet(bullet);
			}
		}
	}

	@Override
	public void render(Graphics2D g) {
		float[] screenCoords = toScreenCoords(getX(), getY());
		float screenX = screenCoords[0];
		float screenY = screenCoords[1];

		stg.util.RenderUtils.enableAntiAliasing(g);

		// 无敌闪烁效果
		boolean shouldRender = true;
		if (isInvincible()) {
			int invincibleTimer = getInvincibleTimer();
			int flashPhase = invincibleTimer % 10;
			if (flashPhase < 5) {
				shouldRender = false;
			}
		}

		// 低速模式显示判定点
		if (isSlowMode() && shouldRender) {
			g.setColor(Color.WHITE);
			g.fillOval((int)(screenX - getHitboxRadius()), (int)(screenY - getHitboxRadius()),
			          (int)(getHitboxRadius() * 2), (int)(getHitboxRadius() * 2));
		}

		// 自定义玩家外观（紫色球体）
		if (shouldRender) {
			g.setColor(CUSTOM_COLOR);
			g.fillOval((int)(screenX - getSize()), (int)(screenY - getSize()),
			          (int)(getSize() * 2), (int)(getSize() * 2));

			// 添加装饰性边框
			g.setColor(new Color(100, 50, 200));
			g.drawOval((int)(screenX - getSize()), (int)(screenY - getSize()),
			          (int)(getSize() * 2), (int)(getSize() * 2));

			// 添加星星图案
			g.setColor(new Color(255, 255, 255, 180));
			int starSize = (int)(getSize() * 0.3f);
			g.fillOval((int)(screenX - starSize), (int)(screenY - starSize),
			          starSize * 2, starSize * 2);
		}
	}
}
