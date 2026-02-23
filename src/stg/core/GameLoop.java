package stg.core;

import stg.base.Window;
import user.player.DefaultPlayer;

/**
 * 游戏循环 - 控制游戏主循环
 * @since 2026-01-29
 * @author JavaSTG Team
 * @date 2026-02-23 更新为适配新的三面板窗口系统
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
	 * @param window 窗口引用
	 */
	public GameLoop(Window window) {
		this.window = window;
		this.running = false;
		this.lastFrameTime = System.nanoTime();
	}
	
	/**
	 * 启动游戏循环
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
				// 检查是否显示标题页面
				try {
					// 使用反射获取showTitleScreen字段
					java.lang.reflect.Field showTitleField = window.getClass().getDeclaredField("showTitleScreen");
					showTitleField.setAccessible(true);
					boolean showTitleScreen = showTitleField.getBoolean(window);
					
					if (showTitleScreen) {
						// 更新标题页面
						java.lang.reflect.Field titlePanelField = window.getClass().getDeclaredField("titlePanel");
						titlePanelField.setAccessible(true);
						Object titlePanel = titlePanelField.get(window);
						if (titlePanel != null) {
							java.lang.reflect.Method updateMethod = titlePanel.getClass().getMethod("update");
							updateMethod.invoke(titlePanel);
						}
					} else {
						// 更新游戏逻辑
						DefaultPlayer player = window.getPlayer();
						if (player != null && player.isActive()) {
							player.update();
						}
					}
				} catch (Exception e) {
					// 如果反射失败，默认更新玩家
					DefaultPlayer player = window.getPlayer();
					if (player != null && player.isActive()) {
						player.update();
					}
				}
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
	 */
	private void cleanup() {
		System.out.println("Game loop cleanup completed");
	}
	
	/**
	 * 停止游戏循环
	 */
	public void stop() {
		running = false;
		if (activeLoop == this) {
			activeLoop = null;
		}
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
	 * 获取当前FPS
	 * @return 当前FPS
	 */
	public int getCurrentFPS() {
		return currentFps;
	}
}
