package stg.ui;

import java.awt.Color;
import java.awt.Font;

import stg.base.KeyStateProvider;
import stg.service.render.IRenderer;
import stg.util.ALAudioManager;
import stg.util.ResourceManager;

/**
 * 标题界面 - 游戏主菜单和角色选择
 * @since 1.0
 * @author JavaSTG Team
 * @date 2026-01-20 实现KeyStateProvider以支持虚拟键盘，将类移动到stg.game.ui包内
 * @date 2026-01-24 添加背景图片支持
 * @date 2026-01-27 添加标题音乐播放功能
 */
public class TitleScreen implements KeyStateProvider {
	private static final Color BG_COLOR = new Color(10, 10, 20);
	private static final Color SELECTED_COLOR = new Color(255, 200, 100);
	private static final Color UNSELECTED_COLOR = new Color(200, 200, 200);

	private enum MenuState {
		MAIN_MENU,
		STAGE_GROUP_SELECT
	}

	private static final String[] MAIN_MENU_ITEMS = {
		"开始游戏",
		"退出游戏"
	};

	private int selectedIndex = 0;
	private MenuState currentState = MenuState.MAIN_MENU;
	private int animationFrame = 0;
	private ResourceManager resourceManager;
	private ALAudioManager audioManager;

	// 按键状态跟踪 - 供虚拟键盘使用
	private boolean upPressed = false;
	private boolean downPressed = false;
	private boolean leftPressed = false;
	private boolean rightPressed = false;
	private boolean zPressed = false;
	private boolean shiftPressed = false;
	private boolean xPressed = false;

	public interface TitleCallback {
		void onStageGroupSelect();
		void onGameStart(stg.stage.StageGroup stageGroup);
		void onExit();
	}

	private TitleCallback callback;

	public TitleScreen(TitleCallback callback) {
		this.callback = callback;
		this.resourceManager = ResourceManager.getInstance();
		this.audioManager = ALAudioManager.getInstance();
		playTitleMusic();
	}
	
	private void playTitleMusic() {
		try {
			audioManager.loadMusic("title", "resources/audio/music/luastg.wav");
			audioManager.playMusic("title", true);
			System.out.println("【音频】标题音乐播放中（WAV 格式）");
		} catch (Exception wavError) {
			System.out.println("【警告】WAV 音乐播放失败，跳过背景音乐 " + wavError.getMessage());
		}
	}
	
	public void stopTitleMusic() {
		audioManager.stopMusic("title");
		System.out.println("【音频】标题音乐已停止");
	}

	/**
	 * 处理键盘输入
	 * @param key 按键代码
	 */
	public void handleKeyPress(int key) {
		switch (currentState) {
			case MAIN_MENU:
				handleMainMenuKey(key);
				break;
		}
	}

	private void handleMainMenuKey(int key) {
		switch (key) {
			case org.lwjgl.glfw.GLFW.GLFW_KEY_UP:
				selectedIndex = (selectedIndex - 1 + MAIN_MENU_ITEMS.length) % MAIN_MENU_ITEMS.length;
				break;
			case org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN:
				selectedIndex = (selectedIndex + 1) % MAIN_MENU_ITEMS.length;
				break;
			case org.lwjgl.glfw.GLFW.GLFW_KEY_Z:
			case org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER:
				handleMainMenuSelection();
				break;
			case org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE:
				callback.onExit();
				break;
		}
	}

	private void handleMainMenuSelection() {
		switch (selectedIndex) {
			case 0:
				// 直接进入关卡组选择
				callback.onStageGroupSelect();
				break;
			case 1:
				callback.onExit();
				break;
		}
	}

	/**
	 * 使用OpenGL渲染器绘制标题界面
	 * @param renderer OpenGL渲染器
	 * @param width 窗口宽度
	 * @param height 窗口高度
	 */
	public void render(IRenderer renderer, int width, int height) {
		// 开始批量渲染以优化性能
		renderer.beginBatch();
		
		// 绘制背景 - 使用屏幕像素坐标
		renderer.setColor(BG_COLOR);
		renderer.drawRect(0, 0, width, height, BG_COLOR);

		// 绘制标题 - 使用屏幕像素坐标
		Font titleFont = new Font("Microsoft YaHei", Font.BOLD, 48);
		renderer.setFont(titleFont);
		renderer.setColor(Color.WHITE);
		String title = "东方STG引擎";
		renderer.drawText(title, width / 2 - 150, height / 2 + 100, titleFont, Color.WHITE);

		// 绘制版本信息 - 使用屏幕像素坐标
		Font versionFont = new Font("Microsoft YaHei", Font.PLAIN, 14);
		renderer.setFont(versionFont);
		renderer.setColor(Color.GRAY);
		renderer.drawText("Version 1.0", width - 100, height - 30, versionFont, Color.GRAY);

		// 绘制菜单
		switch (currentState) {
		case MAIN_MENU:
			drawMainMenu(renderer, width, height);
			break;
		}
		
		// 结束批量渲染
		renderer.endBatch();
	}

	private void drawMainMenu(IRenderer renderer, int width, int height) {
		Font menuFont = new Font("Microsoft YaHei", Font.BOLD, 24);
		renderer.setFont(menuFont);

		for (int i = 0; i < MAIN_MENU_ITEMS.length; i++) {
			String item = MAIN_MENU_ITEMS[i];
			// 使用屏幕像素坐标
			float x = width / 2 - 80;
			float y = height / 2 + 20 - i * 40;

			if (i == selectedIndex) {
				renderer.setColor(SELECTED_COLOR);
				// 绘制选中效果
				renderer.drawText(">", x - 30, y, menuFont, SELECTED_COLOR);
			} else {
				renderer.setColor(UNSELECTED_COLOR);
			}
			renderer.drawText(item, x, y, menuFont, i == selectedIndex ? SELECTED_COLOR : UNSELECTED_COLOR);
		}

		// 绘制操作提示
		Font hintFont = new Font("Microsoft YaHei", Font.PLAIN, 14);
		renderer.setFont(hintFont);
		renderer.setColor(Color.GRAY);
		renderer.drawText("上下 选择菜单", width / 2 - 100, height / 2 - 100, hintFont, Color.GRAY);
		renderer.drawText("Z/Enter 确认", width / 2 - 100, height / 2 - 120, hintFont, Color.GRAY);
	}

	/**
	 * 更新动画帧
	 */
	public void update() {
		animationFrame++;
	}

	// 虚拟键盘接口实现
	@Override
	public boolean isUpPressed() { return upPressed; }
	@Override
	public boolean isDownPressed() { return downPressed; }
	@Override
	public boolean isLeftPressed() { return leftPressed; }
	@Override
	public boolean isRightPressed() { return rightPressed; }
	@Override
	public boolean isZPressed() { return zPressed; }
	@Override
	public boolean isShiftPressed() { return shiftPressed; }
	@Override
	public boolean isXPressed() { return xPressed; }

	public void setUpPressed(boolean upPressed) { this.upPressed = upPressed; }
	public void setDownPressed(boolean downPressed) { this.downPressed = downPressed; }
	public void setLeftPressed(boolean leftPressed) { this.leftPressed = leftPressed; }
	public void setRightPressed(boolean rightPressed) { this.rightPressed = rightPressed; }
	public void setZPressed(boolean zPressed) { this.zPressed = zPressed; }
	public void setShiftPressed(boolean shiftPressed) { this.shiftPressed = shiftPressed; }
	public void setXPressed(boolean xPressed) { this.xPressed = xPressed; }
}

