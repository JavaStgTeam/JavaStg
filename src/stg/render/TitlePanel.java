package stg.render;

import java.awt.Color;
import java.awt.Font;

import stg.base.KeyStateProvider;

/**
 * 标题页面面板
 * 显示游戏标题和主菜单
 * @since 2026-02-23
 * @author JavaSTG Team
 */
public class TitlePanel extends Panel {
	private static final Color BG_COLOR = new Color(10, 10, 20);
	private static final Color SELECTED_COLOR = new Color(255, 200, 100);
	private static final Color UNSELECTED_COLOR = new Color(200, 200, 200);

	private enum MenuState {
		MAIN_MENU
	}

	private static final String[] MAIN_MENU_ITEMS = {
		"开始游戏",
		"退出游戏"
	};

	private int selectedIndex = 0;
	private MenuState currentState = MenuState.MAIN_MENU;
	private int animationFrame = 0;

	public interface TitleCallback {
		void onGameStart();
		void onExit();
	}

	private TitleCallback callback;
	private KeyStateProvider keyStateProvider;

	/**
	 * 构造函数
	 * @param x 面板X坐标
	 * @param y 面板Y坐标
	 * @param width 面板宽度
	 * @param height 面板高度
	 * @param callback 标题回调
	 */
	public TitlePanel(int x, int y, int width, int height, TitleCallback callback) {
		super(x, y, width, height);
		setBackgroundColor(0.04f, 0.04f, 0.08f, 1.0f);
		this.callback = callback;
	}

	/**
	 * 设置按键状态提供者
	 * @param keyStateProvider 按键状态提供者
	 */
	public void setKeyStateProvider(KeyStateProvider keyStateProvider) {
		this.keyStateProvider = keyStateProvider;
	}

	/**
	 * 处理键盘输入
	 */
	public void handleInput() {
		if (keyStateProvider == null) return;

		// 处理上下选择
		if (keyStateProvider.isUpPressed()) {
			selectedIndex = (selectedIndex - 1 + MAIN_MENU_ITEMS.length) % MAIN_MENU_ITEMS.length;
			// 防止重复触发
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		} else if (keyStateProvider.isDownPressed()) {
			selectedIndex = (selectedIndex + 1) % MAIN_MENU_ITEMS.length;
			// 防止重复触发
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

		// 处理确认
		if (keyStateProvider.isZPressed()) {
			handleSelection();
			// 防止重复触发
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

		// 处理退出
		if (keyStateProvider.isXPressed()) {
			if (callback != null) {
				callback.onExit();
			}
		}
	}

	/**
	 * 处理菜单选择
	 */
	private void handleSelection() {
		switch (selectedIndex) {
		case 0:
			// 开始游戏
			if (callback != null) {
				callback.onGameStart();
			}
			break;
		case 1:
			// 退出游戏
			if (callback != null) {
				callback.onExit();
			}
			break;
		}
	}

	/**
	 * 渲染标题页面
	 * @param renderer 渲染器
	 */
	@Override
	public void render(IRenderer renderer) {
		renderBackground(renderer);

		// 绘制标题
		drawTitle(renderer);

		// 绘制菜单
		drawMenu(renderer);

		// 绘制操作提示
		drawHints(renderer);

		// 更新动画帧
		animationFrame++;
	}

	/**
	 * 绘制标题
	 * @param renderer 渲染器
	 */
	private void drawTitle(IRenderer renderer) {
		// 获取字体管理器实例
		FontManager fontManager = FontManager.getInstance();
		
		// 绘制主标题
		Font titleFont = fontManager.getTitleFont();
		String title = "东方STG引擎";
		float titleX = getWidth() / 2 - 150;
		float titleY = getHeight() / 3;

		renderer.drawText(title, titleX, titleY, titleFont, Color.WHITE);

		// 绘制副标题
		Font subtitleFont = fontManager.getFont(18f, Font.PLAIN);
		String subtitle = "Version 1.0";
		float subtitleX = getWidth() / 2 - 60;
		float subtitleY = titleY + 60;

		renderer.drawText(subtitle, subtitleX, subtitleY, subtitleFont, Color.GRAY);
	}

	/**
	 * 绘制菜单
	 * @param renderer 渲染器
	 */
	private void drawMenu(IRenderer renderer) {
		// 获取字体管理器实例
		FontManager fontManager = FontManager.getInstance();
		Font menuFont = fontManager.getMenuFont();

		for (int i = 0; i < MAIN_MENU_ITEMS.length; i++) {
			String item = MAIN_MENU_ITEMS[i];
			float x = getWidth() / 2 - 80;
			float y = getHeight() / 2 - 20 + i * 40;

			if (i == selectedIndex) {
				// 绘制选中效果
				renderer.drawText(">", x - 30, y, menuFont, SELECTED_COLOR);
				renderer.drawText(item, x, y, menuFont, SELECTED_COLOR);
			} else {
				renderer.drawText(item, x, y, menuFont, UNSELECTED_COLOR);
			}
		}
	}

	/**
	 * 绘制操作提示
	 * @param renderer 渲染器
	 */
	private void drawHints(IRenderer renderer) {
		// 获取字体管理器实例
		FontManager fontManager = FontManager.getInstance();
		Font hintFont = fontManager.getHintFont();
		String hint1 = "上下 选择菜单";
		String hint2 = "Z 确认  X 退出";

		float hintX = getWidth() / 2 - 100;
		float hintY1 = getHeight() * 2 / 3;
		float hintY2 = hintY1 + 20;

		renderer.drawText(hint1, hintX, hintY1, hintFont, Color.GRAY);
		renderer.drawText(hint2, hintX, hintY2, hintFont, Color.GRAY);
	}

	/**
	 * 更新标题页面
	 */
	public void update() {
		handleInput();
	}
}