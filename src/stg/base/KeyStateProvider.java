package stg.base;

public interface KeyStateProvider {
	boolean isUpPressed();
	boolean isDownPressed();
	boolean isLeftPressed();
	boolean isRightPressed();
	boolean isZPressed();
	boolean isShiftPressed();
	boolean isXPressed();
	boolean isCPressed();
	boolean isEscPressed();
}
