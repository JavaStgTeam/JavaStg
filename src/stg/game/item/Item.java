package stg.game.item;

import java.awt.*;
import stg.game.obj.Obj;
import stg.game.player.Player;
import stg.game.ui.GameCanvas;

/**
 * 物品类- 所有物品的基类
 * 包括道具、掉落物、特殊物品等
 */
public abstract class Item extends Obj {
	// 道具吸引参数
	protected float attractionDistance = 150.0f;
	protected float attractionSpeed = 3.0f;

	/**
 * 构造函数
 * @param x 初始X坐标
 * @param y 初始Y坐标
 * @param size 物品大小
 * @param color 物品颜色
 */
	public Item(float x, float y, float size, Color color) {
		this(x, y, 0, 0, size, color, null);
	}

	/**
	 * 构造函数（带游戏画布）
	 * @param x 初始X坐标
	 * @param y 初始Y坐标
	 * @param size 物品大小
	 * @param color 物品颜色
	 * @param gameCanvas 游戏画布引用
	 */
	public Item(float x, float y, float size, Color color, GameCanvas gameCanvas) {
		this(x, y, 0, 0, size, color, gameCanvas);
	}

	/**
 * 构造函数
 * @param x 初始X坐标
 * @param y 初始Y坐标
 * @param vx X方向速度
 * @param vy Y方向速度
 * @param size 物品大小
 * @param color 物品颜色
 */
	public Item(float x, float y, float vx, float vy, float size, Color color) {
		this(x, y, vx, vy, size, color, null);
	}

	/**
 * 完整构造函数
 * @param x 初始X坐标
 * @param y 初始Y坐标
 * @param vx X方向速度
 * @param vy Y方向速度
 * @param size 物品大小
 * @param color 物品颜色
 * @param gameCanvas 游戏画布引用
 */
	public Item(float x, float y, float vx, float vy, float size, Color color, GameCanvas gameCanvas) {
		super(x, y, vx, vy, size, color, gameCanvas);
	}

	/**
 * 初始化行为参数
 * 在构造函数中调用，用于初始化行为参数
 */
	protected abstract void initBehavior();

	/**
	 * 实现每帧的自定义更新逻辑
	 */
	protected abstract void onUpdate();

	/**
	 * 实现自定义移动逻辑
	 */
	protected abstract void onMove();

	/**
 * 更新物品状态
 * 子类可重写此方法添加特定行为
 */
	@Override
	public void update() {
		super.update();

		// 检查是否超出边界
		if (getGameCanvas() != null && isOutOfBounds(getGameCanvas().getWidth(), getGameCanvas().getHeight())) {
			setActive(false);
		}
	}

	/**
	 * 渲染物品
	 * @param g 图形上下文
	 */
	@Override
	public void render(Graphics2D g) {
		if (!isActive()) return;

		float[] screenCoords = toScreenCoords(getX(), getY());
		float screenX = screenCoords[0];
		float screenY = screenCoords[1];

		stg.util.RenderUtils.enableAntiAliasing(g);
		g.setColor(getColor());
		g.fillOval((int)(screenX - getSize()), (int)(screenY - getSize()), (int)(getSize() * 2), (int)(getSize() * 2));

		// 绘制高光效果
		g.setColor(new Color(255, 255, 255, 150));
		g.fillOval((int)(screenX - getSize() * 0.4f), (int)(screenY - getSize() * 0.4f), (int)(getSize() * 0.8f), (int)(getSize() * 0.8f));
	}

	/**
	 * 物品被玩家拾取时的处�?	 * 子类可重写此方法实现特定效果
	 */
	public void onCollect() {
		setActive(false);
	}

	/**
	 * 任务开始时触发的方�?- 用于处理开局对话�?	 */
	protected void onTaskStart() {
		// 实现任务开始逻辑
	}

	/**
	 * 任务结束时触发的方法 - 用于处理boss击破对话和道具掉�?	 */
	protected void onTaskEnd() {
		// 实现任务结束逻辑
	}
	
	/**
	 * 应用道具吸引逻辑
	 */
	protected void applyAttraction() {
		if (getGameCanvas() != null) {
			Player player = getGameCanvas().getPlayer();
			if (player != null) {
				float dx = player.getX() - getX();
				float dy = player.getY() - getY();
				float distance = (float)Math.sqrt(dx * dx + dy * dy);
				
				if (distance < attractionDistance) {
					setVx((dx / distance) * attractionSpeed);
					setVy((dy / distance) * attractionSpeed);
				}
			}
		}
	}
	
	/**
	 * 设置吸引参数
	 * @param distance 吸引距离
	 * @param speed 吸引速度
	 */
	protected void setAttractionParams(float distance, float speed) {
		this.attractionDistance = distance;
		this.attractionSpeed = speed;
	}
}

