package stg.entity.player;

import java.awt.Color;
import java.awt.Graphics2D;

import stg.core.GameWorld;
import stg.entity.base.Obj;
import stg.render.RenderLayer;
import stg.service.render.IRenderer;

/**
 * 玩家类- 自机角色
 * @since 2026-01-19
 */
public class Player extends Obj {
	private float speed; // 普通移动速度
	private float speedSlow; // 低速移动速度

	private boolean slowMode; // 低速模式标志
	private boolean shooting; // 射击标志
	private int shootCooldown; // 射击冷却时间
	private static final int SHOOT_INTERVAL = 1;
	private int respawnTimer; // @since 2026-01-19 重生计时(帧数)
	private static final int RESPAWN_TIME = 60; // @since 2026-01-19 重生等待时间(帧数)
	private float spawnX; // @since 2026-01-19 重生X坐标
	private float spawnY; // @since 2026-01-19 重生Y坐标
	private boolean respawning; // @since 2026-01-19 重生动画标志
	private static final float RESPAWN_SPEED = 8.0f; // @since 2026-01-19 重生移动速度
	private int invincibleTimer; // 无敌时间计时(帧数)
	private static final int INVINCIBLE_TIME = 120; // 无敌时间(120f)
	protected static final int BULLET_DAMAGE = 2; // @since 2026-01-23 子弹伤害，DPS = (2 × 2 × 60) / 2 = 120
	private GameWorld gameWorld; // 游戏世界引用，用于发射子弹

	public Player() {
		this(0, 0, 5.0f, 2.0f, 20);
	}

	public Player(float spawnX, float spawnY) {
		this(spawnX, spawnY, 5.0f, 2.0f, 20);
	}

	public Player(float x, float y, float speed, float speedSlow, float size) {
		super(x, y, 0, 0, size, new Color(255, 100, 100));
		this.speed = speed;
		this.speedSlow = speedSlow;
		this.slowMode = false;
		this.shooting = false;
		this.shootCooldown = 0;
		this.respawnTimer = 0;
		this.spawnX = x;
		this.spawnY = y;
		this.respawning = false;
		this.invincibleTimer = INVINCIBLE_TIME; // @since 2026-01-23 初始无敌时间
	}
	
	/**
	 * 设置碰撞判定半径
	 * @param radius 碰撞判定半径
	 */
	@Override
	public void setHitboxRadius(float radius) {
		// 碰撞判定半径在父类 Obj 中定义
	}
	
	/**
	 * 获取碰撞判定半径
	 * @return 碰撞判定半径
	 */
	@Override
	public float getHitboxRadius() {
		return 2.0f; // 默认碰撞判定半径
	}
	
	/**
	 * 获取X方向速度
	 * @return X方向速度
	 */
	public float getVelocityX() {
		return getVx();
	}
	
	/**
	 * 获取Y方向速度
	 * @return Y方向速度
	 */
	public float getVelocityY() {
		return getVy();
	}
	
	/**
	 * 设置速度分量
	 * @param component 分量索引 (0: X, 1: Y)
	 * @param value 速度值
	 */
	public void setVelocityByComponent(int component, float value) {
		if (component == 0) {
			setVx(value);
		} else if (component == 1) {
			setVy(value);
		}
	}
	
	/**
	 * 设置速度分量
	 * @param component 分量索引 (0: X, 1: Y)
	 * @param value 速度值
	 */
	public void setVelocityByComponent(int component, int value) {
		if (component == 0) {
			setVx(value);
		} else if (component == 1) {
			setVy(value);
		}
	}
	
	/**
	 * 移动指定距离
	 * @param dx X方向移动距离
	 * @param dy Y方向移动距离
	 */
	public void moveOn(float dx, float dy) {
		setX(getX() + dx);
		setY(getY() + dy);
	}

	/**
	 * 实现每帧的自定义更新逻辑
	 */
	@Override
	protected void onUpdate() {
		// 子类可以重写此方法实现每帧的自定义更新逻辑
	}

	/**
	 * 实现自定义移动逻辑
	 */
	@Override
	protected void onMove() {
		// 子类可以重写此方法实现自定义移动逻辑
	}

	/**
	 * 更新玩家状态- @since 2026-01-19 使用中心原点坐标系 * 坐标系 右上为+,+),左下为-,-)
	*/
	@Override
	public void update() {
		// 调用自定义更新逻辑
		onUpdate();

		// @since 2026-01-19 处理重生等待计时
		if (respawnTimer > 0) {
			respawnTimer--;
			if (respawnTimer == 0) {
				respawning = true;
				// 使用游戏逻辑坐标系的底部边界作为重生起点
				stg.entity.base.Obj.requireCoordinateSystem();
				stg.util.CoordinateSystem cs = stg.entity.base.Obj.getSharedCoordinateSystem();
				float bottomBound = cs.getBottomBound();
				setPosition(spawnX, bottomBound - getSize());
				setVelocityByComponent(1, RESPAWN_SPEED);
			}
			return;
		}

		// @since 2026-01-19 处理重生动画
		if (respawning) {
			// 调用自定义移动逻辑
			onMove();

			// 更新位置
			moveOn(getVelocityX(), getVelocityY());

			// 检查是否到达重生位置
			if (getY() >= spawnY) {
				setPosition(spawnX, spawnY);
				setVelocityByComponent(0, 0);
				setVelocityByComponent(1, 0);
				respawning = false;
				invincibleTimer = INVINCIBLE_TIME; // @since 2026-01-23 重生后获得无敌时间
				System.out.println("Player respawned at: (" + getX() + ", " + getY() + ") with " + INVINCIBLE_TIME + " frames invincible");
			}
			return; // 重生动画期间不接受玩家输入
		}

		// 调用自定义移动逻辑
		onMove();

		// 更新位置
		moveOn(getVelocityX(), getVelocityY());

		// 使用游戏逻辑坐标系的固定边界进行边界检测
		stg.entity.base.Obj.requireCoordinateSystem();
		stg.util.CoordinateSystem cs = stg.entity.base.Obj.getSharedCoordinateSystem();
		float leftBound = cs.getLeftBound();
		float rightBound = cs.getRightBound();
		float bottomBound = cs.getBottomBound();
		float topBound = cs.getTopBound();

		// @since 2026-01-19 边界限制(中心原点坐标系 * 坐标系 右上为+,+),左下为-,-))
		if (getX() < leftBound + getSize()) setPosition(leftBound + getSize(), getY());
		if (getX() > rightBound - getSize()) setPosition(rightBound - getSize(), getY());
		if (getY() < bottomBound + getSize()) setPosition(getX(), bottomBound + getSize());
		if (getY() > topBound - getSize()) setPosition(getX(), topBound - getSize());

		// 更新射击冷却
		if (shootCooldown > 0) {
			shootCooldown--;
		}

		// @since 2026-01-23 更新无敌时间计时
		if (invincibleTimer > 0) {
			invincibleTimer--;
		}

		// 射击逻辑
		if (shooting && shootCooldown == 0) {
			shoot();
			shootCooldown = SHOOT_INTERVAL;
		}
	}

	/**
	 * 发射子弹 - @since 2026-01-19 向上发射(Y轴正方向)
	 * 子类可重写此方法实现不同的射击模式 */
	protected void shoot() {
		// 简化射击逻辑，不再使用SimpleBullet
		System.out.println("Player shot");
	}

	/**
	 * 渲染玩家 - 简化版本：仅渲染为一个球体
	 * @param g 图形上下文
	 */
	@Override
	public void render(Graphics2D g) {
		// 将中心原点坐标转换为屏幕坐标
		float[] screenCoords = toScreenCoords(getX(), getY());
		float screenX = screenCoords[0];
		float screenY = screenCoords[1];

		// 开启抗锯齿
		stg.util.RenderUtils.enableAntiAliasing(g);

		// @since 2026-01-23 无敌闪烁效果：每5帧闪烁一次
		boolean shouldRender = true;
		if (invincibleTimer > 0) {
			int flashPhase = invincibleTimer % 10; // 10帧为一个闪烁周期
			if (flashPhase < 5) {
				shouldRender = false;
			}
		}

		// 绘制角色主体（仅为一个简单的红色球体）
		if (shouldRender) {
			g.setColor(getColor());
			g.fillOval((int)(screenX - getSize()), (int)(screenY - getSize()),
					(int)(getSize() * 2), (int)(getSize() * 2));
		}

		// 低速模式时显示受击判定点（在球体上方）
		if (slowMode && shouldRender) {
			g.setColor(Color.WHITE);
			g.fillOval((int)(screenX - getHitboxRadius()), (int)(screenY - getHitboxRadius()),
					(int)(getHitboxRadius() * 2), (int)(getHitboxRadius() * 2));
		}
	}

	/**
	 * 渲染玩家（IRenderer版本，支持OpenGL）
	 * @param renderer 渲染器
	 */
	@Override
	public void render(IRenderer renderer) {
		// 开启抗锯齿
		renderer.enableAntiAliasing();

		// @since 2026-01-23 无敌闪烁效果：每5帧闪烁一次
		boolean shouldRender = true;
		if (invincibleTimer > 0) {
			int flashPhase = invincibleTimer % 10; // 10帧为一个闪烁周期
			if (flashPhase < 5) {
				shouldRender = false;
			}
		}

		// 绘制角色主体（仅为一个简单的红色球体）
		if (shouldRender) {
			renderer.drawCircle(getX(), getY(), getSize(), getColor());
		}

		// 低速模式时显示受击判定点（在球体上方）
		if (slowMode && shouldRender) {
			renderer.drawCircle(getX(), getY(), getHitboxRadius(), Color.WHITE);
		}

		// 禁用抗锯齿
		renderer.disableAntiAliasing();
	}
	/**
	 * 向上移动 - @since 2026-01-19 Y轴正方向 */
	public void moveUp() {
		setVelocityByComponent(1, slowMode ? speedSlow : speed);
	}

	/**
	 * 向下移动 - @since 2026-01-19 Y轴负方向 */
	public void moveDown() {
		setVelocityByComponent(1, slowMode ? -speedSlow : -speed);
	}

	/**
	 * 向左移动
	 */
	public void moveLeft() {
		setVelocityByComponent(0, slowMode ? -speedSlow : -speed);
	}

	/**
	 * 向右移动
	 */
	public void moveRight() {
		setVelocityByComponent(0, slowMode ? speedSlow : speed);
	}

	/**
	 * 停止垂直移动
	 */
	public void stopVertical() {
		setVelocityByComponent(1, 0);
	}

	/**
	 * 停止水平移动
	 */
	public void stopHorizontal() {
		setVelocityByComponent(0, 0);
	}

	/**
	 * 设置射击状态
	 * @param shooting 是否射击
	 */
	public void setShooting(boolean shooting) {
		this.shooting = shooting;
	}

	/**
	 * 设置低速模式
	 * @param slow 是否低速模式
	 */
	public void setSlowMode(boolean slow) {
		this.slowMode = slow;
	}

	/**
	 * 设置位置
	 * @param x X坐标
	 * @param y Y坐标
	 */
	public void setPosition(float x, float y) {
		setX(x);
		setY(y);
		// @since 2026-01-19 保存初始位置用于重生
		this.spawnX = x;
		this.spawnY = y;
	}

	/**
	 * 获取普通移动速度
	 * @return 移动速度
	 */
	public float getSpeed() {
		return speed;
	}

	/**
	 * 获取低速移动速度
	 * @return 低速移动速度
	 */
	public float getSpeedSlow() {
		return speedSlow;
	}

	/**
	 * 设置普通移动速度
	 * @param speed 移动速度
	 */
	public void setSpeed(float speed) {
		this.speed = speed;
	}

	/**
	 * 设置低速移动速度
	 * @param speedSlow 低速移动速度
	 */
	public void setSpeedSlow(float speedSlow) {
		this.speedSlow = speedSlow;
	}



	/**
	 * 是否低速模式
	 * @return 是否低速模式
	 */
	public boolean isSlowMode() {
		return slowMode;
	}



	/**
	 * @since 2026-01-19 受击处理
	 * 玩家中弹后立即移到屏幕下方，然后等待重生
	 */
	public void onHit() {
		// 立即移动到屏幕下方
		int canvasHeight = 921;
		setPosition(getX(), -canvasHeight / 2.0f - getSize());
		setVelocityByComponent(0, 0);
		setVelocityByComponent(1, 0);
		respawning = false;

		// 启动重生等待计时器
		respawnTimer = RESPAWN_TIME;

		System.out.println("Player hit! Moved off-screen. Respawn animation in " + RESPAWN_TIME + " frames");
	}

	/**
	 * 重置玩家状态（用于重新开始游戏）
	 */
	@Override
	public void reset() {
		super.reset();
		setVelocityByComponent(0, 0);
		setVelocityByComponent(1, 0);
		slowMode = false;
		shooting = false;
		shootCooldown = 0;
		respawnTimer = 0;
		respawning = false;
		invincibleTimer = INVINCIBLE_TIME; // @since 2026-01-23 重置时获得无敌时间		// x和y由GameCanvas.resetGame() 设置
	}

	/**
	 * @since 2026-01-23 检查玩家是否处于无敌状态
	 * @return 是否无敌
	 */
	public boolean isInvincible() {
		return invincibleTimer > 0;
	}

	/**
	 * @since 2026-01-23 获取无敌计时器剩余帧数
	 * @return 无敌剩余帧数
	 */
	protected int getInvincibleTimer() {
		return invincibleTimer;
	}

	/**
	 * 获取子弹伤害
	 * @return 子弹伤害
	 */
	public int getBulletDamage() {
		return BULLET_DAMAGE;
	}
	
	/**
	 * 获取渲染层级
	 * @return 渲染层级
	 */
	@Override
	public RenderLayer getRenderLayer() {
		// 玩家角色使用PLAYER层级
		return RenderLayer.PLAYER;
	}

	/**
	 * 设置游戏世界引用
	 * @param gameWorld 游戏世界实例
	 */
	public void setGameWorld(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
	}

	/**
	 * 获取游戏世界引用
	 * @return 游戏世界实例
	 */
	protected GameWorld getGameWorld() {
		return gameWorld;
	}

	/**
	 * 任务开始时触发的方法 - 用于处理开局对话
	 */
	protected void onTaskStart() {
		// 实现任务开始逻辑
	}

	/**
	 * 任务结束时触发的方法 - 用于处理boss击破对话和道具掉落
	 */
	protected void onTaskEnd() {
		// 实现任务结束逻辑
	}
}