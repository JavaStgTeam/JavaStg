package stg.game.ui;

import java.awt.Component;
import stg.base.KeyStateProvider;
import stg.game.player.Player;
import stg.util.CoordinateSystem;
import user.player.DefaultPlayer;

/**
 * 游戏画布类 - 负责游戏的渲染和输入处理
 * @since 2026-01-20
 */
public class GameCanvas extends Component implements KeyStateProvider {
    // 按键状态
    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean zPressed = false;
    private boolean shiftPressed = false;
    private boolean xPressed = false;
    
    // 画布尺寸
    private int width = 800;
    private int height = 600;
    
    /**
     * 设置画布尺寸
     */
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        super.setSize(width, height);
    }
    
    /**
     * 设置按键状态
     */
    public void setUpPressed(boolean upPressed) {
        this.upPressed = upPressed;
    }
    
    public void setDownPressed(boolean downPressed) {
        this.downPressed = downPressed;
    }
    
    public void setLeftPressed(boolean leftPressed) {
        this.leftPressed = leftPressed;
    }
    
    public void setRightPressed(boolean rightPressed) {
        this.rightPressed = rightPressed;
    }
    
    public void setZPressed(boolean zPressed) {
        this.zPressed = zPressed;
    }
    
    public void setShiftPressed(boolean shiftPressed) {
        this.shiftPressed = shiftPressed;
    }
    
    public void setXPressed(boolean xPressed) {
        this.xPressed = xPressed;
    }
    
    // 实现KeyStateProvider接口
    @Override
    public boolean isUpPressed() {
        return upPressed;
    }
    
    @Override
    public boolean isDownPressed() {
        return downPressed;
    }
    
    @Override
    public boolean isLeftPressed() {
        return leftPressed;
    }
    
    @Override
    public boolean isRightPressed() {
        return rightPressed;
    }
    
    @Override
    public boolean isZPressed() {
        return zPressed;
    }
    
    @Override
    public boolean isShiftPressed() {
        return shiftPressed;
    }
    
    @Override
    public boolean isXPressed() {
        return xPressed;
    }
    
    // 游戏状态面板
    private GameStatusPanel gameStatusPanel;
    
    // 玩家
    private Player player;
    
    // 坐标系统
    private CoordinateSystem coordinateSystem;
    
    // 游戏渲染器
    private stg.game.GameRenderer gameRenderer;
    
    // 游戏世界
    private stg.game.GameWorld gameWorld;
    
    // 关卡组
    private stg.game.stage.StageGroup stageGroup;
    
    /**
     * 构造函数
     */
    public GameCanvas() {
        this.coordinateSystem = new CoordinateSystem(width, height);
        
        // 初始化游戏世界
        this.gameWorld = new stg.game.GameWorld();
        
        // 初始化游戏渲染器
        this.gameRenderer = new stg.game.GameRenderer(gameWorld, null, coordinateSystem);
        
        // 设置为可聚焦
        setFocusable(true);
        
        // 添加键盘事件监听器
        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                handleKeyPress(e);
            }
            
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                handleKeyRelease(e);
            }
        });
    }
    
    /**
     * 处理按键按下事件
     */
    private void handleKeyPress(java.awt.event.KeyEvent e) {
        switch (e.getKeyCode()) {
            case java.awt.event.KeyEvent.VK_UP:
                setUpPressed(true);
                break;
            case java.awt.event.KeyEvent.VK_DOWN:
                setDownPressed(true);
                break;
            case java.awt.event.KeyEvent.VK_LEFT:
                setLeftPressed(true);
                break;
            case java.awt.event.KeyEvent.VK_RIGHT:
                setRightPressed(true);
                break;
            case java.awt.event.KeyEvent.VK_Z:
                setZPressed(true);
                break;
            case java.awt.event.KeyEvent.VK_SHIFT:
                setShiftPressed(true);
                break;
            case java.awt.event.KeyEvent.VK_X:
                setXPressed(true);
                break;
        }
    }
    
    /**
     * 处理按键释放事件
     */
    private void handleKeyRelease(java.awt.event.KeyEvent e) {
        switch (e.getKeyCode()) {
            case java.awt.event.KeyEvent.VK_UP:
                setUpPressed(false);
                break;
            case java.awt.event.KeyEvent.VK_DOWN:
                setDownPressed(false);
                break;
            case java.awt.event.KeyEvent.VK_LEFT:
                setLeftPressed(false);
                break;
            case java.awt.event.KeyEvent.VK_RIGHT:
                setRightPressed(false);
                break;
            case java.awt.event.KeyEvent.VK_Z:
                setZPressed(false);
                break;
            case java.awt.event.KeyEvent.VK_SHIFT:
                setShiftPressed(false);
                break;
            case java.awt.event.KeyEvent.VK_X:
                setXPressed(false);
                break;
        }
    }
    
    /**
     * 设置游戏状态面板
     */
    public void setGameStatusPanel(GameStatusPanel gameStatusPanel) {
        this.gameStatusPanel = gameStatusPanel;
    }
    
    /**
     * 设置玩家
     */
    public void setPlayer(float x, float y) {
        // 使用默认自机类，发射两个主炮
        this.player = new user.player.DefaultPlayer(x, y);
        
        // 更新游戏渲染器中的玩家引用
        if (gameRenderer != null) {
            gameRenderer.setPlayer(player);
        }
    }
    
    /**
     * 获取玩家
     */
    public Player getPlayer() {
        return player;
    }
    
    /**
     * 重置游戏
     */
    public void resetGame() {
        // 这里只是一个占位实现，实际应该重置游戏状态
    }
    
    /**
     * 设置关卡组
     */
    public void setStageGroup(stg.game.stage.StageGroup stageGroup) {
        this.stageGroup = stageGroup;
    }
    
    /**
     * 请求焦点
     */
    public boolean requestFocusInWindow() {
        return super.requestFocusInWindow();
    }
    
    /**
     * 更新游戏
     */
    public void update() {
        // 获取实际的画布尺寸
        int actualWidth = getWidth();
        int actualHeight = getHeight();
        
        // 根据按键状态更新玩家移动
        if (player != null) {
            // 水平方向:同时按下左右键时保持静止
            if (isLeftPressed() && isRightPressed()) {
                player.stopHorizontal();
            } else if (isLeftPressed()) {
                player.moveLeft();
            } else if (isRightPressed()) {
                player.moveRight();
            } else {
                player.stopHorizontal();
            }
            
            // 垂直方向:同时按下上下键时保持静止
            if (isUpPressed() && isDownPressed()) {
                player.stopVertical();
            } else if (isUpPressed()) {
                player.moveUp();
            } else if (isDownPressed()) {
                player.moveDown();
            } else {
                player.stopVertical();
            }
            
            // 更新射击状态
            player.setShooting(isZPressed());
            
            // 更新低速模式
            player.setSlowMode(isShiftPressed());
            
            // 更新玩家状态
            player.update();
        }
        
        // 更新游戏世界
        if (gameWorld != null) {
            gameWorld.update(actualWidth, actualHeight);
        }
        
        // 更新关卡组状态
        if (stageGroup != null) {
            stageGroup.update();
        }
    }
    
    /**
     * 获取坐标系统
     */
    public CoordinateSystem getCoordinateSystem() {
        return coordinateSystem;
    }
    
    /**
     * 获取游戏世界
     */
    public stg.game.GameWorld getWorld() {
        return gameWorld;
    }
    
    /**
     * 绘制游戏
     */
    @Override
    public void paint(java.awt.Graphics g) {
        java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
        
        // 获取实际的画布尺寸
        int actualWidth = getWidth();
        int actualHeight = getHeight();
        
        // 绘制背景
        g2d.setColor(java.awt.Color.BLACK);
        g2d.fillRect(0, 0, actualWidth, actualHeight);
        
        // 直接绘制玩家，使用实际的画布尺寸
        if (player != null) {
            // 计算玩家在屏幕上的位置
            float screenX = player.getX() + actualWidth / 2;
            float screenY = -player.getY() + actualHeight / 2;
            
            // 绘制玩家
            g2d.setColor(java.awt.Color.WHITE);
            g2d.fillOval((int)(screenX - 10), (int)(screenY - 10), 20, 20);
        }
        
        // 绘制关卡组信息
        if (stageGroup != null) {
            g2d.setColor(java.awt.Color.WHITE);
            g2d.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 12));
            g2d.drawString("关卡组: " + stageGroup.getDisplayName(), 10, 20);
            g2d.drawString("当前关卡: " + (stageGroup.getCurrentStage() != null ? stageGroup.getCurrentStage().getStageName() : "无"), 10, 40);
        }
    }
}