package Main;

import stg.base.Window;
import stg.service.core.ServiceManager;
import stg.stage.StageGroup;
import stg.stage.StageGroupManager;
import stg.ui.GameCanvas;
import stg.ui.StageGroupSelectPanel;
import stg.ui.TitleScreen;

public class Main implements TitleScreen.TitleCallback {
	private static Window window;
	public static TitleScreen titleScreen;
	public static StageGroupSelectPanel stageGroupSelectPanel;
	public static boolean showTitleScreen = true;
	public static boolean showStageGroupSelect = false;

	public static void main(String[] args) {
		System.out.println("启动 STG 游戏引擎");

		// 添加全局异常捕获
		Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
			System.err.println("全局未捕获异常: " + throwable.getMessage());
			throwable.printStackTrace();
			System.exit(1);
		});

		try {
			// 初始化服务管理器
			ServiceManager.getInstance().initializeAllServices();
			System.out.println("服务管理器初始化完成");

			// 创建窗口
			window = new Window(true);
			
			// 创建标题页面
			titleScreen = new TitleScreen(new Main());
			
			// 游戏循环由GameLoop处理
			// 这里会阻塞，直到游戏结束
			System.out.println("游戏已启动");
		} catch (Exception e) {
			System.err.println("初始化异常: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static void startDefaultGame() {
		try {
			// 获取游戏画布
			GameCanvas gameCanvas = window.getGameCanvas();
			
			// 重置游戏状态
			gameCanvas.resetGame();
			
			// 启动默认关卡组
			StageGroupManager stageGroupManager = StageGroupManager.getInstance();
			stageGroupManager.init(gameCanvas);
			
			// 获取第一个关卡组并启动
			if (!stageGroupManager.getStageGroups().isEmpty()) {
				StageGroup stageGroup = stageGroupManager.getStageGroups().get(0);
				System.out.println("开始游戏, 关卡组 " + stageGroup.getGroupName());
				
				// 设置关卡组
				gameCanvas.setStageGroup(stageGroup);
				stageGroup.reset();
				stageGroup.start();
				
				System.out.println("游戏开始, 关卡组 " + stageGroup.getGroupName());
			} else {
				System.out.println("没有找到关卡组，启动默认游戏");
			}
		} catch (Exception e) {
			System.err.println("启动游戏异常: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static Window getWindow() {
		return window;
	}

	@Override
	public void onStageGroupSelect() {
		showTitleScreen = false;
		showStageGroupSelect = true;
		stageGroupSelectPanel = new StageGroupSelectPanel(new StageGroupSelectPanel.StageGroupSelectCallback() {
			@Override
			public void onStageGroupSelected(StageGroup stageGroup) {
				startGameWithStageGroup(stageGroup);
			}
			
			@Override
			public void onBack() {
				showStageGroupSelect = false;
				showTitleScreen = true;
			}
		});
	}

	@Override
	public void onGameStart(StageGroup stageGroup) {
		showTitleScreen = false;
		showStageGroupSelect = false;
		startGameWithStageGroup(stageGroup);
	}

	@Override
	public void onExit() {
		System.exit(0);
	}

	private static void startGameWithStageGroup(StageGroup stageGroup) {
		// 停止标题音乐
		if (titleScreen != null) {
			titleScreen.stopTitleMusic();
		}
		
		// 获取游戏画布
		GameCanvas gameCanvas = window.getGameCanvas();
		
		// 重置游戏状态
		gameCanvas.resetGame();
		
		// 设置关卡组
		System.out.println("开始游戏, 关卡组 " + stageGroup.getGroupName());
		gameCanvas.setStageGroup(stageGroup);
		stageGroup.reset();
		stageGroup.start();
		System.out.println("游戏开始, 关卡组 " + stageGroup.getGroupName());
	}

	public static void returnToTitle() {
		// 获取游戏画布
		GameCanvas gameCanvas = window.getGameCanvas();
		
		// 重置游戏状态
		gameCanvas.resetGame();
		
		// 显示标题页面
		showTitleScreen = true;
		showStageGroupSelect = false;
		titleScreen = new TitleScreen(new Main());
	}
}

