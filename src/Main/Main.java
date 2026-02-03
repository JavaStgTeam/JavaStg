package Main;

import java.awt.EventQueue;
import stg.base.Window;
import stg.game.player.Player;
import stg.game.stage.StageGroup;
import stg.game.stage.StageGroupManager;
import stg.game.ui.GameCanvas;
import stg.game.ui.TitleScreen;
import user.player.PlayerType;

public class Main {
	private static Window window;
	private static TitleScreen titleScreen;
	private static PlayerType selectedPlayerType = PlayerType.DEFAULT;

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
			public void onStageGroupSelect(PlayerType playerType) {
				System.out.println("选择关卡组 " + playerType.getName());
				showStageGroupSelect(playerType);
			}

			@Override
			public void onGameStart(StageGroup stageGroup, PlayerType playerType) {
				System.out.println("开始游戏 " + playerType.getName() + ", 关卡组 " + stageGroup.getGroupName());
				startGame(stageGroup, playerType);
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

	private static void showStageGroupSelect(PlayerType playerType) {
		stg.game.ui.StageGroupSelectPanel selectPanel = new stg.game.ui.StageGroupSelectPanel(


			new stg.game.ui.StageGroupSelectPanel.StageGroupSelectCallback() {
				@Override
				public void onStageGroupSelected(StageGroup stageGroup, PlayerType type) {
					System.out.println("选择关卡组 " + stageGroup.getGroupName());
					startGame(stageGroup, type);
				}

				@Override
				public void onBack() {
					System.out.println("返回自机选择");
					showTitleScreen();
				}
			}, playerType
		);

		// 使用StageGroupManager获取关卡组列表
		stg.game.ui.GameCanvas gameCanvas = window.getGameCanvas();
		StageGroupManager stageGroupManager = StageGroupManager.getInstance();
		stageGroupManager.init(gameCanvas);

		// 添加所有关卡组到选择面板
		java.util.List<StageGroup> stageGroups = stageGroupManager.getStageGroups();
		for (StageGroup group : stageGroups) {
			selectPanel.addStageGroup(group);
			System.out.println("添加关卡组 " + group.getGroupName() + " - " + group.getDisplayName());
		}

		window.getCenterPanel().removeAll();
		window.getCenterPanel().add(selectPanel);
		window.getVirtualKeyboardPanel().setKeyStateProvider(selectPanel);
		selectPanel.requestFocusInWindow();
		window.revalidate();
		window.repaint();
	}

	private static void startGame(StageGroup stageGroup, PlayerType playerType) {
		titleScreen.stopTitleMusic();
		
		window.initializePlayer(playerType);
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
			float actualPlayerY = -canvasHeight / 2.0f + 40; // 距离底部40像素(Y为负�?
			player.setPosition(actualPlayerX, actualPlayerY);
			System.out.println("玩家出生位置: (" + actualPlayerX + ", " + actualPlayerY + ")");
		}

		// 启动关卡组
		gameCanvas.setStageGroup(stageGroup);
		stageGroup.start();

		gameCanvas.requestFocusInWindow();
		new stg.game.GameLoop(gameCanvas).start();
		System.out.println("游戏开始，自机: " + playerType.getName() + ", 关卡组 " + stageGroup.getGroupName());
	}



	public static Window getWindow() {
		return window;
	}

	public static PlayerType getSelectedPlayerType() {
		return selectedPlayerType;
	}

	public static void returnToTitle() {
		// 停止游戏循环
		stg.game.GameLoop.stopAll();

		// 返回标题界面
		showTitleScreen();
	}
}

