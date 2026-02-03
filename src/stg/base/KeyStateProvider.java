package stg.base;

/**
 * 按键状态提供者接口
 * 支持标题界面的虚拟键盘显示
 * 添加Shift和X键支持
 * @since 2026-01-20
 */
public interface KeyStateProvider {
	boolean isUpPressed();
	boolean isDownPressed();
	boolean isLeftPressed();
	boolean isRightPressed();
	boolean isZPressed();
	boolean isShiftPressed();
	boolean isXPressed();
}
