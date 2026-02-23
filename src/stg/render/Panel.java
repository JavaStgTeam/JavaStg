package stg.render;

/**
 * 面板基类
 * 所有面板的抽象基类，定义面板的基本属性和方法
 * @since 2026-02-23
 * @author JavaSTG Team
 */
public abstract class Panel {
	/** 面板X坐标 */
	protected int x;
	/** 面板Y坐标 */
	protected int y;
	/** 面板宽度 */
	protected int width;
	/** 面板高度 */
	protected int height;
	/** 背景颜色 */
	protected float[] backgroundColor = {0.0f, 0.0f, 0.0f, 1.0f};
	
	/**
	 * 构造函数
	 * @param x 面板X坐标
	 * @param y 面板Y坐标
	 * @param width 面板宽度
	 * @param height 面板高度
	 */
	public Panel(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * 渲染面板
	 * @param renderer 渲染器
	 */
	public abstract void render(IRenderer renderer);
	
	/**
	 * 更新面板状态
	 * @param deltaTime 帧间隔时间（秒）
	 */
	public void update(float deltaTime) {
		// 默认空实现，子类可覆盖
	}
	
	/**
	 * 设置背景颜色
	 * @param r 红色分量 (0.0 - 1.0)
	 * @param g 绿色分量 (0.0 - 1.0)
	 * @param b 蓝色分量 (0.0 - 1.0)
	 * @param a 透明度 (0.0 - 1.0)
	 */
	public void setBackgroundColor(float r, float g, float b, float a) {
		this.backgroundColor[0] = r;
		this.backgroundColor[1] = g;
		this.backgroundColor[2] = b;
		this.backgroundColor[3] = a;
	}
	
	/**
	 * 渲染背景
	 * @param renderer 渲染器
	 */
	protected void renderBackground(IRenderer renderer) {
		renderer.drawRect(0, 0, width, height, 
				backgroundColor[0], backgroundColor[1], backgroundColor[2], backgroundColor[3]);
	}
	
	/**
	 * 获取面板X坐标
	 * @return X坐标
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * 获取面板Y坐标
	 * @return Y坐标
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * 获取面板宽度
	 * @return 宽度
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * 获取面板高度
	 * @return 高度
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * 设置面板位置和尺寸
	 * @param x X坐标
	 * @param y Y坐标
	 * @param width 宽度
	 * @param height 高度
	 */
	public void setBounds(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
}
