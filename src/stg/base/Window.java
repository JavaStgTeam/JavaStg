package stg.base;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import stg.game.GameLoop;
import stg.game.player.Player;
import stg.game.ui.GameCanvas;
import stg.game.ui.GameStatusPanel;
import user.player.PlayerType;

/**
 * 窗口类 - STG游戏主窗口
 * @since 2026-02-02
 * @author JavaSTG Team
 */
public class Window extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel leftPanel;
    private JPanel centerPanel;
    private JPanel rightPanel;
    private GameCanvas gameCanvas;
    private VirtualKeyboardPanel virtualKeyboardPanel;
    private GameStatusPanel gameStatusPanel;
    private int totalWidth = 1280;
    private int totalHeight = 960;
    private Player player;

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
     */
    private void initialize() {
        initialize(true);
    }

    /**
     * 初始化窗口
     * @param initPlayer 是否立即初始化玩家
     */
    private void initialize(boolean initPlayer) {
        setTitle("STG Game Engine"); // 设置标题
        setSize(totalWidth, totalHeight); // 设置大小
        setLocationRelativeTo(null); // 居中显示
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // 关闭操作
        setResizable(false); // 禁止调整大小
        setLayout(new BorderLayout());

        // 计算三个面板的宽度(1:1.5:1)
        int leftWidth = (int)(totalWidth / 3.5); // 左侧面板宽度
        int centerWidth = (int)(totalWidth * 1.5 / 3.5); // 中间面板宽度
        int rightWidth = totalWidth - leftWidth - centerWidth; // 右侧面板宽度

        // 创建左侧面板
        leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(leftWidth, totalHeight));
        leftPanel.setMinimumSize(new Dimension(leftWidth, totalHeight));
        leftPanel.setMaximumSize(new Dimension(leftWidth, totalHeight));
        leftPanel.setBackground(new Color(30, 30, 40));
        leftPanel.setLayout(new BorderLayout());

        // 创建按键提示标签（上方）
        JLabel hintsLabel = new JLabel("<html><div style='font-family: Monospace; font-size: 14px; color: white;'>" +
            "<b>操作说明</b><br><br>" +
            "方向键 移动<br>" +
            "Z         射击<br>" +
            "Shift     低速模式<br>" +
            "X         预留<br><br>" +
            "<b>游戏信息</b><br><br>" +
            "受击判定半径: 2像素<br>" +
            "低速模式 显示判定点</div></html>");
        hintsLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        leftPanel.add(hintsLabel, BorderLayout.NORTH);

        // 创建中间面板
        centerPanel = new JPanel();
        centerPanel.setPreferredSize(new Dimension(centerWidth, totalHeight));
        centerPanel.setMinimumSize(new Dimension(centerWidth, totalHeight));
        centerPanel.setMaximumSize(new Dimension(centerWidth, totalHeight));
        centerPanel.setBackground(new Color(20, 20, 30));
        centerPanel.setLayout(new BorderLayout());

        // 创建游戏画布
        gameCanvas = new GameCanvas();
        centerPanel.add(gameCanvas, BorderLayout.CENTER);

        // 创建虚拟键盘面板（需要先创建gameCanvas）
        virtualKeyboardPanel = new VirtualKeyboardPanel(gameCanvas);
        leftPanel.add(virtualKeyboardPanel, BorderLayout.CENTER);

        // 创建右侧面板
        rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension(rightWidth, totalHeight));
        rightPanel.setMinimumSize(new Dimension(rightWidth, totalHeight));
        rightPanel.setMaximumSize(new Dimension(rightWidth, totalHeight));
        rightPanel.setBackground(new Color(30, 30, 40));
        rightPanel.setLayout(new BorderLayout());

        // 创建游戏状态面板
        gameStatusPanel = new GameStatusPanel();
        rightPanel.add(gameStatusPanel, BorderLayout.CENTER);

        // 将游戏状态面板传递给游戏画布
        gameCanvas.setGameStatusPanel(gameStatusPanel);

        // 根据参数决定是否立即初始化玩家
        if (initPlayer) {
            initializePlayer(PlayerType.DEFAULT);
            // 启动游戏循环（仅在初始化玩家时启动）
            new GameLoop(gameCanvas).start();
        }

        // 创建容器面板
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        mainPanel.add(leftPanel);
        mainPanel.add(centerPanel);
        mainPanel.add(rightPanel);

        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    /**
     * 获取左侧面板
     * @return 左侧面板
     */
    public JPanel getLeftPanel() {
        return leftPanel;
    }

    /**
     * 获取中间面板
     * @return 中间面板
     */
    public JPanel getCenterPanel() {
        return centerPanel;
    }

    /**
     * 获取右侧面板
     * @return 右侧面板
     */
    public JPanel getRightPanel() {
        return rightPanel;
    }

    /**
     * 获取游戏画布
     * @return 游戏画布
     */
    public GameCanvas getGameCanvas() {
        return gameCanvas;
    }

    /**
     * 获取虚拟键盘面板
     * 添加getter以支持按键状态切换
     * @return 虚拟键盘面板
     * @since 2026-01-20
     */
    public VirtualKeyboardPanel getVirtualKeyboardPanel() {
        return virtualKeyboardPanel;
    }

    /**
     * 获取游戏状态面板
     * 添加getter以支持游戏状态访问
     * @return 游戏状态面板
     * @since 2026-01-24
     */
    public GameStatusPanel getGameStatusPanel() {
        return gameStatusPanel;
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
     * 初始化玩家（使用指定类型）
     * @param type 自机类型
     */
    public void initializePlayer(PlayerType type) {
        // 使用工厂创建玩家
        gameCanvas.setPlayer(type, 0.0f, 0.0f);
        player = gameCanvas.getPlayer(); // 获取创建的玩家
        // 添加窗口监听器，在窗口显示后设置正确的玩家位置
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                SwingUtilities.invokeLater(() -> {
                    int canvasWidth = gameCanvas.getWidth();
                    int canvasHeight = gameCanvas.getHeight();
                    float actualPlayerX = 0;
                    float actualPlayerY = -canvasHeight / 2.0f + 40;
                    player.setPosition(actualPlayerX, actualPlayerY);
                });
            }
        });
    }
}

