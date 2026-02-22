package stg.core;

import stg.base.Window;
import stg.ui.GameCanvas;

/**
 * 游戏循环 - 控制游戏主循环
 * @since 2026-01-29
 */
public class GameLoop implements Runnable {
	private final GameCanvas canvas; // 游戏画布
	private Window window; // 窗口引用
	private boolean running; // 运行标志
	private int targetFPS = 60; // 目标帧率
	private boolean vsyncAware = true; // 是否感知垂直同步
	private static GameLoop activeLoop; // 当前活跃的游戏循环实例
	private Thread gameThread; // 游戏线程引用
	// FPS计算相关变量
	private int frameCount = 0;
	private long lastFpsUpdate = 0;
	private int currentFps = 0;
	// 帧率控制相关变量
	private long lastFrameTime = 0;
	private long accumulatedSleepTime = 0;
	private static final long NANO_PER_MILLI = 1000000L;
	private static final long NANO_PER_SECOND = 1000000000L;
	/**
	 * 构造函数 - 创建游戏循环实例
	 * @param canvas 游戏画布
	 */
	public GameLoop(GameCanvas canvas) {
		this.canvas = canvas;
		this.running = false;
		this.lastFrameTime = System.nanoTime();
	}

	/**
	 * 设置窗口引用
	 * @param window 窗口引用
	 */
	public void setWindow(Window window) {
		this.window = window;
		// 设置初始窗口标题
		if (window != null) {
			int objCount = canvas.getObjectCount();
			window.updateTitle(objCount, 0);
		}
	}

	/**
	 * 启动游戏循环
	 */
	public void start() {
		if (!running) {
			// 停止之前的循环（如果有）
			if (activeLoop != null) {
				activeLoop.stop();
			}
			activeLoop = this;
			running = true;
			// 直接在当前线程中运行游戏循环，确保OpenGL上下文可用
			run();
		}
	}

	/**
	 * 游戏主循环 - 持续更新游戏状态
	 */
	@Override
	public void run() {
		lastFpsUpdate = System.currentTimeMillis();
		lastFrameTime = System.nanoTime();
		
		while (running && !Thread.interrupted()) {
			// 检查窗口是否应该关闭
			if (window != null && window.shouldClose()) {
				running = false;
				break;
			}
			
			long currentTime = System.nanoTime();
			long elapsedTime = currentTime - lastFrameTime;
			
			// 处理渲染器
			if (canvas.getRenderer() != null) {
				// 检查是否需要显示标题页面或关卡组选择页面
				if (Main.Main.showTitleScreen) {
					// 渲染标题页面
					if (Main.Main.titleScreen != null) {
						Main.Main.titleScreen.update();
						Main.Main.titleScreen.render(canvas.getRenderer(), canvas.getWidth(), canvas.getHeight());
					}
				} else if (Main.Main.showStageGroupSelect) {
					// 渲染关卡组选择页面
					if (Main.Main.stageGroupSelectPanel != null) {
						Main.Main.stageGroupSelectPanel.update();
						Main.Main.stageGroupSelectPanel.render(canvas.getRenderer(), canvas.getWidth(), canvas.getHeight());
					}
				} else {
					// 更新游戏状态
					canvas.update();
					// 使用新的渲染系统渲染游戏世界
					canvas.render();
				}
			}
			
			// 处理窗口事件
			if (window != null) {
				window.pollEvents();
			}
			
			// 计算FPS
			frameCount++;
			long currentMsTime = System.currentTimeMillis();
			if (currentMsTime - lastFpsUpdate >= 1000) { // 每1秒更新一次FPS
				currentFps = frameCount;
				frameCount = 0;
				lastFpsUpdate = currentMsTime;
			}
			
			// 每次循环都更新窗口标题，确保任何时候都显示
			if (window != null) {
				int objCount = canvas.getObjectCount();
				window.updateTitle(objCount, currentFps);
			}
			
			// 计算休眠时间以维持目标帧率
			long targetFrameTime = NANO_PER_SECOND / targetFPS;
			long sleepTime = targetFrameTime - elapsedTime;
			
			// 帧率控制策略
			if (sleepTime > 0) {
				try {
					// 使用更精确的休眠
					if (sleepTime > NANO_PER_MILLI) {
						// 先休眠毫秒部分
						long msSleep = sleepTime / NANO_PER_MILLI;
						int nsSleep = (int)(sleepTime % NANO_PER_MILLI);
						Thread.sleep(msSleep, nsSleep);
					} else {
						// 直接使用Thread.sleep的纳秒精度
						Thread.sleep(0, (int)sleepTime);
					}
				} catch (InterruptedException e) {
					// 重新设置线程的中断状态
					Thread.currentThread().interrupt();
					// 记录中断信息
					System.out.println("Game loop interrupted during sleep");
				}
			} else if (sleepTime < -targetFrameTime * 0.5) {
				// 如果帧率过低，打印警告
				System.out.println("Warning: Low FPS detected - elapsed time: " + (elapsedTime / NANO_PER_MILLI) + "ms");
			}
			
			lastFrameTime = System.nanoTime();
		}
		// 循环结束后清理资源
		cleanup();
	}

	/**
	 * 清理资源
	 */
	private void cleanup() {
		// 清理资源的代码
		System.out.println("Game loop cleanup completed");
	}

	/**
	 * 停止游戏循环
	 */
	public void stop() {
		running = false;
		// 中断线程以确保它能及时退出
		if (gameThread != null && gameThread.isAlive()) {
			gameThread.interrupt();
			// 等待线程结束（可选，根据需要调整超时时间）
			try {
				// 使用较短的超时时间，避免阻塞主线程
				gameThread.join(500);
			} catch (InterruptedException e) {
				// 重新设置当前线程的中断状态
				Thread.currentThread().interrupt();
				System.out.println("Interrupted while waiting for game thread to stop");
			}
		}
		if (activeLoop == this) {
			activeLoop = null;
		}
		// 清空线程引用
		gameThread = null;
	}

	/**
	 * 停止所有游戏循环
	 */
	public static void stopAll() {
		if (activeLoop != null) {
			activeLoop.stop();
		}
	}
	
	/**
	 * 设置目标帧率
	 * @param targetFPS 目标帧率
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
	 * 设置是否感知垂直同步
	 * @param vsyncAware 是否感知垂直同步
	 */
	public void setVSyncAware(boolean vsyncAware) {
		this.vsyncAware = vsyncAware;
	}
	
	/**
	 * 获取当前FPS
	 * @return 当前FPS
	 */
	public int getCurrentFPS() {
		return currentFps;
	}
}