package user.player;

import java.awt.*;
import user.bullet.PlayerTrackingBullet;
import stg.game.ui.GameCanvas;
import stg.game.player.Player;

/**
 * 灵梦子机�? * 特点：发射追踪弹，自动瞄准最近的敌人
 */
public class ReimuOption extends Option {
	private static final float REIMU_OPTION_SIZE = 10.0f;
	private static final Color REIMU_OPTION_COLOR = new Color(255, 180, 200);
	private static final int REIMU_OPTION_SHOOT_INTERVAL = 2;
	private static final int REIMU_OPTION_BULLET_DAMAGE = 1;
	private static final float BULLET_SPEED = 46.0f;
	private static final float BULLET_SIZE = 3.0f;
	private static final Color BULLET_COLOR = new Color(255, 150, 200);

	public ReimuOption(Player player, float offsetX, float offsetY, GameCanvas gameCanvas) {
		super(player, offsetX, offsetY, gameCanvas);
		setSize(REIMU_OPTION_SIZE);
		setColor(REIMU_OPTION_COLOR);
		setShootInterval(REIMU_OPTION_SHOOT_INTERVAL);
		setBulletDamage(REIMU_OPTION_BULLET_DAMAGE);
		setFollowSpeed(0.2f);
	}

	@Override
	protected void shoot() {
		if (gameCanvas == null) return;

		// 发射追踪弹（追踪敌人）
		float initialAngle = (float)(Math.PI / 2); // 向上
		PlayerTrackingBullet bullet = new PlayerTrackingBullet(x, y, BULLET_SPEED, initialAngle, 0.15f, BULLET_SIZE, BULLET_COLOR);
		bullet.setDamage(bulletDamage);
		stg.game.GameWorld world = (stg.game.GameWorld) gameCanvas.getWorld();
		if (world != null) {
			world.addPlayerBullet(bullet);
		}
	}

	@Override
	public void render(Graphics2D g) {
		float screenX = x + gameCanvas.getWidth() / 2.0f;
		float screenY = gameCanvas.getHeight() / 2.0f - y;

		stg.util.RenderUtils.enableAntiAliasing(g);

		// 绘制灵梦子机主体（粉红色�?		g.setColor(color);
		g.fillOval((int)(screenX - size), (int)(screenY - size),
		          (int)(size * 2), (int)(size * 2));

		// 绘制灵梦标志性的红色边框
		g.setColor(new Color(220, 50, 50));
		g.drawOval((int)(screenX - size), (int)(screenY - size),
		          (int)(size * 2), (int)(size * 2));

		// 绘制子机核心（白色亮点）
		g.setColor(new Color(255, 255, 255, 220));
		g.fillOval((int)(screenX - size * 0.5f), (int)(screenY - size * 0.5f),
		          (int)(size), (int)(size));

		// 绘制灵梦的阴阳图案（简化版�?		g.setColor(new Color(255, 255, 255, 150));
		g.fillArc((int)(screenX - size * 0.3f), (int)(screenY - size * 0.3f),
		          (int)(size * 0.6f), (int)(size * 0.6f), 0, 180);
	}
}

