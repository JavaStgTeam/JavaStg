package stg.render;

/**
 * 右侧面板
 * 位于窗口右侧，用于显示游戏状态信息
 * @since 2026-02-23
 * @author JavaSTG Team
 */
public class RightPanel extends Panel {
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
	 * 渲染右侧面板
	 * @param renderer 渲染器
	 */
	@Override
	public void render(IRenderer renderer) {
		renderBackground(renderer);
	}
}
