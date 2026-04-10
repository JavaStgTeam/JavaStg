package stg.core;

import stg.base.Window;
import stg.entity.player.Player;
import stg.util.GameConstants;

/**
 * 游戏循环 - 控制游戏主循环
 * <p>
 * 负责游戏的帧更新、渲染和事件处理，是游戏运行的核心组件。
 * 支持多面板状态管理，包括标题页、关卡组选择、玩家选择和游戏主界面。
 * @since 2026-01-29
 * @author JavaSTG Team
 * @date 2026-04-10 重构：消除反射，使用类型安全的接口调用
 */
public class GameLoop implements Runnable {
	/** 窗口引用 */
	private final Window window;
	/** 运行标志 */
	private boolean running;
	/** 目标帧率 */
	private int targetFPS = 60;
	/** 当前活跃的游戏循环实例 */
	private static GameLoop activeLoop;
	/** 帧计数 */
	private int frameCount = 0;
	/** 上次FPS更新时间 */
	private long lastFpsUpdate = 0;
	/** 当前FPS */
	private int currentFps = 0;
	/** 上一帧时间 */
	private long lastFrameTime = 0;
	/** 纳秒常量 */
	private static final long NANO_PER_MILLI = 1000000L;
	private static final long NANO_PER_SECOND = 1000000000L;
	
	/**
	 * 构造函数 - 创建游戏循环实例
	 * @param window 窗口引用，用于获取游戏状态和执行渲染操作
	 */
	public GameLoop(Window window) {
		this.window = window;
		this.running = false;
		this.lastFrameTime = System.nanoTime();
	}

	/**
	 * 启动游戏循环
	 * 如果已有活跃的游戏循环，会先停止它，然后启动当前实例
	 * 启动后会进入游戏主循环，持续更新游戏状态直到游戏结束
	 */
	public void start() {
		if (!running) {
			if (activeLoop != null) {
				activeLoop.stop();
			}
			activeLoop = this;
			running = true;
			run();
		}
	}
	
	/**
	 * 游戏主循环 - 持续更新游戏状态
	 * @see Runnable#run()
	 */
	@Override
	public void run() {
		lastFpsUpdate = System.currentTimeMillis();
		lastFrameTime = System.nanoTime();
		
		while (running && !Thread.interrupted()) {
			if (window != null && window.shouldClose()) {
				running = false;
				break;
			}
			
			long currentTime = System.nanoTime();
			long elapsedTime = currentTime - lastFrameTime;
			
			if (window != null) {
				// 使用类型安全的方式更新当前面板
				window.updateCurrentPanel();
			}
			
			window.render();
			window.swapBuffers();
			window.pollEvents();
			
			frameCount++;
			long currentMsTime = System.currentTimeMillis();
			if (currentMsTime - lastFpsUpdate >= 1000) {
				currentFps = frameCount;
				frameCount = 0;
				lastFpsUpdate = currentMsTime;
			}
			
			if (window != null) {
				window.updateTitle(window.getObjectCount(), currentFps);
			}
			
			long targetFrameTime = NANO_PER_SECOND / targetFPS;
			long sleepTime = targetFrameTime - elapsedTime;
			
			if (sleepTime > 0) {
				try {
					if (sleepTime > NANO_PER_MILLI) {
						long msSleep = sleepTime / NANO_PER_MILLI;
						int nsSleep = (int)(sleepTime % NANO_PER_MILLI);
						Thread.sleep(msSleep, nsSleep);
					} else {
						Thread.sleep(0, (int)sleepTime);
					}
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					System.out.println("Game loop interrupted during sleep");
				}
			}
			
			lastFrameTime = System.nanoTime();
		}
		cleanup();
	}

	/**
	 * 清理资源
	 * 在游戏循环结束时调用，用于释放相关资源
	 */
	private void cleanup() {
		System.out.println("Game loop cleanup completed");
	}

	/**
	 * 停止游戏循环
	 * 将运行标志设置为false，并在当前循环是活跃循环时将其置为null
	 */
	public void stop() {
		running = false;
		if (activeLoop == this) {
			activeLoop = null;
		}
	}

	/**
	 * 停止所有游戏循环
	 * 如果存在活跃的游戏循环，调用其stop方法停止它
	 */
	public static void stopAll() {
		if (activeLoop != null) {
			activeLoop.stop();
		}
	}

	/**
	 * 设置目标帧率
	 * @param targetFPS 目标帧率，必须大于0
	 * 用于控制游戏运行的速度，默认为60 FPS
	 */
	public void setTargetFPS(int targetFPS) {
		if (targetFPS > 0) {
			this.targetFPS = targetFPS;
		}
	}

	/**
	 * 获取当前目标帧率
	 * @return 目标帧率
	 */
	public int getTargetFPS() {
		return targetFPS;
	}

	/**
	 * 获取当前FPS
	 * @return 当前FPS
	 * 返回最近一秒内的平均帧率
	 */
	public int getCurrentFPS() {
		return currentFps;
	}
}
