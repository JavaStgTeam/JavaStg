package stg.util;

/**
 * 坐标系统工具类
 * 提供坐标转换功能,将屏幕坐标系转换为画布中心原点坐标系
 * 游戏逻辑尺寸固定为360*480，支持屏幕尺寸变化的拉伸适配
 * @since 2026-01-19
 */
public class CoordinateSystem {
	private int screenWidth;
	private int screenHeight;
	private float scaleX;
	private float scaleY;

	/**
	 * 构造函数
	 * @param screenWidth 屏幕宽度
	 * @param screenHeight 屏幕高度
	 * @throws IllegalArgumentException 如果屏幕尺寸小于等于0
	 */
	public CoordinateSystem(int screenWidth, int screenHeight) {
		updateScreenSize(screenWidth, screenHeight);
	}

	/**
	 * 更新屏幕尺寸
	 * @param width 新屏幕宽度
	 * @param height 新屏幕高度
	 * @throws IllegalArgumentException 如果屏幕尺寸小于等于0
	 * @since 2026-01-19
	 */
	public void updateScreenSize(int width, int height) {
		if (width <= 0 || height <= 0) {
			throw new IllegalArgumentException("Screen dimensions must be positive: width=" + width + ", height=" + height);
		}
		this.screenWidth = width;
		this.screenHeight = height;
		// 使用统一的缩放因子，保持游戏内容的正确比例
		// 计算基于宽度和高度的缩放因子，取较小值以确保内容完全显示
		float scaleByWidth = (float) screenWidth / GameConstants.GAME_WIDTH;
		float scaleByHeight = (float) screenHeight / GameConstants.GAME_HEIGHT;
		this.scaleX = Math.min(scaleByWidth, scaleByHeight);
		this.scaleY = this.scaleX;
	}

	/**
	 * 将游戏逻辑坐标转换为屏幕坐标
	 * 坐标系说明: 游戏逻辑坐标右上角为(+,+),左下角为(-,-)
	 * @param x 游戏逻辑X坐标(向右为正)
	 * @param y 游戏逻辑Y坐标(向上为正)
	 * @return 屏幕左上角原点坐标[screenX, screenY]
	 * @since 2026-01-19
	 */
	public float[] toScreenCoords(float x, float y) {
		// 计算游戏内容在屏幕中的实际尺寸
		float gameContentWidth = GameConstants.GAME_WIDTH * scaleX;
		float gameContentHeight = GameConstants.GAME_HEIGHT * scaleY;
		
		// 计算游戏内容在屏幕中的偏移量，确保居中显示
		float offsetX = (screenWidth - gameContentWidth) / 2.0f;
		float offsetY = (screenHeight - gameContentHeight) / 2.0f;
		
		// 先转换到游戏逻辑中心原点，再应用缩放，最后添加偏移量
		float logicCenterX = GameConstants.GAME_WIDTH / 2.0f;
		float logicCenterY = GameConstants.GAME_HEIGHT / 2.0f;
		float screenX = (x + logicCenterX) * scaleX + offsetX;
		float screenY = offsetY + gameContentHeight - (y + logicCenterY) * scaleY;
		return new float[]{screenX, screenY};
	}

	/**
	 * 将屏幕坐标转换为游戏逻辑坐标
	 * @param screenX 屏幕X坐标
	 * @param screenY 屏幕Y坐标
	 * @return 游戏逻辑坐标 [x, y]
	 * @since 2026-01-19
	 */
	public float[] toGameCoords(float screenX, float screenY) {
		// 计算游戏内容在屏幕中的实际尺寸
		float gameContentWidth = GameConstants.GAME_WIDTH * scaleX;
		float gameContentHeight = GameConstants.GAME_HEIGHT * scaleY;
		
		// 计算游戏内容在屏幕中的偏移量
		float offsetX = (screenWidth - gameContentWidth) / 2.0f;
		float offsetY = (screenHeight - gameContentHeight) / 2.0f;
		
		// 先减去偏移量，再应用缩放，最后转换到游戏逻辑中心原点
		float logicCenterX = GameConstants.GAME_WIDTH / 2.0f;
		float logicCenterY = GameConstants.GAME_HEIGHT / 2.0f;
		float x = ((screenX - offsetX) / scaleX) - logicCenterX;
		float y = logicCenterY - ((screenY - offsetY) / scaleY);
		return new float[]{x, y};
	}

	/**
	 * 获取屏幕中心X坐标
	 * @return 屏幕中心X
	 * @since 2026-01-19
	 */
	public float getScreenCenterX() {
		return screenWidth / 2.0f;
	}

	/**
	 * 获取屏幕中心Y坐标
	 * @return 屏幕中心Y
	 * @since 2026-01-19
	 */
	public float getScreenCenterY() {
		return screenHeight / 2.0f;
	}

	/**
	 * 获取屏幕宽度
	 * @return 屏幕宽度
	 * @since 2026-01-19
	 */
	public int getScreenWidth() {
		return screenWidth;
	}

	/**
	 * 获取屏幕高度
	 * @return 屏幕高度
	 * @since 2026-01-19
	 */
	public int getScreenHeight() {
		return screenHeight;
	}

	/**
	 * 获取游戏逻辑左边界
	 * @return 左边界X坐标
	 * @since 2026-01-19
	 */
	public float getLeftBound() {
		return -GameConstants.GAME_WIDTH / 2.0f;
	}

	/**
	 * 获取游戏逻辑右边界
	 * @return 右边界X坐标
	 * @since 2026-01-19
	 */
	public float getRightBound() {
		return GameConstants.GAME_WIDTH / 2.0f;
	}

	/**
	 * 获取游戏逻辑上边界
	 * @return 上边界Y坐标
	 * @since 2026-01-19
	 */
	public float getTopBound() {
		return GameConstants.GAME_HEIGHT / 2.0f;
	}

	/**
	 * 获取游戏逻辑下边界
	 * @return 下边界Y坐标
	 * @since 2026-01-19
	 */
	public float getBottomBound() {
		return -GameConstants.GAME_HEIGHT / 2.0f;
	}

	/**
	 * 获取X轴缩放因子
	 * @return X轴缩放因子
	 */
	public float getScaleX() {
		return scaleX;
	}

	/**
	 * 获取Y轴缩放因子
	 * @return Y轴缩放因子
	 */
	public float getScaleY() {
		return scaleY;
	}
}

