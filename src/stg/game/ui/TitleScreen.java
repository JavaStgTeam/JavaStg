package stg.game.ui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import stg.base.KeyStateProvider;
import stg.util.AudioManager;
import stg.util.ResourceManager;

/**
 * 标题界面 - 游戏主菜单和角色选择
 * 将类移动到stg.game.ui包内，保持与其他UI组件的一致性
 * @Time 2026-01-20 实现KeyStateProvider以支持虚拟键盘 * @Time 2026-01-24 添加背景图片支持\n\t * @since 2026-01-20
 * 添加标题音乐播放功能\n\t * @since 2026-01-27
 */
public class TitleScreen extends JPanel implements KeyStateProvider {
	private static final long serialVersionUID = 1L;
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
	private Timer animationTimer;
	private int animationFrame = 0;
	private BufferedImage backgroundImage;
	private ResourceManager resourceManager;
	private AudioManager audioManager;

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
	void onGameStart(stg.game.stage.StageGroup stageGroup);
		void onExit();
	}

	private TitleCallback callback;

	public TitleScreen(TitleCallback callback) {
		this.callback = callback;
		this.resourceManager = ResourceManager.getInstance();
		this.audioManager = AudioManager.getInstance();
		loadBackgroundImage();
		playTitleMusic();
		
		setFocusable(true);
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				handleKeyPress(e);
			}
		});

		animationTimer = new Timer(16, e -> {
			animationFrame++;
			repaint();
		});
		animationTimer.start();
	}
	
	private void loadBackgroundImage() {
		backgroundImage = resourceManager.loadImage("ui_bg.png", "images");
		if (backgroundImage == null) {
			System.out.println("【警告】UI背景图片加载失败，使用默认背景色");
		} else {
			System.out.println("【资源】UI背景图片加载成功: " + 
				backgroundImage.getWidth() + "x" + backgroundImage.getHeight());
		}
	}
	
	private void playTitleMusic() {
		try {
			audioManager.playMusic("luastg.wav", true);
			System.out.println("【音频】标题音乐播放中（WAV 格式）");	
		} catch (Exception wavError) {
			System.out.println("【警告】WAV 音乐播放失败，跳过背景音乐 " + wavError.getMessage());
		}
	}
	
	public void stopTitleMusic() {
		audioManager.stopMusic();
		System.out.println("【音频】标题音乐已停止");
	}

	private void handleKeyPress(KeyEvent e) {
		switch (currentState) {
			case MAIN_MENU:
				handleMainMenuKey(e);
				break;
		}
	}

	private void handleMainMenuKey(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				selectedIndex = (selectedIndex - 1 + MAIN_MENU_ITEMS.length) % MAIN_MENU_ITEMS.length;
				repaint();
				break;
			case KeyEvent.VK_DOWN:
				selectedIndex = (selectedIndex + 1) % MAIN_MENU_ITEMS.length;
				repaint();
				break;
			case KeyEvent.VK_Z:
			case KeyEvent.VK_ENTER:
				handleMainMenuSelection();
				break;
			case KeyEvent.VK_ESCAPE:
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
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		int width = getWidth();
		int height = getHeight();

		// 绘制背景图片
		if (backgroundImage != null) {
			// 缩放图片以适应窗口
			g2d.drawImage(backgroundImage, 0, 0, width, height, null);
		} else {
			// 默认背景颜色
			g2d.setColor(BG_COLOR);
			g2d.fillRect(0, 0, width, height);
		}

		// 绘制标题
		stg.util.RenderUtils.enableAntiAliasing(g2d);
		g2d.setFont(new Font("Microsoft YaHei", Font.BOLD, 48));
		g2d.setColor(Color.WHITE);
		String title = "东方STG引擎";
		int titleWidth = g2d.getFontMetrics().stringWidth(title);
		g2d.drawString(title, width / 2 - titleWidth / 2, 100);

		// 绘制版本信息
		g2d.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
		g2d.setColor(Color.GRAY);
		g2d.drawString("Version 1.0", width - 80, height - 10);

		// 绘制菜单
		switch (currentState) {
			case MAIN_MENU:
				drawMainMenu(g2d, width, height);
				break;
		}
	}

	private void drawMainMenu(Graphics2D g2d, int width, int height) {
		g2d.setFont(new Font("Microsoft YaHei", Font.BOLD, 24));

		for (int i = 0; i < MAIN_MENU_ITEMS.length; i++) {
			String item = MAIN_MENU_ITEMS[i];
			int itemWidth = g2d.getFontMetrics().stringWidth(item);
			int x = width / 2 - itemWidth / 2;
			int y = height / 2 + i * 40;

			if (i == selectedIndex) {
				g2d.setColor(SELECTED_COLOR);
				// 绘制选中效果
				g2d.drawString(">", x - 30, y);
			} else {
				g2d.setColor(UNSELECTED_COLOR);
			}
			g2d.drawString(item, x, y);
		}

		// 绘制操作提示
		g2d.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
		g2d.setColor(Color.GRAY);
		g2d.drawString("上下 选择菜单", width / 2 - 80, height - 40);
		g2d.drawString("Z/Enter 确认", width / 2 - 80, height - 20);
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

