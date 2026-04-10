package stg.render;

import java.awt.Color;
import java.awt.Font;

import stg.base.KeyStateProvider;

public class PauseMenu extends Panel {
	private static final Color BG_COLOR = new Color(10, 10, 20, 200);
	private static final Color SELECTED_COLOR = new Color(255, 200, 100);
	private static final Color UNSELECTED_COLOR = new Color(200, 200, 200);

	private static final String[] MENU_ITEMS = {
		"继续游戏",
		"返回标题",
		"重新开始"
	};

	private int selectedIndex = 0;
	private boolean upPressed = false;
	private boolean downPressed = false;
	private boolean zPressed = false;
	private boolean escPressed = false;

	public interface PauseCallback {
		void onResume();
		void onReturnToTitle();
		void onRestart();
	}

	private PauseCallback callback;
	private KeyStateProvider keyStateProvider;

	public PauseMenu(int x, int y, int width, int height, PauseCallback callback) {
		super(x, y, width, height);
		setBackgroundColor(0.04f, 0.04f, 0.08f, 0.8f);
		this.callback = callback;
	}

	public void setKeyStateProvider(KeyStateProvider keyStateProvider) {
		this.keyStateProvider = keyStateProvider;
	}

	public void handleInput() {
		if (keyStateProvider == null) return;

		boolean currentUpPressed = keyStateProvider.isUpPressed();
		if (currentUpPressed && !upPressed) {
			selectedIndex = (selectedIndex - 1 + MENU_ITEMS.length) % MENU_ITEMS.length;
			upPressed = true;
		} else if (!currentUpPressed) {
			upPressed = false;
		}

		boolean currentDownPressed = keyStateProvider.isDownPressed();
		if (currentDownPressed && !downPressed) {
			selectedIndex = (selectedIndex + 1) % MENU_ITEMS.length;
			downPressed = true;
		} else if (!currentDownPressed) {
			downPressed = false;
		}

		boolean currentZPressed = keyStateProvider.isZPressed();
		if (currentZPressed && !zPressed) {
			handleSelection();
			zPressed = true;
		} else if (!currentZPressed) {
			zPressed = false;
		}

		boolean currentEscPressed = keyStateProvider.isEscPressed();
		if (currentEscPressed && !escPressed) {
			if (callback != null) {
				callback.onResume();
			}
			escPressed = true;
		} else if (!currentEscPressed) {
			escPressed = false;
		}
	}

	private void handleSelection() {
		switch (selectedIndex) {
		case 0:
			if (callback != null) {
				callback.onResume();
			}
			break;
		case 1:
			if (callback != null) {
				callback.onReturnToTitle();
			}
			break;
		case 2:
			if (callback != null) {
				callback.onRestart();
			}
			break;
		}
	}

	@Override
	public void render(IRenderer renderer) {
		drawBackground(renderer);
		drawMenu(renderer);
	}

	private void drawBackground(IRenderer renderer) {
		renderer.drawRect(0, 0, getWidth(), getHeight(), 
				backgroundColor[0], backgroundColor[1], backgroundColor[2], backgroundColor[3]);
	}

	private void drawMenu(IRenderer renderer) {
		FontManager fontManager = FontManager.getInstance();
		Font menuFont = fontManager.getFont(32f, Font.BOLD);

		for (int i = 0; i < MENU_ITEMS.length; i++) {
			String item = MENU_ITEMS[i];
			float menuX = getWidth() / 2 - 120;
			float menuY = getHeight() / 2 + 60 - i * 60;

			if (i == selectedIndex) {
				renderer.drawText(">", menuX - 50 - 1, menuY - 1, menuFont, Color.BLACK);
				renderer.drawText(">", menuX - 50 + 1, menuY - 1, menuFont, Color.BLACK);
				renderer.drawText(">", menuX - 50 - 1, menuY + 1, menuFont, Color.BLACK);
				renderer.drawText(">", menuX - 50 + 1, menuY + 1, menuFont, Color.BLACK);
				renderer.drawText(">", menuX - 50, menuY, menuFont, SELECTED_COLOR);

				renderer.drawText(item, menuX - 1, menuY - 1, menuFont, Color.BLACK);
				renderer.drawText(item, menuX + 1, menuY - 1, menuFont, Color.BLACK);
				renderer.drawText(item, menuX - 1, menuY + 1, menuFont, Color.BLACK);
				renderer.drawText(item, menuX + 1, menuY + 1, menuFont, Color.BLACK);
				renderer.drawText(item, menuX, menuY, menuFont, SELECTED_COLOR);
			} else {
				renderer.drawText(item, menuX, menuY, menuFont, UNSELECTED_COLOR);
			}
		}
	}

	public void update() {
		handleInput();
	}

	public void resetKeyStates() {
		upPressed = false;
		downPressed = false;
		zPressed = false;
		escPressed = false;
	}
}
