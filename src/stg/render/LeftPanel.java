package stg.render;

import stg.base.KeyStateProvider;

/**
 * 左侧面板
 * 位于窗口左侧，用于显示游戏状态信息和虚拟键盘
 * @since 2026-02-23
 * @author JavaSTG Team
 */
public class LeftPanel extends Panel {
	/** 虚拟键盘渲染器 */
	private VirtualKeyboardRenderer virtualKeyboard;
	
	/**
	 * 构造函数
	 * @param x 面板X坐标
	 * @param y 面板Y坐标
	 * @param width 面板宽度
	 * @param height 面板高度
	 */
	public LeftPanel(int x, int y, int width, int height) {
		super(x, y, width, height);
		setBackgroundColor(0.1f, 0.1f, 0.15f, 1.0f);
		virtualKeyboard = new VirtualKeyboardRenderer();
	}
	
	/**
	 * 设置按键状态提供者
	 * @param provider 按键状态提供者
	 */
	public void setKeyStateProvider(KeyStateProvider provider) {
		virtualKeyboard.setKeyStateProvider(provider);
	}
	
	/**
	 * 渲染左侧面板
	 * @param renderer 渲染器
	 */
	@Override
	public void render(IRenderer renderer) {
		renderBackground(renderer);
		virtualKeyboard.render(renderer, width, height);
	}
}
