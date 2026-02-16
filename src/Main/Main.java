package Main;

import java.awt.EventQueue;
import stg.base.Window;
import stg.game.player.Player;
import stg.game.stage.StageGroup;
import stg.game.stage.StageGroupManager;
import stg.game.ui.GameCanvas;
import stg.game.ui.TitleScreen;
import stg.util.LogUtil;

public class Main {
	private static Window window;
	private static TitleScreen titleScreen;

	public static void main(String[] args) {
		System.out.println("启动 STG 游戏引擎");

		EventQueue.invokeLater(() -> {
			window = new Window(false);
			showTitleScreen();
		});
	}

	private static void showTitleScreen() {
		titleScreen = new TitleScreen(new TitleScreen.TitleCallback() {
			@Override
			public void onStageGroupSelect() {
				System.out.println("选择关卡组");
				showStageGroupSelect();
			}

			@Override
			public void onGameStart(StageGroup stageGroup) {
				System.out.println("开始游戏, 关卡组 " + stageGroup.getGroupName());
				startGame(stageGroup);
			}

			@Override
			public void onExit() {
				System.out.println("退出游戏");
				System.exit(0);
			}
		});

		window.getCenterPanel().removeAll();
		window.getCenterPanel().add(titleScreen);

		// 更新虚拟键盘以显示标题界面的按键状态
		window.getVirtualKeyboardPanel().setKeyStateProvider(titleScreen);

		titleScreen.requestFocusInWindow();
		window.revalidate();
		window.repaint();
	}

	private static void showStageGroupSelect() {
		stg.game.ui.StageGroupSelectPanel selectPanel = new stg.game.ui.StageGroupSelectPanel(


			new stg.game.ui.StageGroupSelectPanel.StageGroupSelectCallback() {
				@Override
				public void onStageGroupSelected(StageGroup stageGroup) {
					System.out.println("选择关卡组 " + stageGroup.getGroupName());
					startGame(stageGroup);
				}

				@Override
				public void onBack() {
					System.out.println("返回主菜单");
					showTitleScreen();
				}
			}
		);

		window.getCenterPanel().removeAll();
		window.getCenterPanel().add(selectPanel);
		window.getVirtualKeyboardPanel().setKeyStateProvider(selectPanel);
		selectPanel.requestFocusInWindow();
		window.revalidate();
		window.repaint();

		// 在单独的线程中获取关卡组列表，以避免阻塞UI线程
		new Thread(() -> {
			try {
				// 使用StageGroupManager获取关卡组列表
				stg.game.ui.GameCanvas gameCanvas = window.getGameCanvas();
				StageGroupManager stageGroupManager = StageGroupManager.getInstance();
				stageGroupManager.init(gameCanvas);

				// 获取关卡组列表
				final java.util.List<StageGroup> stageGroups = stageGroupManager.getStageGroups();
				
				// 添加所有关卡组到选择面板（在EDT线程中执行）
				javax.swing.SwingUtilities.invokeLater(() -> {
					try {
						for (StageGroup group : stageGroups) {
							selectPanel.addStageGroup(group);
							System.out.println("添加关卡组 " + group.getGroupName() + " - " + group.getDisplayName());
						}

						// 更新UI
						window.revalidate();
						window.repaint();
					} catch (Exception e) {
						LogUtil.error("Main", "添加关卡组到选择面板时出错", e);
					}
				});
			} catch (Exception e) {
				LogUtil.error("Main", "获取关卡组列表时出错", e);
			}
		}).start();
	}

	private static void startGame(StageGroup stageGroup) {
		titleScreen.stopTitleMusic();
		
		window.initializePlayer();
		window.getCenterPanel().removeAll();

		// 获取游戏画布并更新虚拟键盘按键状态提供者
		GameCanvas gameCanvas = window.getGameCanvas();
		window.getVirtualKeyboardPanel().setKeyStateProvider(gameCanvas);

		window.getCenterPanel().add(gameCanvas);

		// 重置游戏状态为新对局
		gameCanvas.resetGame();

		// 设置玩家位置到屏幕底部中心
		Player player = gameCanvas.getPlayer();
		if (player != null) {
			int canvasHeight = gameCanvas.getHeight();
			float actualPlayerX = 0; // 水平居中
			float actualPlayerY = -canvasHeight / 2.0f + 40; // 距离底部40像素(Y为负)
			player.setPosition(actualPlayerX, actualPlayerY);
			System.out.println("玩家出生位置: (" + actualPlayerX + ", " + actualPlayerY + ")");
		}

		// 启动关卡组
		gameCanvas.setStageGroup(stageGroup);
		stageGroup.start();

		gameCanvas.requestFocusInWindow();
		new stg.game.GameLoop(gameCanvas).start();
		System.out.println("游戏开始, 关卡组 " + stageGroup.getGroupName());
	}




	public static Window getWindow() {
		return window;
	}

	public static void returnToTitle() {
		// 停止游戏循环
		stg.game.GameLoop.stopAll();

		// 返回标题界面
		showTitleScreen();
	}
}

