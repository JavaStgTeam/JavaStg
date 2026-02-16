package stg.base;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import stg.game.GameLoop;
import stg.game.player.Player;
import stg.game.ui.GameCanvas;
import stg.game.ui.GameStatusPanel;

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

    // 拖拽相关变量
    private boolean isDragging = false;
    private int dragStartX, dragStartY;

    /**
     * 初始化窗口
     * @param initPlayer 是否立即初始化玩家
     */
    private void initialize(boolean initPlayer) {
        setTitle("STG Game Engine"); // 设置标题
        setSize(totalWidth, totalHeight); // 设置大小
        setLocationRelativeTo(null); // 居中显示
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // 关闭操作
        setResizable(true); // 允许调整大小
        setLayout(new BorderLayout());
        
        // 添加鼠标事件监听器实现窗口拖拽
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    // 检查是否点击在标题栏区域
                    if (e.getY() < getInsets().top) {
                        isDragging = true;
                        dragStartX = e.getX();
                        dragStartY = e.getY();
                    }
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                isDragging = false;
            }
        });
        
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDragging) {
                    int newX = getLocation().x + (e.getX() - dragStartX);
                    int newY = getLocation().y + (e.getY() - dragStartY);
                    setLocation(newX, newY);
                }
            }
        });

        // 计算三个面板的宽度(1:1.5:1)
        int leftWidth = (int)(totalWidth / 3.5); // 左侧面板宽度
        int centerWidth = (int)(totalWidth * 1.5 / 3.5); // 中间面板宽度
        int rightWidth = totalWidth - leftWidth - centerWidth; // 右侧面板宽度

        // 创建左侧面板
        leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(leftWidth, totalHeight));
        leftPanel.setMinimumSize(new Dimension(100, totalHeight)); // 只设置最小宽度
        leftPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, totalHeight)); // 移除最大宽度限制
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
        centerPanel.setMinimumSize(new Dimension(300, totalHeight)); // 只设置最小宽度
        centerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, totalHeight)); // 移除最大宽度限制
        centerPanel.setBackground(new Color(20, 20, 30));
        centerPanel.setLayout(new BorderLayout());

        // 创建游戏画布
        gameCanvas = new GameCanvas();
        gameCanvas.setSize(centerWidth, totalHeight);
        centerPanel.add(gameCanvas, BorderLayout.CENTER);

        // 创建虚拟键盘面板（需要先创建gameCanvas）
        virtualKeyboardPanel = new VirtualKeyboardPanel(gameCanvas);
        leftPanel.add(virtualKeyboardPanel, BorderLayout.CENTER);

        // 创建右侧面板
        rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension(rightWidth, totalHeight));
        rightPanel.setMinimumSize(new Dimension(100, totalHeight)); // 只设置最小宽度
        rightPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, totalHeight)); // 移除最大宽度限制
        rightPanel.setBackground(new Color(30, 30, 40));
        rightPanel.setLayout(new BorderLayout());

        // 创建游戏状态面板
        gameStatusPanel = new GameStatusPanel();
        rightPanel.add(gameStatusPanel, BorderLayout.CENTER);

        // 将游戏状态面板传递给游戏画布
        gameCanvas.setGameStatusPanel(gameStatusPanel);

        // 根据参数决定是否立即初始化玩家
        if (initPlayer) {
            initializePlayer();
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
     * 初始化玩家
     */
    public void initializePlayer() {
        // 创建默认玩家
        gameCanvas.setPlayer(0.0f, 0.0f);
        player = gameCanvas.getPlayer(); // 获取创建的玩家
        // 添加窗口监听器，在窗口显示后设置正确的玩家位置
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                SwingUtilities.invokeLater(() -> {
                    // 使用游戏逻辑坐标系的固定高度计算初始位置，确保玩家始终在屏幕内出生
                    // 游戏逻辑坐标系：Y轴向上为正，底部是-240，顶部是240
                    float actualPlayerX = 0;
                    // 在屏幕底部上方40像素处出生
                    float actualPlayerY = -stg.util.GameConstants.GAME_HEIGHT / 2.0f + 40;
                    player.setPosition(actualPlayerX, actualPlayerY);
                });
            }
            
            @Override
            public void componentResized(ComponentEvent e) {
                SwingUtilities.invokeLater(() -> {
                    // 更新窗口尺寸
                    totalWidth = getWidth();
                    totalHeight = getHeight();
                    
                    // 计算中间面板的理想宽度（保持3:4的比例）
                    // 3:4比例意味着宽度是高度的0.75倍
                    float idealRatio = 3.0f / 4.0f; // 明确设置为3:4比例
                    int centerWidth = (int)(totalHeight * idealRatio);
                    
                    // 确保中间面板宽度严格符合3:4比例，避免整数取整误差
                    // 重新计算，确保宽度是高度的3/4
                    centerWidth = (totalHeight * 3) / 4;
                    
                    // 计算剩余宽度，分配给左右侧面板
                    int remainingWidth = totalWidth - centerWidth;
                    int leftWidth = remainingWidth / 2;
                    int rightWidth = remainingWidth - leftWidth;
                    
                    // 确保中间面板至少有最小宽度
                    if (centerWidth < 300) {
                        centerWidth = 300;
                        remainingWidth = totalWidth - centerWidth;
                        leftWidth = remainingWidth / 2;
                        rightWidth = remainingWidth - leftWidth;
                    }
                    
                    // 确保左右侧面板至少有最小宽度
                    if (leftWidth < 100) {
                        leftWidth = 100;
                        rightWidth = totalWidth - centerWidth - leftWidth;
                    }
                    if (rightWidth < 100) {
                        rightWidth = 100;
                        leftWidth = totalWidth - centerWidth - rightWidth;
                    }
                    
                    // 确保所有面板宽度之和等于窗口宽度，避免白边
                    int totalPanelWidth = leftWidth + centerWidth + rightWidth;
                    if (totalPanelWidth != totalWidth) {
                        // 调整右侧面板宽度以填充剩余空间
                        rightWidth += (totalWidth - totalPanelWidth);
                    }
                    
                    // 确保右侧面板不会为负数
                    if (rightWidth < 100) {
                        rightWidth = 100;
                        // 重新计算左侧面板宽度
                        leftWidth = totalWidth - centerWidth - rightWidth;
                        // 确保左侧面板也不为负数
                        if (leftWidth < 100) {
                            leftWidth = 100;
                            // 最后调整中间面板宽度
                            centerWidth = totalWidth - leftWidth - rightWidth;
                        }
                    }
                    
                    // 更新左侧面板尺寸
                    leftPanel.setPreferredSize(new Dimension(leftWidth, totalHeight));
                    leftPanel.setMinimumSize(new Dimension(leftWidth, totalHeight));
                    leftPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, totalHeight)); // 移除最大宽度限制
                    leftPanel.setSize(leftWidth, totalHeight);
                    
                    // 更新中间面板尺寸
                    centerPanel.setPreferredSize(new Dimension(centerWidth, totalHeight));
                    centerPanel.setMinimumSize(new Dimension(centerWidth, totalHeight));
                    centerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, totalHeight)); // 移除最大宽度限制
                    centerPanel.setSize(centerWidth, totalHeight);
                    
                    // 更新右侧面板尺寸
                    rightPanel.setPreferredSize(new Dimension(rightWidth, totalHeight));
                    rightPanel.setMinimumSize(new Dimension(rightWidth, totalHeight));
                    rightPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, totalHeight)); // 移除最大宽度限制
                    rightPanel.setSize(rightWidth, totalHeight);
                    
                    // 更新游戏画布尺寸
                    gameCanvas.setSize(centerWidth, totalHeight);
                    
                    // 确保内部组件也调整大小
                    if (virtualKeyboardPanel != null) {
                        virtualKeyboardPanel.setSize(leftWidth, totalHeight);
                        virtualKeyboardPanel.revalidate();
                        virtualKeyboardPanel.repaint();
                    }
                    
                    if (gameStatusPanel != null) {
                        gameStatusPanel.setSize(rightWidth, totalHeight);
                        gameStatusPanel.revalidate();
                        gameStatusPanel.repaint();
                    }
                    
                    // 重新布局
                    revalidate();
                    repaint();
                    
                    // 如果玩家存在，更新玩家位置
                    if (player != null) {
                        // 使用游戏逻辑坐标系的固定高度计算位置，确保玩家始终在屏幕内
                        float actualPlayerX = 0;
                        float actualPlayerY = -stg.util.GameConstants.GAME_HEIGHT / 2.0f + 40;
                        player.setPosition(actualPlayerX, actualPlayerY);
                    }
                });
            }
        });
    }
}

