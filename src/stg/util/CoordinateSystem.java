package stg.util;

/**
 * 坐标系统工具类
 * 提供坐标转换功能,将屏幕坐标系转换为画布中心原点坐标系
 * @since 2026-01-19
 */
public class CoordinateSystem {
	private int canvasWidth;
	private int canvasHeight;

	/**
	 * 构造函�?	 * @param canvasWidth 画布宽度
	 * @param canvasHeight 画布高度
	 */
	public CoordinateSystem(int canvasWidth, int canvasHeight) {
		this.canvasWidth = canvasWidth;
		this.canvasHeight = canvasHeight;
	}

	/**
	 * 更新画布尺寸
	 * @param width 新宽度
	 * @param height 新高度
	 * @since 2026-01-19
	 */
	public void updateCanvasSize(int width, int height) {
		this.canvasWidth = width;
		this.canvasHeight = height;
	}

	/**
	 * 将画布中心原点坐标转换为屏幕左上角原点坐标
	 * 坐标系说明: 右上角为(+,+),左下角为(-,-)
	 * @param x 中心原点X坐标(向右为正)
	 * @param y 中心原点Y坐标(向上为正)
	 * @return 屏幕左上角原点坐标[screenX, screenY]
	 * @since 2026-01-19
	 */
	public float[] toScreenCoords(float x, float y) {
		// X: 中心向右为正,所�?+ width/2
		// Y: 中心向上为正,但屏幕Y轴向下为�?所�?- height/2
		float screenX = x + canvasWidth / 2.0f;
		float screenY = canvasHeight / 2.0f - y;
		return new float[]{screenX, screenY};
	}

	/**
	 * 将屏幕左上角原点坐标转换为画布中心原点坐标
	 * @param screenX 屏幕X坐标
	 * @param screenY 屏幕Y坐标
	 * @return 中心原点坐标 [x, y]
	 * @since 2026-01-19
	 */
	public float[] toCenterCoords(float screenX, float screenY) {
		// X: 屏幕坐标 - 中心坐标
		// Y: 中心坐标 - 屏幕坐标(因为Y轴翻�?
		float x = screenX - canvasWidth / 2.0f;
		float y = canvasHeight / 2.0f - screenY;
		return new float[]{x, y};
	}

	/**
	 * 获取画布中心X坐标
	 * @return 中心X
	 * @since 2026-01-19
	 */
	public float getCenterX() {
		return canvasWidth / 2.0f;
	}

	/**
	 * 获取画布中心Y坐标
	 * @return 中心Y
	 * @since 2026-01-19
	 */
	public float getCenterY() {
		return canvasHeight / 2.0f;
	}

	/**
	 * 获取画布宽度
	 * @return 画布宽度
	 * @since 2026-01-19
	 */
	public int getCanvasWidth() {
		return canvasWidth;
	}

	/**
	 * 获取画布高度
	 * @return 画布高度
	 * @since 2026-01-19
	 */
	public int getCanvasHeight() {
		return canvasHeight;
	}

	/**
	 * 获取画布左边界(中心原点坐标系)
	 * @return 左边界X坐标
	 * @since 2026-01-19
	 */
	public float getLeftBound() {
		return -canvasWidth / 2.0f;
	}

	/**
	 * 获取画布右边界(中心原点坐标系)
	 * @return 右边界X坐标
	 * @since 2026-01-19
	 */
	public float getRightBound() {
		return canvasWidth / 2.0f;
	}

	/**
	 * 获取画布上边界(中心原点坐标系)
	 * @return 上边界Y坐标
	 * @since 2026-01-19
	 */
	public float getTopBound() {
		return -canvasHeight / 2.0f;
	}

	/**
	 * 获取画布下边界(中心原点坐标系)
	 * @return 下边界Y坐标
	 * @since 2026-01-19
	 */
	public float getBottomBound() {
		return canvasHeight / 2.0f;
	}
}

