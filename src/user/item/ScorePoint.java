package user.item;

import java.awt.*;
import stg.game.ui.GameCanvas;
import stg.game.player.Player;
import stg.game.item.Item;

public class ScorePoint extends Item {
	private static final float SCORE_POINT_SIZE = 8.0f;
	private static final Color SCORE_POINT_COLOR = new Color(100, 150, 255);
	private static final int SCORE_VALUE = 100;

	private int scoreValue;
	private boolean isLarge;

	/**
	 * Builder 模式 - 用于简化ScorePoint 的创建
	 */
	public static class Builder {
		private final float x;
		private final float y;
		private float vx = 0;
		private float vy = 0;
		private int scoreValue = SCORE_VALUE;
		private boolean isLarge = false;
		private GameCanvas gameCanvas = null;
		
		/**
		 * 构造器
		 * @param x X坐标
		 * @param y Y坐标
		 */
		public Builder(float x, float y) {
			this.x = x;
			this.y = y;
		}
		
		/**
		 * 设置速度
		 * @param vx X方向速度
		 * @param vy Y方向速度
		 * @return Builder 实例
		 */
		public Builder velocity(float vx, float vy) {
			this.vx = vx;
			this.vy = vy;
			return this;
		}
		
		/**
		 * 设置分数值
		 * @param scoreValue 分数值
		 * @return Builder 实例
		 */
		public Builder scoreValue(int scoreValue) {
			this.scoreValue = scoreValue;
			return this;
		}
		
		/**
		 * 设置是否为大分数值
		 * @param isLarge 是否为大分数值
		 * @return Builder 实例
		 */
		public Builder large(boolean isLarge) {
			this.isLarge = isLarge;
			return this;
		}
		
		/**
		 * 设置游戏画布
		 * @param gameCanvas 游戏画布
		 * @return Builder 实例
		 */
		public Builder gameCanvas(GameCanvas gameCanvas) {
			this.gameCanvas = gameCanvas;
			return this;
		}
		
		/**
		 * 构建 ScorePoint 实例
		 * @return ScorePoint 实例
		 */
		public ScorePoint build() {
			return new ScorePoint(x, y, vx, vy, scoreValue, isLarge, gameCanvas);
		}
	}

	public ScorePoint(float x, float y) {
		this(x, y, SCORE_VALUE, false, null);
	}

	public ScorePoint(float x, float y, int scoreValue) {
		this(x, y, scoreValue, false, null);
	}

	public ScorePoint(float x, float y, boolean isLarge) {
		this(x, y, SCORE_VALUE, isLarge, null);
	}

	public ScorePoint(float x, float y, int scoreValue, boolean isLarge) {
		this(x, y, scoreValue, isLarge, null);
	}

	public ScorePoint(float x, float y, GameCanvas gameCanvas) {
		this(x, y, SCORE_VALUE, false, gameCanvas);
	}

	public ScorePoint(float x, float y, int scoreValue, GameCanvas gameCanvas) {
		this(x, y, scoreValue, false, gameCanvas);
	}

	public ScorePoint(float x, float y, boolean isLarge, GameCanvas gameCanvas) {
		this(x, y, SCORE_VALUE, isLarge, gameCanvas);
	}

	public ScorePoint(float x, float y, int scoreValue, boolean isLarge, GameCanvas gameCanvas) {
		super(x, y, 0, 0, isLarge ? SCORE_POINT_SIZE * 1.5f : SCORE_POINT_SIZE, SCORE_POINT_COLOR, gameCanvas);
		this.scoreValue = scoreValue;
		this.isLarge = isLarge;
		setAttractionParams(200.0f, 4.0f);
	}

	public ScorePoint(float x, float y, float vx, float vy, GameCanvas gameCanvas) {
		this(x, y, vx, vy, SCORE_VALUE, false, gameCanvas);
	}

	public ScorePoint(float x, float y, float vx, float vy, int scoreValue, GameCanvas gameCanvas) {
		this(x, y, vx, vy, scoreValue, false, gameCanvas);
	}

	public ScorePoint(float x, float y, float vx, float vy, boolean isLarge, GameCanvas gameCanvas) {
		this(x, y, vx, vy, SCORE_VALUE, isLarge, gameCanvas);
	}

	public ScorePoint(float x, float y, float vx, float vy, int scoreValue, boolean isLarge, GameCanvas gameCanvas) {
		super(x, y, vx, vy, isLarge ? SCORE_POINT_SIZE * 1.5f : SCORE_POINT_SIZE, SCORE_POINT_COLOR, gameCanvas);
		this.scoreValue = scoreValue;
		this.isLarge = isLarge;
		setAttractionParams(200.0f, 4.0f);
	}

	/**
	 * 初始化行为参数
	 */
	@Override
	protected void initBehavior() {
		// 初始化行为参数
	}

	/**
	 * 实现每帧的自定义更新逻辑
	 */
	@Override
	protected void onUpdate() {
		applyAttraction();
	}

	/**
	 * 实现自定义移动逻辑
	 */
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

		g.setColor(color);
		g.fillOval((int)(screenX - size), (int)(screenY - size), (int)(size * 2), (int)(size * 2));

		g.setColor(new Color(150, 200, 255, 100));
		g.fillOval((int)(screenX - size * 1.2f), (int)(screenY - size * 1.2f), (int)(size * 2.4f), (int)(size * 2.4f));

		g.setColor(new Color(255, 255, 255, 200));
		g.fillOval((int)(screenX - size * 0.3f), (int)(screenY - size * 0.3f), (int)(size * 0.6f), (int)(size * 0.6f));

		if (isLarge) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", Font.BOLD, (int)(size * 0.8f)));
			String text = String.valueOf(scoreValue);
			FontMetrics fm = g.getFontMetrics();
			int textWidth = fm.stringWidth(text);
			int textHeight = fm.getHeight();
			g.drawString(text, (int)(screenX - textWidth / 2), (int)(screenY + textHeight / 4));
		}
	}

	@Override
	public void onCollect() {
		super.onCollect();

		if (gameCanvas != null) {
			Player player = gameCanvas.getPlayer();
			if (player != null) {
				System.out.println("ScorePoint collected! Score +" + scoreValue);
			}
		}
	}

	public int getScoreValue() {
		return scoreValue;
	}

	public void setScoreValue(int scoreValue) {
		this.scoreValue = scoreValue;
	}

	public boolean isLarge() {
		return isLarge;
	}

	public void setLarge(boolean isLarge) {
		this.isLarge = isLarge;
		this.size = isLarge ? SCORE_POINT_SIZE * 1.5f : SCORE_POINT_SIZE;
	}

	public static ScorePoint createSmall(float x, float y, GameCanvas gameCanvas) {
		return new ScorePoint(x, y, SCORE_VALUE, false, gameCanvas);
	}

	public static ScorePoint createLarge(float x, float y, GameCanvas gameCanvas) {
		return new ScorePoint(x, y, SCORE_VALUE * 10, true, gameCanvas);
	}

	public static ScorePoint createCustom(float x, float y, int scoreValue, GameCanvas gameCanvas) {
		boolean isLarge = scoreValue >= SCORE_VALUE * 10;
		return new ScorePoint(x, y, scoreValue, isLarge, gameCanvas);
	}
}
