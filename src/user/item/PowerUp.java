package user.item;

import java.awt.*;
import stg.game.ui.GameCanvas;
import stg.game.player.Player;
import stg.game.item.Item;

/**
 * 强化道具类- 增加玩家火力
 */
public class PowerUp extends Item {
	private static final float POWERUP_SIZE = 12.0f;
	private static final Color POWERUP_COLOR = new Color(255, 200, 50);
	private static final int POWER_VALUE = 1; // 增加的火力值
	public PowerUp(float x, float y) {
		super(x, y, POWERUP_SIZE, POWERUP_COLOR);
		setAttractionParams(150.0f, 3.0f);
	}

	public PowerUp(float x, float y, float vx, float vy) {
		super(x, y, vx, vy, POWERUP_SIZE, POWERUP_COLOR);
		setAttractionParams(150.0f, 3.0f);
	}

	public PowerUp(float x, float y, GameCanvas gameCanvas) {
		super(x, y, POWERUP_SIZE, POWERUP_COLOR, gameCanvas);
		setAttractionParams(150.0f, 3.0f);
	}

	@Override
	protected void initBehavior() {
		// 初始化行为参数
	}
	
	@Override
	protected void onUpdate() {
		applyAttraction();
	}
	
	@Override
	protected void onMove() {
		// 自定义移动逻辑
	}
	
	@Override
	public void update() {
		super.update();
	}

	@Override
	public void render(Graphics2D g) {
		if (!active) return;

		float[] screenCoords = toScreenCoords(x, y);
		float screenX = screenCoords[0];
		float screenY = screenCoords[1];

		stg.util.RenderUtils.enableAntiAliasing(g);

		// 绘制P标志
		g.setColor(color);
		g.fillOval((int)(screenX - size), (int)(screenY - size), (int)(size * 2), (int)(size * 2));

		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, (int)(size * 1.2f)));
		String text = "P";
		FontMetrics fm = g.getFontMetrics();
		int textWidth = fm.stringWidth(text);
		int textHeight = fm.getHeight();
		g.drawString(text, (int)(screenX - textWidth / 2), (int)(screenY + textHeight / 4));
	}

	@Override
	public void onCollect() {
		super.onCollect();

		if (gameCanvas != null) {
			Player player = gameCanvas.getPlayer();
			if (player != null) {
				// 增加玩家火力（这里可以扩展Player类来支持火力系统）
				System.out.println("PowerUp collected! Power +1");
			}
		}
	}

	public int getPowerValue() {
		return POWER_VALUE;
	}
}

