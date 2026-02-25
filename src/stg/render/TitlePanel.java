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
	private int backgroundTextureId = -1;
	private boolean upPressed = false;
	private boolean downPressed = false;
	private boolean zPressed = false;
	private boolean xPressed = false;

	public interface TitleCallback {
		void onGameStart();
		void onExit();
	}

	/**
	 * 重置按键状态
	 */
	public void resetKeyStates() {
		upPressed = false;
		downPressed = false;
		zPressed = false;
		xPressed = false;
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
	 * 加载背景纹理
	 * @param renderer 渲染器实例
	 */
	public void loadBackgroundTexture(IRenderer renderer) {
		// 使用绝对路径加载背景图片
		String backgroundPath = "e:\\Myproject\\Game\\jstg_Team\\JavaStg\\resources\\images\\menu_bg.png";
		// 也可以尝试使用相对路径
		String relativePath = "resources/images/menu_bg.png";
		
		// 尝试使用绝对路径加载
		if (renderer instanceof GLRenderer) {
			GLRenderer glRenderer = (GLRenderer) renderer;
			backgroundTextureId = glRenderer.loadTexture(backgroundPath);
			
			// 如果绝对路径加载失败，尝试使用相对路径
			if (backgroundTextureId == -1) {
				System.out.println("使用绝对路径加载背景纹理失败，尝试使用相对路径");
				backgroundTextureId = glRenderer.loadTexture(relativePath);
			}
			
			if (backgroundTextureId != -1) {
				System.out.println("背景纹理加载成功，纹理ID: " + backgroundTextureId);
			} else {
				System.err.println("背景纹理加载失败");
			}
		} else {
			System.err.println("无效的渲染器实例");
		}
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
		boolean currentUpPressed = keyStateProvider.isUpPressed();
		if (currentUpPressed && !upPressed) {
			selectedIndex = (selectedIndex - 1 + MAIN_MENU_ITEMS.length) % MAIN_MENU_ITEMS.length;
			upPressed = true;
		} else if (!currentUpPressed) {
			upPressed = false;
		}

		boolean currentDownPressed = keyStateProvider.isDownPressed();
		if (currentDownPressed && !downPressed) {
			selectedIndex = (selectedIndex + 1) % MAIN_MENU_ITEMS.length;
			downPressed = true;
		} else if (!currentDownPressed) {
			downPressed = false;
		}

		// 处理确认
		boolean currentZPressed = keyStateProvider.isZPressed();
		if (currentZPressed && !zPressed) {
			handleSelection();
			zPressed = true;
		} else if (!currentZPressed) {
			zPressed = false;
		}

		// 处理退出
		boolean currentXPressed = keyStateProvider.isXPressed();
		if (currentXPressed && !xPressed) {
			if (callback != null) {
				callback.onExit();
			}
			xPressed = true;
		} else if (!currentXPressed) {
			xPressed = false;
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
		// 首先绘制背景图片
		drawBackgroundImage(renderer);

		// 然后绘制标题和菜单
		drawTitle(renderer);
		drawMenu(renderer);
		drawHints(renderer);

		// 更新动画帧
		animationFrame++;
	}
	
	/**
	 * 绘制背景图片
	 * @param renderer 渲染器
	 */
	public void drawBackgroundImage(IRenderer renderer) {
		if (backgroundTextureId != -1) {
			// 绘制背景图片，覆盖整个面板
			renderer.drawImage(backgroundTextureId, 0, 0, getWidth(), getHeight());
		}
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
		String title = "JavaSTG";
		float titleX = getWidth() / 2 - 100;
		float titleY = getHeight() - getHeight() / 3;

		// 绘制黑色边框
		renderer.drawText(title, titleX - 2, titleY - 2, titleFont, Color.BLACK);
		renderer.drawText(title, titleX + 2, titleY - 2, titleFont, Color.BLACK);
		renderer.drawText(title, titleX - 2, titleY + 2, titleFont, Color.BLACK);
		renderer.drawText(title, titleX + 2, titleY + 2, titleFont, Color.BLACK);
		// 绘制白色文字
		renderer.drawText(title, titleX, titleY, titleFont, Color.WHITE);

		// 绘制副标题
		Font subtitleFont = fontManager.getFont(18f, Font.PLAIN);
		String subtitle = "Version 1.0";
		float subtitleX = getWidth() / 2 - 60;
		float subtitleY = titleY - 60;

		// 绘制黑色边框
		renderer.drawText(subtitle, subtitleX - 1, subtitleY - 1, subtitleFont, Color.BLACK);
		renderer.drawText(subtitle, subtitleX + 1, subtitleY - 1, subtitleFont, Color.BLACK);
		renderer.drawText(subtitle, subtitleX - 1, subtitleY + 1, subtitleFont, Color.BLACK);
		renderer.drawText(subtitle, subtitleX + 1, subtitleY + 1, subtitleFont, Color.BLACK);
		// 绘制灰色文字
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
			float y = getHeight() - getHeight() / 2 - 20 - i * 40;

			if (i == selectedIndex) {
				// 绘制选中效果
				// 绘制黑色边框
				renderer.drawText(">", x - 30 - 1, y - 1, menuFont, Color.BLACK);
				renderer.drawText(">", x - 30 + 1, y - 1, menuFont, Color.BLACK);
				renderer.drawText(">", x - 30 - 1, y + 1, menuFont, Color.BLACK);
				renderer.drawText(">", x - 30 + 1, y + 1, menuFont, Color.BLACK);
				// 绘制选中颜色
				renderer.drawText(">", x - 30, y, menuFont, SELECTED_COLOR);
				
				// 绘制黑色边框
				renderer.drawText(item, x - 1, y - 1, menuFont, Color.BLACK);
				renderer.drawText(item, x + 1, y - 1, menuFont, Color.BLACK);
				renderer.drawText(item, x - 1, y + 1, menuFont, Color.BLACK);
				renderer.drawText(item, x + 1, y + 1, menuFont, Color.BLACK);
				// 绘制选中颜色
				renderer.drawText(item, x, y, menuFont, SELECTED_COLOR);
			} else {
				// 绘制黑色边框
				renderer.drawText(item, x - 1, y - 1, menuFont, Color.BLACK);
				renderer.drawText(item, x + 1, y - 1, menuFont, Color.BLACK);
				renderer.drawText(item, x - 1, y + 1, menuFont, Color.BLACK);
				renderer.drawText(item, x + 1, y + 1, menuFont, Color.BLACK);
				// 绘制未选中颜色
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
		float hintY1 = getHeight() - getHeight() * 2 / 3;
		float hintY2 = hintY1 - 20;

		// 绘制黑色边框
		renderer.drawText(hint1, hintX - 1, hintY1 - 1, hintFont, Color.BLACK);
		renderer.drawText(hint1, hintX + 1, hintY1 - 1, hintFont, Color.BLACK);
		renderer.drawText(hint1, hintX - 1, hintY1 + 1, hintFont, Color.BLACK);
		renderer.drawText(hint1, hintX + 1, hintY1 + 1, hintFont, Color.BLACK);
		// 绘制灰色文字
		renderer.drawText(hint1, hintX, hintY1, hintFont, Color.GRAY);

		// 绘制黑色边框
		renderer.drawText(hint2, hintX - 1, hintY2 - 1, hintFont, Color.BLACK);
		renderer.drawText(hint2, hintX + 1, hintY2 - 1, hintFont, Color.BLACK);
		renderer.drawText(hint2, hintX - 1, hintY2 + 1, hintFont, Color.BLACK);
		renderer.drawText(hint2, hintX + 1, hintY2 + 1, hintFont, Color.BLACK);
		// 绘制灰色文字
		renderer.drawText(hint2, hintX, hintY2, hintFont, Color.GRAY);
	}

	/**
	 * 更新标题页面
	 */
	public void update() {
		handleInput();
	}
}