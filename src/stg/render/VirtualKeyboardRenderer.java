package stg.render;

import stg.base.KeyStateProvider;

/**
 * 虚拟键盘渲染器 - 在左侧面板显示按键状态
 * @since 2026-02-23
 * @author JavaSTG Team
 */
public class VirtualKeyboardRenderer {
	private KeyStateProvider keyStateProvider;
	private int keyWidth = 40;
	private int keyHeight = 40;
	private int keyGap = 4;
	
	public VirtualKeyboardRenderer() {
		this.keyStateProvider = null;
	}
	
	public VirtualKeyboardRenderer(KeyStateProvider keyStateProvider) {
		this.keyStateProvider = keyStateProvider;
	}
	
	public void setKeyStateProvider(KeyStateProvider keyStateProvider) {
		this.keyStateProvider = keyStateProvider;
	}
	
	public void render(IRenderer renderer, int panelWidth, int panelHeight) {
		if (keyStateProvider == null) {
			return;
		}
		
		// 不要随意调整
		int baseX = panelWidth / 2 - 100; 
		int baseY = panelHeight / 2 - 450; 
		
		// 方向键布局 - 标准键盘排列
		int upX = baseX + keyWidth;
		int upY = baseY + keyHeight + keyGap; // 向上键在上方
		
		int downX = baseX + keyWidth;
		int downY = baseY; // 向下键在下方
		
		int leftX = baseX - 2;
		int leftY = baseY;
		
		int rightX = baseX + keyWidth * 2 + keyGap -2; // 右键向右移动2像素
		int rightY = baseY;
		
		// 绘制方向键
		drawKey(renderer, upX, upY, keyWidth, keyHeight, "↑", keyStateProvider.isUpPressed());
		drawKey(renderer, downX, downY, keyWidth, keyHeight, "↓", keyStateProvider.isDownPressed());
		drawKey(renderer, leftX, leftY, keyWidth, keyHeight, "←", keyStateProvider.isLeftPressed());
		drawKey(renderer, rightX, rightY, keyWidth, keyHeight, "→", keyStateProvider.isRightPressed());
		
		// 功能键布局 - 右侧
		int funcX = baseX + keyWidth * 3 + keyGap * 2;
		int zY = baseY + keyHeight + keyGap; // Z与上键对齐
		int shiftY = baseY; // Shift与下键对齐
		
		// 绘制功能键
		drawKey(renderer, funcX, zY, keyWidth, keyHeight, "Z", keyStateProvider.isZPressed());
		drawKey(renderer, funcX + keyWidth + keyGap, zY, keyWidth, keyHeight, "X", keyStateProvider.isXPressed());
		drawKey(renderer, funcX, shiftY, keyWidth * 2 + keyGap, keyHeight, "Shift", keyStateProvider.isShiftPressed());
	}
	
	private void drawKey(IRenderer renderer, int x, int y, int width, int height, String text, boolean pressed) {
		if (pressed) {
			renderer.drawRect(x, y, width, height, 0.39f, 0.78f, 0.39f, 1.0f);
			renderer.drawRect(x + 2, y + 2, width - 4, height - 4, 0.31f, 0.71f, 0.31f, 1.0f);
			renderer.drawRect(x + 4, y + 4, width - 8, height - 8, 0.24f, 0.63f, 0.24f, 1.0f);
		} else {
			renderer.drawRect(x, y, width, height, 0.24f, 0.24f, 0.31f, 1.0f);
			renderer.drawRect(x, y, width, height, 0.39f, 0.39f, 0.47f, 1.0f);
		}
	}
}
