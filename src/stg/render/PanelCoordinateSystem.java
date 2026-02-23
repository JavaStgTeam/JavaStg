package stg.render;

/**
 * 面板坐标系转换工具
 * 处理游戏逻辑坐标和面板屏幕坐标之间的转换
 * 游戏逻辑坐标系以中心为原点，Y轴向上为正
 * 面板屏幕坐标系以左下角为原点，Y轴向上为正
 * @since 2026-02-23
 * @author JavaSTG Team
 */
public class PanelCoordinateSystem {
	private int panelWidth;
	private int panelHeight;
	private int logicalWidth;
	private int logicalHeight;
	private float scaleX;
	private float scaleY;
	
	/**
	 * 构造函数
	 * @param panelWidth 面板宽度
	 * @param panelHeight 面板高度
	 * @param logicalWidth 游戏逻辑宽度
	 * @param logicalHeight 游戏逻辑高度
	 */
	public PanelCoordinateSystem(int panelWidth, int panelHeight, int logicalWidth, int logicalHeight) {
		this.panelWidth = panelWidth;
		this.panelHeight = panelHeight;
		this.logicalWidth = logicalWidth;
		this.logicalHeight = logicalHeight;
		updateScale();
	}
	
	/**
	 * 更新缩放因子
	 */
	private void updateScale() {
		this.scaleX = (float) panelWidth / logicalWidth;
		this.scaleY = (float) panelHeight / logicalHeight;
	}
	
	/**
	 * 将游戏逻辑坐标转换为面板屏幕坐标
	 * 游戏逻辑坐标系以中心为原点，Y轴向上为正
	 * 面板屏幕坐标系以左下角为原点，Y轴向上为正
	 * @param logicX 游戏逻辑X坐标
	 * @param logicY 游戏逻辑Y坐标
	 * @return 面板屏幕坐标 [screenX, screenY]
	 */
	public float[] logicToScreen(float logicX, float logicY) {
		float logicCenterX = logicalWidth / 2.0f;
		float logicCenterY = logicalHeight / 2.0f;
		
		float screenX = (logicX + logicCenterX) * scaleX;
		float screenY = (logicY + logicCenterY) * scaleY;
		
		return new float[]{screenX, screenY};
	}
	
	/**
	 * 将面板屏幕坐标转换为游戏逻辑坐标
	 * @param screenX 面板屏幕X坐标
	 * @param screenY 面板屏幕Y坐标
	 * @return 游戏逻辑坐标 [logicX, logicY]
	 */
	public float[] screenToLogic(float screenX, float screenY) {
		float logicCenterX = logicalWidth / 2.0f;
		float logicCenterY = logicalHeight / 2.0f;
		
		float logicX = screenX / scaleX - logicCenterX;
		float logicY = screenY / scaleY - logicCenterY;
		
		return new float[]{logicX, logicY};
	}
	
	/**
	 * 获取面板宽度
	 * @return 面板宽度
	 */
	public int getPanelWidth() {
		return panelWidth;
	}
	
	/**
	 * 获取面板高度
	 * @return 面板高度
	 */
	public int getPanelHeight() {
		return panelHeight;
	}
	
	/**
	 * 获取游戏逻辑宽度
	 * @return 游戏逻辑宽度
	 */
	public int getLogicalWidth() {
		return logicalWidth;
	}
	
	/**
	 * 获取游戏逻辑高度
	 * @return 游戏逻辑高度
	 */
	public int getLogicalHeight() {
		return logicalHeight;
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
