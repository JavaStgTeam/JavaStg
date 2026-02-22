package stg.base;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import stg.core.GameLoop;
import stg.entity.player.Player;
import stg.ui.GameCanvas;

/**
 * 窗口类 - STG游戏主窗口
 * @since 2026-02-02
 * @author JavaSTG Team
 */
public class Window {
    private long window;
    private GameCanvas gameCanvas;
    private int totalWidth = 1280;
    private int totalHeight = 960;
    private Player player;
    private boolean initialized = false;

    /**
     * 构造函数
     */
    public Window() {
        initialize(true);
    }

    /**
     * 构造函数
     * @param initPlayer 是否立即初始化玩家
     */
    public Window(boolean initPlayer) {
        initialize(initPlayer);
    }

    /**
     * 初始化窗口
     * @param initPlayer 是否立即初始化玩家
     */
    private void initialize(boolean initPlayer) {
        try {
            // 初始化GLFW
            if (!GLFW.glfwInit()) {
                throw new IllegalStateException("无法初始化GLFW");
            }

            // 设置错误回调
            GLFWErrorCallback.createPrint(System.err).set();

            // 配置窗口
            GLFW.glfwDefaultWindowHints();
            GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
            GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
            GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
            GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
            GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_COMPAT_PROFILE);
            GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);

            // 创建窗口
            System.out.println("创建GLFW窗口，宽度: " + totalWidth + "，高度: " + totalHeight);
            window = GLFW.glfwCreateWindow(totalWidth, totalHeight, "JavaStg Engine  obj=0  fps=0", 0, 0);
            if (window == 0) {
                System.err.println("GLFW窗口创建失败");
                throw new IllegalStateException("无法创建GLFW窗口");
            }
            System.out.println("GLFW窗口创建成功，窗口句柄: " + window);

            // 设置键盘回调
            GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
                if (gameCanvas != null) {
                    gameCanvas.handleKeyEvent(key, action);
                }
                if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
                    GLFW.glfwSetWindowShouldClose(window, true);
                }
            });

            // 居中窗口
            long monitor = GLFW.glfwGetPrimaryMonitor();
            if (monitor != 0) {
                org.lwjgl.glfw.GLFWVidMode vidMode = GLFW.glfwGetVideoMode(monitor);
                if (vidMode != null) {
                    GLFW.glfwSetWindowPos(window, (vidMode.width() - totalWidth) / 2, (vidMode.height() - totalHeight) / 2);
                }
            }

            // 创建OpenGL上下文
            GLFW.glfwMakeContextCurrent(window);
            GL.createCapabilities();

            // 启用垂直同步
            GLFW.glfwSwapInterval(1);

            // 显示窗口
            GLFW.glfwShowWindow(window);

            // 创建游戏画布
            gameCanvas = new GameCanvas(window);

            // 根据参数决定是否立即初始化玩家
            if (initPlayer) {
                initializePlayer();
                // 启动游戏循环（仅在初始化玩家时启动）
                GameLoop gameLoop = new GameLoop(gameCanvas);
                gameLoop.setWindow(this);
                gameLoop.start();
            }

            initialized = true;
            System.out.println("GLFW窗口初始化成功");
        } catch (Exception e) {
            System.err.println("窗口初始化失败: " + e.getMessage());
            e.printStackTrace();
            cleanup();
        }
    }

    /**
     * 获取游戏画布
     * @return 游戏画布
     */
    public GameCanvas getGameCanvas() {
        return gameCanvas;
    }

    /**
     * 获取窗口句柄
     * @return 窗口句柄
     */
    public long getWindow() {
        return window;
    }

    /**
     * 更新窗口标题，显示对象数量和FPS
     * @param objCount 对象数量
     * @param fps 当前FPS
     */
    public void updateTitle(int objCount, int fps) {
        if (window != 0) {
            GLFW.glfwSetWindowTitle(window, "JavaStg Engine  obj=" + objCount + "  fps=" + fps);
        }
    }

    /**
     * 获取窗口总宽度
     * @return 窗口总宽度
     */
    public int getTotalWidth() {
        return totalWidth;
    }

    /**
     * 获取窗口总高度
     * @return 窗口总高度
     */
    public int getTotalHeight() {
        return totalHeight;
    }

    /**
     * 检查窗口是否应该关闭
     * @return 是否应该关闭
     */
    public boolean shouldClose() {
        return window != 0 && GLFW.glfwWindowShouldClose(window);
    }

    /**
     * 交换缓冲区
     */
    public void swapBuffers() {
        if (window != 0) {
            GLFW.glfwSwapBuffers(window);
        }
    }

    /**
     * 轮询事件
     */
    public void pollEvents() {
        GLFW.glfwPollEvents();
    }

    /**
     * 初始化玩家
     */
    public void initializePlayer() {
        // 创建默认玩家
        gameCanvas.setPlayer(0.0f, 0.0f);
        player = gameCanvas.getPlayer(); // 获取创建的玩家
        
        // 设置玩家初始位置
        if (player != null) {
            // 使用游戏逻辑坐标系的固定高度计算初始位置，确保玩家始终在屏幕内出生
            // 游戏逻辑坐标系：Y轴向上为正，底部是-240，顶部是240
            float actualPlayerX = 0;
            // 在屏幕底部上方40像素处出生
            float actualPlayerY = -stg.util.GameConstants.GAME_HEIGHT / 2.0f + 40;
            player.setPosition(actualPlayerX, actualPlayerY);
        }
    }

    /**
     * 清理资源
     */
    public void cleanup() {
        if (gameCanvas != null) {
            gameCanvas.cleanup();
            gameCanvas = null;
        }

        if (window != 0) {
            GLFW.glfwDestroyWindow(window);
            window = 0;
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
}

