package stg.render;

/**
 * 可渲染接口
 * 游戏中需要被渲染的实体应实现此接口
 * @since 2026-02-23
 * @author JavaSTG Team
 */
public interface IRenderable {
	/**
	 * 获取渲染层级
	 * @return 渲染层级
	 */
	int getRenderLayer();
	
	/**
	 * 检查是否激活
	 * @return 是否激活
	 */
	boolean isActive();
	
	/**
	 * 渲染实体
	 * @param renderer 渲染器
	 */
	void render(IRenderer renderer);
}
