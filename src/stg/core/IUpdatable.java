package stg.core;

/**
 * 可更新接口 - 定义可以被游戏循环更新的对象
 * <p>
 * 用于替代反射机制，提供类型安全的更新调用方式。
 * @since 2026-04-10
 * @author JavaSTG Team
 */
public interface IUpdatable {
	/**
	 * 执行更新逻辑
	 * 每帧由游戏循环调用一次
	 */
	void update();
}
