package user.bullet;

import java.awt.Color;
import stg.entity.bullet.Bullet;
import stg.util.objectpool.Pooled;

/**
 * 简单子弹类 - 提供空的task实现
 * 用于不需要特殊task行为的子弹
 * @since 2026-02-10 创建默认实现
 * @date 2026-02-22 添加@Pooled注解支持对象池
 */
@Pooled(initialCapacity = 100, maxCapacity = 500, name = "SimpleBulletPool")
public class SimpleBullet extends Bullet {

	/**
	 * 构造函数
	 * @param x 初始X坐标
	 * @param y 初始Y坐标
	 * @param vx X方向速度
	 * @param vy Y方向速度
	 * @param size 子弹大小
	 * @param color 子弹颜色
	 */
	public SimpleBullet(float x, float y, float vx, float vy, float size, Color color) {
		super(x, y, vx, vy, size, color);
	}

	/**
	 * 任务开始时触发的方法- 空实现
	 */
	@Override
	protected void onTaskStart() {
		// 空实现，不需要特殊行为
	}

	/**
	 * 任务结束时触发的方法 - 空实现
	 */
	@Override
	protected void onTaskEnd() {
		// 空实现，不需要特殊行为
	}

}