package stg.core;

import stg.base.Window;
import stg.entity.player.Player;

/**
 * 游戏循环 - 控制游戏主循环
 * <p>
 * 负责游戏的帧更新、渲染和事件处理，是游戏运行的核心组件。
 * 支持多面板状态管理，包括标题页、关卡组选择、玩家选择和游戏主界面。
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
			try {
				java.lang.reflect.Field panelStateField = window.getClass().getDeclaredField("currentPanelState");
				panelStateField.setAccessible(true);
				Object panelState = panelStateField.get(window);
				
				String panelStateName = panelState.toString();
				
				switch (panelStateName) {
				case "TITLE":
					java.lang.reflect.Field titlePanelField = window.getClass().getDeclaredField("titlePanel");
					titlePanelField.setAccessible(true);
					Object titlePanel = titlePanelField.get(window);
					if (titlePanel != null) {
						java.lang.reflect.Method updateMethod = titlePanel.getClass().getMethod("update");
						updateMethod.invoke(titlePanel);
					}
					break;
				case "STAGE_GROUP_SELECT":
					java.lang.reflect.Field stageGroupSelectPanelField = window.getClass().getDeclaredField("stageGroupSelectPanel");
					stageGroupSelectPanelField.setAccessible(true);
					Object stageGroupSelectPanel = stageGroupSelectPanelField.get(window);
					if (stageGroupSelectPanel != null) {
						java.lang.reflect.Method updateMethod = stageGroupSelectPanel.getClass().getMethod("update");
						updateMethod.invoke(stageGroupSelectPanel);
					}
					break;
				case "PLAYER_SELECT":
					java.lang.reflect.Field playerSelectPanelField = window.getClass().getDeclaredField("playerSelectPanel");
					playerSelectPanelField.setAccessible(true);
					Object playerSelectPanel = playerSelectPanelField.get(window);
					if (playerSelectPanel != null) {
						java.lang.reflect.Method updateMethod = playerSelectPanel.getClass().getMethod("update");
						updateMethod.invoke(playerSelectPanel);
					}
					break;
				case "GAME":
					try {
						java.lang.reflect.Field isPausedField = window.getClass().getDeclaredField("isPaused");
						isPausedField.setAccessible(true);
						boolean isPaused = isPausedField.getBoolean(window);
						
						if (isPaused) {
							java.lang.reflect.Field pauseMenuField = window.getClass().getDeclaredField("pauseMenu");
							pauseMenuField.setAccessible(true);
							Object pauseMenu = pauseMenuField.get(window);
							if (pauseMenu != null) {
								java.lang.reflect.Method updateMethod = pauseMenu.getClass().getMethod("update");
								updateMethod.invoke(pauseMenu);
							}
						} else {
							Player player = window.getPlayer();
							if (player != null && player.isActive()) {
								player.update();
							}
							
							try {
								java.lang.reflect.Field gameWorldField = window.getClass().getDeclaredField("gameWorld");
								gameWorldField.setAccessible(true);
								Object gameWorld = gameWorldField.get(window);
								
								java.lang.reflect.Field stageGroupField = window.getClass().getDeclaredField("selectedStageGroup");
								stageGroupField.setAccessible(true);
								Object stageGroup = stageGroupField.get(window);
								
								if (stageGroup != null) {
									java.lang.reflect.Method updateMethod = stageGroup.getClass().getMethod("update");
									updateMethod.invoke(stageGroup);
								}
								
								if (gameWorld != null) {
									java.lang.reflect.Method updateMethod = gameWorld.getClass().getMethod("update", int.class, int.class);
									updateMethod.invoke(gameWorld, 720, 960);
								}
							} catch (Exception ex) {
							}
						}
					} catch (Exception ex) {
						Player player = window.getPlayer();
						if (player != null && player.isActive()) {
							player.update();
						}
						
						try {
							java.lang.reflect.Field gameWorldField = window.getClass().getDeclaredField("gameWorld");
							gameWorldField.setAccessible(true);
							Object gameWorld = gameWorldField.get(window);
							
							java.lang.reflect.Field stageGroupField = window.getClass().getDeclaredField("selectedStageGroup");
							stageGroupField.setAccessible(true);
							Object stageGroup = stageGroupField.get(window);
							
							if (stageGroup != null) {
								java.lang.reflect.Method updateMethod = stageGroup.getClass().getMethod("update");
								updateMethod.invoke(stageGroup);
							}
							
							if (gameWorld != null) {
								java.lang.reflect.Method updateMethod = gameWorld.getClass().getMethod("update", int.class, int.class);
								updateMethod.invoke(gameWorld, 720, 960);
							}
						} catch (Exception ex2) {
						}
					}
					break;
				}
			} catch (Exception e) {
				Player player = window.getPlayer();
				if (player != null && player.isActive()) {
					player.update();
				}
				
				try {
					java.lang.reflect.Field gameWorldField = window.getClass().getDeclaredField("gameWorld");
					gameWorldField.setAccessible(true);
					Object gameWorld = gameWorldField.get(window);
					
					java.lang.reflect.Field stageGroupField = window.getClass().getDeclaredField("selectedStageGroup");
					stageGroupField.setAccessible(true);
					Object stageGroup = stageGroupField.get(window);
					
					if (stageGroup != null) {
						java.lang.reflect.Method updateMethod = stageGroup.getClass().getMethod("update");
						updateMethod.invoke(stageGroup);
					}
					
					if (gameWorld != null) {
						java.lang.reflect.Method updateMethod = gameWorld.getClass().getMethod("update", int.class, int.class);
						updateMethod.invoke(gameWorld, 720, 960);
					}
				} catch (Exception ex) {
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