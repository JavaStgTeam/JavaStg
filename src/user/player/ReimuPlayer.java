package user.player;

import java.awt.*;
import stg.game.player.Player;
import stg.game.ui.GameCanvas;

/**
 * 灵梦自机类
 * 灵符-梦想封印：高火力广域攻击
 */
public class ReimuPlayer extends Player {
	private static final float REIMU_SPEED = 4.5f;
	private static final float REIMU_SPEED_SLOW = 2.0f;
	private static final float REIMU_SIZE = 18f;
	private static final int REIMU_SHOOT_INTERVAL = 1;
	private static final Color REIMU_COLOR = new Color(255, 200, 220);
	private static final int REIMU_BULLET_DAMAGE = 1; // 灵梦子弹伤害

	public ReimuPlayer(float spawnX, float spawnY) {
		super(spawnX, spawnY);
		setSpeed(REIMU_SPEED);
		setSpeedSlow(REIMU_SPEED_SLOW);
		this.size = REIMU_SIZE;
		setShootInterval(REIMU_SHOOT_INTERVAL);
		this.bulletDamage = REIMU_BULLET_DAMAGE; // 设置灵梦子弹伤害
	}

	/**
	 * 初始化灵梦的子机
	 * 参考东方正作，灵梦通常有2-4个子机
	 */
	public void initializeOptions(GameCanvas canvas) {
		this.gameCanvas = canvas;

		// 添加2个子机，位于玩家左右两侧
		ReimuOption option1 = new ReimuOption(this, -25, 10, canvas);
		ReimuOption option2 = new ReimuOption(this, 25, 10, canvas);

		addOption(option1);
		addOption(option2);

		System.out.println("灵梦子机初始化完成，2个子机");
	}

	@Override
	protected void shoot() {
		// 使用默认自机的射击实�?		super.shoot();
	}

	@Override
	public void render(Graphics2D g) {
		float[] screenCoords = toScreenCoords(getX(), getY());
		float screenX = screenCoords[0];
		float screenY = screenCoords[1];

		stg.util.RenderUtils.enableAntiAliasing(g);

		// 无敌闪烁效果：每5帧闪烁一次
		boolean shouldRender = true;
		if (isInvincible()) {
			// 由于invincibleTimer是private，我们使用isInvincible()来控制闪烁
			int flashPhase = frame % 10;
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

		// 灵梦外观：粉红色球体
		if (shouldRender) {
			g.setColor(REIMU_COLOR);
			g.fillOval((int)(screenX - getSize()), (int)(screenY - getSize()),
			          (int)(getSize() * 2), (int)(getSize() * 2));

			// 添加灵梦标志性的红色头带
			g.setColor(new Color(220, 50, 50));
			g.fillRect((int)(screenX - getSize() + 2), (int)(screenY - getSize() - 2),
			          (int)(getSize() * 2 - 4), 4);
		}
	}
}

