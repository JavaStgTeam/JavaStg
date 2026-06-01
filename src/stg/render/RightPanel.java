package stg.render;

import java.awt.Color;
import java.awt.Font;

import stg.core.ScoreManager;

/**
 * 右侧面板
 * 位于窗口右侧，用于显示游戏状态信息
 * @since 2026-02-23
 * @author JavaSTG Team
 */
public class RightPanel extends Panel {

	private ScoreManager scoreManager;

	/**
	 * 构造函数
	 * @param x 面板X坐标
	 * @param y 面板Y坐标
	 * @param width 面板宽度
	 * @param height 面板高度
	 */
	public RightPanel(int x, int y, int width, int height) {
		super(x, y, width, height);
		setBackgroundColor(0.1f, 0.1f, 0.15f, 1.0f);
	}

	/**
	 * 设置分数管理器
	 * @param scoreManager 分数管理器实例
	 */
	public void setScoreManager(ScoreManager scoreManager) {
		this.scoreManager = scoreManager;
	}

	/**
	 * 渲染右侧面板
	 * @param renderer 渲染器
	 */
	@Override
	public void render(IRenderer renderer) {
		renderBackground(renderer);

		if (scoreManager != null) {
			Font labelFont = FontManager.getInstance().getFont(18f, Font.BOLD);
			Font valueFont = FontManager.getInstance().getFont(20f, Font.PLAIN);
			int panelHeight = getHeight();
			int panelWidth = getWidth();
			int rightX = panelWidth - 15;

			renderer.drawText("Score", 15, panelHeight - 40, labelFont, Color.WHITE);
			String scoreText = ScoreManager.formatScore(scoreManager.getScore());
			renderer.drawText(scoreText, rightX, panelHeight - 40, valueFont, Color.WHITE);

			renderer.drawText("Hi-Score", 15, panelHeight - 100, labelFont, Color.WHITE);
			String hiScoreText = ScoreManager.formatScore(scoreManager.getHighScore());
			renderer.drawText(hiScoreText, rightX, panelHeight - 100, valueFont, Color.WHITE);

			renderer.drawText("Graze", 15, panelHeight - 160, labelFont, Color.WHITE);
			String grazeText = ScoreManager.formatScore(scoreManager.getGraze());
			renderer.drawText(grazeText, rightX, panelHeight - 160, valueFont, Color.WHITE);
		}
	}
}
