package stg.game.ui;

import java.awt.Component;
import stg.base.KeyStateProvider;
import user.player.PlayerType;
import stg.game.player.Player;
import stg.util.CoordinateSystem;

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
     * 获取画布宽度
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * 获取画布高度
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * 设置画布尺寸
     */
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
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
    
    /**
     * 构造函数
     */
    public GameCanvas() {
        this.coordinateSystem = new CoordinateSystem(width, height);
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
    public void setPlayer(PlayerType playerType, float x, float y) {
        // 这里只是一个占位实现，实际应该创建对应的玩家对象
        this.player = new Player(x, y);
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
    public void setStageGroup(Object stageGroup) {
        // 这里只是一个占位实现，实际应该设置关卡组
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
        // 这里只是一个占位实现，实际应该更新游戏状态
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
    public Object getWorld() {
        // 这里只是一个占位实现，实际应该返回游戏世界对象
        return null;
    }
}