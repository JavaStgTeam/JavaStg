package stg.base;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import stg.core.GameLoop;
import stg.render.GLRenderer;
import stg.render.GamePanel;
import stg.render.IRenderer;
import stg.render.LeftPanel;
import stg.render.RightPanel;
import stg.render.TitlePanel;
import stg.util.ALAudioManager;
import stg.util.CoordinateSystem;
import user.player.DefaultPlayer;

/**
 * 窗口类 - STG游戏主窗口
 * 窗口被两条竖线分割成三个面板
 * @since 2026-02-02
 * @author JavaSTG Team
 * @date 2026-02-23 重写为三面板布局
 */
public class Window {
	/** 窗口总宽度 */
	private static final int TOTAL_WIDTH = 1280;
	/** 窗口总高度 */
	private static final int TOTAL_HEIGHT = 960;
	/** 分割线颜色 */
	private static final float DIVIDER_COLOR_R = 0.3f;
	private static final float DIVIDER_COLOR_G = 0.3f;
	private static final float DIVIDER_COLOR_B = 0.3f;
	private static final float DIVIDER_COLOR_A = 1.0f;
	
	/** GLFW窗口句柄 */
	private long windowHandle;
	/** 渲染器 */
	private IRenderer renderer;
	/** 左侧面板 */
	private LeftPanel leftPanel;
	/** 主游戏面板 */
	private GamePanel gamePanel;
	/** 右侧面板 */
	private RightPanel rightPanel;
	/** 标题面板 */
	private TitlePanel titlePanel;
	/** 游戏循环 */
	private GameLoop gameLoop;
	/** 玩家实例 */
	private DefaultPlayer player;
	/** 坐标系统 */
	private CoordinateSystem coordinateSystem;
	/** 是否已初始化 */
	private boolean initialized = false;
	/** 是否显示标题页面 */
	private boolean showTitleScreen = true;
	/** 按键状态 */
	private boolean[] keyStates = new boolean[GLFW.GLFW_KEY_LAST];
	/** 按键状态提供者 */
	private KeyStateProvider keyStateProvider;
	
	/**
	 * 构造函数
	 */
	public Window() {
		initialize();
	}
	
	/**
	 * 初始化窗口
	 */
	private void initialize() {
		try {
			initGLFW();
			createWindow();
			setupCallbacks();
			centerWindow();
			initOpenGL();
			initPanels();
			showWindow();
			initialized = true;
			System.out.println("窗口初始化成功");
		} catch (Exception e) {
			System.err.println("窗口初始化失败: " + e.getMessage());
			e.printStackTrace();
			cleanup();
		}
	}
	
	/**
	 * 初始化GLFW
	 */
	private void initGLFW() {
		if (!GLFW.glfwInit()) {
			throw new IllegalStateException("无法初始化GLFW");
		}
		GLFWErrorCallback.createPrint(System.err).set();
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 2);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 1);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_ANY_PROFILE);
	}
	
	/**
	 * 创建GLFW窗口
	 */
	private void createWindow() {
		windowHandle = GLFW.glfwCreateWindow(TOTAL_WIDTH, TOTAL_HEIGHT, "JavaStg Engine", 0, 0);
		if (windowHandle == 0) {
			throw new IllegalStateException("无法创建GLFW窗口");
		}
		System.out.println("GLFW窗口创建成功: " + TOTAL_WIDTH + "x" + TOTAL_HEIGHT);
	}
	
	/**
	 * 设置回调函数
	 */
	private void setupCallbacks() {
		GLFW.glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
			if (key >= 0 && key < keyStates.length) {
				if (action == GLFW.GLFW_PRESS) {
					keyStates[key] = true;
				} else if (action == GLFW.GLFW_RELEASE) {
					keyStates[key] = false;
				}
			}
			
			if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
				GLFW.glfwSetWindowShouldClose(window, true);
			}
		});
		
		keyStateProvider = new KeyStateProvider() {
			@Override
			public boolean isUpPressed() {
				return keyStates[GLFW.GLFW_KEY_UP];
			}
			
			@Override
			public boolean isDownPressed() {
				return keyStates[GLFW.GLFW_KEY_DOWN];
			}
			
			@Override
			public boolean isLeftPressed() {
				return keyStates[GLFW.GLFW_KEY_LEFT];
			}
			
			@Override
			public boolean isRightPressed() {
				return keyStates[GLFW.GLFW_KEY_RIGHT];
			}
			
			@Override
			public boolean isZPressed() {
				return keyStates[GLFW.GLFW_KEY_Z];
			}
			
			@Override
			public boolean isShiftPressed() {
				return keyStates[GLFW.GLFW_KEY_LEFT_SHIFT] || keyStates[GLFW.GLFW_KEY_RIGHT_SHIFT];
			}
			
			@Override
			public boolean isXPressed() {
				return keyStates[GLFW.GLFW_KEY_X];
			}
		};
	}
	
	/**
	 * 居中窗口
	 */
	private void centerWindow() {
		long monitor = GLFW.glfwGetPrimaryMonitor();
		if (monitor != 0) {
			org.lwjgl.glfw.GLFWVidMode vidMode = GLFW.glfwGetVideoMode(monitor);
			if (vidMode != null) {
				GLFW.glfwSetWindowPos(windowHandle, 
						(vidMode.width() - TOTAL_WIDTH) / 2, 
						(vidMode.height() - TOTAL_HEIGHT) / 2);
			}
		}
	}
	
	/**
	 * 显示窗口
	 */
	private void showWindow() {
		GLFW.glfwShowWindow(windowHandle);
	}
	
	/**
	 * 初始化OpenGL
	 */
	private void initOpenGL() {
		GLFW.glfwMakeContextCurrent(windowHandle);
		GL.createCapabilities();
		GLFW.glfwSwapInterval(1);
		
		renderer = new GLRenderer();
		renderer.initialize(TOTAL_WIDTH, TOTAL_HEIGHT);
	}
	
	/**
	 * 初始化面板
	 * 计算三个面板的位置和尺寸
	 * 中间面板宽高比为3:4
	 */
	private void initPanels() {
		int gamePanelWidth = TOTAL_HEIGHT * 3 / 4;
		int sidePanelWidth = (TOTAL_WIDTH - gamePanelWidth) / 2;
		
		leftPanel = new LeftPanel(0, 0, sidePanelWidth, TOTAL_HEIGHT);
		gamePanel = new GamePanel(sidePanelWidth, 0, gamePanelWidth, TOTAL_HEIGHT);
		rightPanel = new RightPanel(sidePanelWidth + gamePanelWidth, 0, sidePanelWidth, TOTAL_HEIGHT);
		
		// 创建标题面板
		titlePanel = new TitlePanel(0, 0, TOTAL_WIDTH, TOTAL_HEIGHT, new TitlePanel.TitleCallback() {
			@Override
			public void onGameStart() {
				// 开始游戏
				showTitleScreen = false;
				// 停止标题音乐
				ALAudioManager.getInstance().stopMusic("title");
				startGameLoop();
				System.out.println("开始游戏");
			}
			
			@Override
			public void onExit() {
				// 退出游戏
				GLFW.glfwSetWindowShouldClose(windowHandle, true);
				System.out.println("退出游戏");
			}
		});
		titlePanel.setKeyStateProvider(keyStateProvider);
		// 加载标题面板背景纹理
		titlePanel.loadBackgroundTexture(renderer);
		
		coordinateSystem = new CoordinateSystem(gamePanelWidth, TOTAL_HEIGHT);
		stg.entity.base.Obj.setSharedCoordinateSystem(coordinateSystem);
		
		player = new DefaultPlayer(0.0f, -200.0f);
		player.setKeyStateProvider(keyStateProvider);
		gamePanel.setPlayer(player);
		
		leftPanel.setKeyStateProvider(keyStateProvider);
		
		// 加载标题音乐
		ALAudioManager audioManager = ALAudioManager.getInstance();
		audioManager.init();
		// 使用绝对路径加载音频文件
		String musicPath = "e:\\Myproject\\Game\\jstg_Team\\JavaStg\\resources\\audio\\music\\luastg 0.08.540 - 1.27.800.ogg";
		audioManager.loadMusic("title", musicPath);
		// 播放标题音乐（循环）
		audioManager.playMusic("title", true);
		System.out.println("面板布局: 左侧=" + sidePanelWidth + ", 中间=" + gamePanelWidth + ", 右侧=" + sidePanelWidth);
	}
	
	/**
	 * 启动游戏循环
	 */
	public void startGameLoop() {
		if (gameLoop == null) {
			gameLoop = new GameLoop(this);
			gameLoop.start();
		}
	}
	
	/**
	 * 获取按键状态提供者
	 * @return 按键状态提供者
	 */
	public KeyStateProvider getKeyStateProvider() {
		return keyStateProvider;
	}
	
	/**
	 * 获取玩家
	 * @return 玩家实例
	 */
	public DefaultPlayer getPlayer() {
		return player;
	}
	
	/**
	 * 渲染所有面板
	 */
	public void render() {
		renderer.beginFrame();
		renderer.clear(0.0f, 0.0f, 0.0f, 1.0f);
		
		if (showTitleScreen) {
			// 显示标题页面
			renderer.setViewport(0, 0, TOTAL_WIDTH, TOTAL_HEIGHT);
			titlePanel.render(renderer);
		} else {
			// 显示游戏面板
			renderer.setViewport(leftPanel.getX(), leftPanel.getY(), leftPanel.getWidth(), leftPanel.getHeight());
			leftPanel.render(renderer);
			
			renderer.setViewport(gamePanel.getX(), gamePanel.getY(), gamePanel.getWidth(), gamePanel.getHeight());
			gamePanel.render(renderer);
			
			renderer.setViewport(rightPanel.getX(), rightPanel.getY(), rightPanel.getWidth(), rightPanel.getHeight());
			rightPanel.render(renderer);
			
			renderer.setViewport(0, 0, TOTAL_WIDTH, TOTAL_HEIGHT);
			renderDividers();
		}
		
		renderer.endFrame();
	}
	
	/**
	 * 渲染分割线
	 */
	private void renderDividers() {
		int gamePanelX = gamePanel.getX();
		int rightPanelX = rightPanel.getX();
		
		renderer.drawLine(gamePanelX, 0, gamePanelX, TOTAL_HEIGHT, 
				DIVIDER_COLOR_R, DIVIDER_COLOR_G, DIVIDER_COLOR_B, DIVIDER_COLOR_A);
		renderer.drawLine(rightPanelX, 0, rightPanelX, TOTAL_HEIGHT, 
				DIVIDER_COLOR_R, DIVIDER_COLOR_G, DIVIDER_COLOR_B, DIVIDER_COLOR_A);
	}
	
	/**
	 * 更新窗口标题
	 * @param objCount 对象数量
	 * @param fps 当前FPS
	 */
	public void updateTitle(int objCount, int fps) {
		if (windowHandle != 0) {
			GLFW.glfwSetWindowTitle(windowHandle, "JavaStg Engine  obj=" + objCount + "  fps=" + fps);
		}
	}
	
	/**
	 * 检查窗口是否应该关闭
	 * @return 是否应该关闭
	 */
	public boolean shouldClose() {
		return windowHandle != 0 && GLFW.glfwWindowShouldClose(windowHandle);
	}
	
	/**
	 * 交换缓冲区
	 */
	public void swapBuffers() {
		if (windowHandle != 0) {
			GLFW.glfwSwapBuffers(windowHandle);
		}
	}
	
	/**
	 * 轮询事件
	 */
	public void pollEvents() {
		GLFW.glfwPollEvents();
	}
	
	/**
	 * 清理资源
	 */
	public void cleanup() {
		if (renderer != null) {
			renderer.cleanup();
			renderer = null;
		}
		
		// 清理音频资源
		ALAudioManager.getInstance().cleanup();
		
		if (windowHandle != 0) {
			GLFW.glfwDestroyWindow(windowHandle);
			windowHandle = 0;
		}
		
		GLFW.glfwTerminate();
		GLFWErrorCallback callback = GLFW.glfwSetErrorCallback(null);
		if (callback != null) {
			callback.free();
		}
		
		initialized = false;
		System.out.println("窗口资源清理完成");
	}
	
	/**
	 * 检查是否初始化成功
	 * @return 是否初始化成功
	 */
	public boolean isInitialized() {
		return initialized;
	}
	
	/**
	 * 获取窗口总宽度
	 * @return 窗口总宽度
	 */
	public int getTotalWidth() {
		return TOTAL_WIDTH;
	}
	
	/**
	 * 获取窗口总高度
	 * @return 窗口总高度
	 */
	public int getTotalHeight() {
		return TOTAL_HEIGHT;
	}
	
	/**
	 * 获取渲染器
	 * @return 渲染器
	 */
	public IRenderer getRenderer() {
		return renderer;
	}
	
	/**
	 * 获取左侧面板
	 * @return 左侧面板
	 */
	public LeftPanel getLeftPanel() {
		return leftPanel;
	}
	
	/**
	 * 获取主游戏面板
	 * @return 主游戏面板
	 */
	public GamePanel getGamePanel() {
		return gamePanel;
	}
	
	/**
	 * 获取右侧面板
	 * @return 右侧面板
	 */
	public RightPanel getRightPanel() {
		return rightPanel;
	}
	
	/**
	 * 获取对象数量
	 * @return 对象数量
	 */
	public int getObjectCount() {
		return 0;
	}
}
