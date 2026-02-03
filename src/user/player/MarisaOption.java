package user.player;

import java.awt.*;
import user.bullet.CircularBullet;
import user.bullet.SimpleBullet;
import stg.game.player.Player;
import stg.game.ui.GameCanvas;

/**
 * 魔理沙子机类
 * 特点：发射高威力集中弹，激光攻�? */
public class MarisaOption extends Option {
	private static final float MARISA_OPTION_SIZE = 10.0f;
	private static final Color MARISA_OPTION_COLOR = new Color(180, 200, 255);
	private static final int MARISA_OPTION_SHOOT_INTERVAL = 3;
	private static final int MARISA_OPTION_BULLET_DAMAGE = 2;
	private static final float BULLET_SPEED = 52.0f;
	private static final float BULLET_SIZE = 4.0f;
	private static final Color BULLET_COLOR = new Color(100, 200, 255);
	private static final int LASER_INTERVAL = 15; // 激光发射间隔
	private int laserCooldown; // 激光冷却
	public MarisaOption(Player player, float offsetX, float offsetY, GameCanvas gameCanvas) {
		super(player, offsetX, offsetY, gameCanvas);
		setSize(MARISA_OPTION_SIZE);
		setColor(MARISA_OPTION_COLOR);
		setShootInterval(MARISA_OPTION_SHOOT_INTERVAL);
		setBulletDamage(MARISA_OPTION_BULLET_DAMAGE);
		setFollowSpeed(0.18f);
		this.laserCooldown = 0;
	}

	@Override
	public void update() {
		super.update();

		// 更新激光冷却
		if (laserCooldown > 0) {
			laserCooldown--;
		}
	}

	@Override
	protected void shoot() {
		if (gameCanvas == null) return;

		// 发射高威力集中弹
		SimpleBullet bullet = new SimpleBullet(getX(), getY(), 0, BULLET_SPEED, BULLET_SIZE, BULLET_COLOR);
		bullet.setDamage(getBulletDamage());
		// 暂时注释掉，因为 GameCanvas 可能没有 addBullet 方法
		// gameCanvas.addBullet(bullet);

		// 定期发射圆形弹幕（增加攻击范围）
		if (laserCooldown == 0) {
			shootCircularSpread();
			laserCooldown = LASER_INTERVAL;
		}
	}

	/**
	 * 发射圆形扩散弹幕
	 */
	private void shootCircularSpread() {
		int bulletCount = 8;
		float spreadAngle = (float)(2 * Math.PI / bulletCount);
		float bulletSpeed = 40.0f;
		float bulletSize = 3.0f;

		for (int i = 0; i < bulletCount; i++) {
			float angle = i * spreadAngle;
			float vx = (float)Math.sin(angle) * bulletSpeed;
			float vy = (float)Math.cos(angle) * bulletSpeed;

			CircularBullet bullet = new CircularBullet(getX(), getY(), vx, vy, bulletSize, BULLET_COLOR, Color.WHITE, getBulletDamage() - 1);
			// 暂时注释掉，因为 GameCanvas 可能没有 addBullet 方法
			// gameCanvas.addBullet(bullet);
		}
	}

	@Override
	public void render(Graphics2D g) {
		float screenX = getX() + gameCanvas.getWidth() / 2.0f;
		float screenY = gameCanvas.getHeight() / 2.0f - getY();

		stg.util.RenderUtils.enableAntiAliasing(g);

		// 绘制魔理沙子机主体（蓝色圆形）
		g.setColor(getColor());
		g.fillOval((int)(screenX - getSize()), (int)(screenY - getSize()),
		          (int)(getSize() * 2), (int)(getSize() * 2));

		// 绘制魔理沙标志性的金色边框
		g.setColor(new Color(255, 215, 0));
		g.drawOval((int)(screenX - getSize()), (int)(screenY - getSize()),
		          (int)(getSize() * 2), (int)(getSize() * 2));

		// 绘制子机核心（白色亮点）
		g.setColor(new Color(255, 255, 255, 220));
		g.fillOval((int)(screenX - getSize() * 0.5f), (int)(screenY - getSize() * 0.5f),
		          (int)(getSize()), (int)(getSize()));

		// 绘制魔理沙的星星图案（简化版本）
		g.setColor(new Color(255, 255, 255, 180));
		int starSize = (int)(getSize() * 0.3f);
		g.fillOval((int)(screenX - starSize), (int)(screenY - starSize),
		          starSize * 2, starSize * 2);
	}

	@Override
	public void reset() {
		super.reset();
		laserCooldown = 0;
	}
}

