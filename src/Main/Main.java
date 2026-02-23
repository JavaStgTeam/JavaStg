package Main;

import stg.base.Window;

/**
 * 主入口类
 * @since 2026-02-02
 * @author JavaSTG Team
 * @date 2026-02-23 更新为使用新的三面板窗口系统
 */
public class Main {
	/** 窗口实例 */
	private static Window window;
	
	/**
	 * 主入口方法
	 * @param args 命令行参数
	 */
	public static void main(String[] args) {
		System.out.println("启动 STG 游戏引擎");
		
		Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
			System.err.println("全局未捕获异常: " + throwable.getMessage());
			throwable.printStackTrace();
			System.exit(1);
		});
		
		try {
			window = new Window();
			System.out.println("游戏已启动");
			window.startGameLoop();
			System.out.println("游戏循环已结束");
		} catch (Exception e) {
			System.err.println("初始化异常: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * 获取窗口实例
	 * @return 窗口实例
	 */
	public static Window getWindow() {
		return window;
	}
}
