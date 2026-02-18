package stg.core;

import stg.ui.GameCanvas;

/**
 * 游戏循环 - 控制游戏主循环
 * @since 2026-01-29
 */
public class GameLoop implements Runnable {
	private final GameCanvas canvas; // 游戏画布
	private boolean running; // 运行标志
	private final int targetFPS = 60; // 目标帧率
	private static GameLoop activeLoop; // 当前活跃的游戏循环实例
	private Thread gameThread; // 游戏线程引用
	/**
	 * 构造函数 - 创建游戏循环实例
	 * @param canvas 游戏画布（需为 public 或位于 stg.ui 包内）
	 */
	public GameLoop(GameCanvas canvas) {
		this.canvas = canvas;
		this.running = false;
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
			gameThread = new Thread(this);
			gameThread.start();
		}
	}

	/**
	 * 游戏主循环 - 持续更新游戏状态
	 */
	@Override
	public void run() {
		while (running && !Thread.interrupted()) {
			long startTime = System.nanoTime();

			canvas.update(); // 更新游戏状态
			canvas.repaint(); // 触发重绘
			// 计算休眠时间以维持目标帧率（使用纳秒精度）
			long elapsedTime = System.nanoTime() - startTime;
			long targetFrameTime = 1000000000L / targetFPS; // 纳秒
			long sleepTime = targetFrameTime - elapsedTime;

			if (sleepTime > 0) {
				try {
					Thread.sleep(sleepTime / 1000000L, (int)(sleepTime % 1000000L));
				} catch (InterruptedException e) {
					// 重新设置线程的中断状态
					Thread.currentThread().interrupt();
					// 记录中断信息
					System.out.println("Game loop interrupted during sleep");
				}
			}
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
}