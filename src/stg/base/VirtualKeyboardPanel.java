package stg.base;

import java.awt.*;
import javax.swing.*;
import stg.game.ui.GameCanvas;

/**
 * 虚拟键盘面板 - 显示按键状态
 * 支持KeyStateProvider接口,使标题界面也能显示按键
 * @since 2026-01-20
 */
public class VirtualKeyboardPanel extends JPanel {
	private KeyStateProvider keyStateProvider;
	private Timer updateTimer;
	private int keyWidth = 45;
	private int keyHeight = 45;
	private int keyGap = 6;

	private boolean upPressed = false;
	private boolean downPressed = false;
	private boolean leftPressed = false;
	private boolean rightPressed = false;
	private boolean shiftPressed = false;
	private boolean zPressed = false;
	private boolean xPressed = false;

	public VirtualKeyboardPanel(GameCanvas gameCanvas) {
		this.keyStateProvider = gameCanvas;
		initialize();
	}

	/**
	 * 构造函�?- 接受KeyStateProvider
	 * 支持标题界面按键显示\n\t * @since 2026-01-20
	 */
	public VirtualKeyboardPanel(KeyStateProvider keyStateProvider) {
		this.keyStateProvider = keyStateProvider;
		initialize();
	}

	private void initialize() {
		setBackground(new Color(30, 30, 40));
		setLayout(new BorderLayout());

		JPanel keyboardPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				stg.util.RenderUtils.enableAntiAliasing(g2d);
				drawKeyboard(g2d);
			}
		};
		keyboardPanel.setBackground(new Color(30, 30, 40));
		add(keyboardPanel, BorderLayout.CENTER);

		updateTimer = new Timer(50, e -> {
			updateKeyStates();
			repaint();
		});
		updateTimer.start();
	}

	/**
	 * 更新按键状�?	 */
	private void updateKeyStates() {
		if (keyStateProvider == null) return;
		upPressed = keyStateProvider.isUpPressed();
		downPressed = keyStateProvider.isDownPressed();
		leftPressed = keyStateProvider.isLeftPressed();
		rightPressed = keyStateProvider.isRightPressed();
		zPressed = keyStateProvider.isZPressed();
		shiftPressed = keyStateProvider.isShiftPressed();
		xPressed = keyStateProvider.isXPressed();
	}

	/**
	 * 绘制虚拟键盘
	 */
	private void drawKeyboard(Graphics2D g) {
		int panelWidth = getWidth();
		int panelHeight = getHeight();

		int centerX = panelWidth / 2;
		int centerY = panelHeight / 2;

		int upX = centerX - 55;
		int upY = centerY - 50;

		int downX = upX;
		int downY = upY + keyHeight + keyGap;

		int leftX = upX - keyWidth - keyGap;
		int leftY = downY;

		int rightX = upX + keyWidth + keyGap;
		int rightY = downY;

		drawKey(g, upX, upY, keyWidth, keyHeight, "↑", upPressed);
		drawKey(g, downX, downY, keyWidth, keyHeight, "↓", downPressed);
		drawKey(g, leftX, leftY, keyWidth, keyHeight, "←", leftPressed);
		drawKey(g, rightX, rightY, keyWidth, keyHeight, "→", rightPressed);

		int funcX = centerX + 25;
		int funcY = centerY - 50;

		drawKey(g, funcX, funcY, keyWidth * 2 + keyGap, keyHeight, "Shift", shiftPressed);

		int zxY = funcY + keyHeight + keyGap + 15;
		drawKey(g, funcX, zxY, keyWidth, keyHeight, "Z", zPressed);
		drawKey(g, funcX + keyWidth + keyGap, zxY, keyWidth, keyHeight, "X", xPressed);
	}

	/**
	 * 绘制单个按键
	 */
	private void drawKey(Graphics2D g, int x, int y, int width, int height, String text, boolean pressed) {

		if (pressed) {
			g.setColor(new Color(100, 200, 100));
			g.fillRect(x, y, width, height);

			g.setColor(new Color(80, 180, 80));
			g.fillRect(x + 2, y + 2, width - 4, height - 4);

			g.setColor(new Color(60, 160, 60));
			g.fillRect(x + 4, y + 4, width - 8, height - 8);
		} else {
			g.setColor(new Color(60, 60, 80));
			g.fillRect(x, y, width, height);

			g.setColor(new Color(100, 100, 120));
			g.drawRect(x, y, width, height);
		}

		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 12));
		FontMetrics fm = g.getFontMetrics();
		int textWidth = fm.stringWidth(text);
		int textHeight = fm.getHeight();

		int textX = x + (width - textWidth) / 2;
		int textY = y + (height - textHeight) / 2 + fm.getAscent();

		if (pressed) {
			textX += 2;
			textY += 2;
		}

		g.drawString(text, textX, textY);
	}

	/**
	 * 设置按键状态提供者
	 * 支持切换到标题界面
	 * @since 2026-01-20
	 * */
	public void setKeyStateProvider(KeyStateProvider provider) {
		this.keyStateProvider = provider;
	}

	public void stop() {
		if (updateTimer != null) {
			updateTimer.stop();
		}
	}
}

