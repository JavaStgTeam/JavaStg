package stg.game;

import java.awt.event.KeyEvent;
import stg.base.KeyStateProvider;
import stg.game.player.Player;

/**
 * 输入处理�?- 处理键盘输入
 */
public class InputHandler implements KeyStateProvider {
    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean zPressed = false;
    private boolean xPressed = false;
    private boolean shiftPressed = false;
    
    private Player player;
    private GameStateManager gameStateManager;
    
    /**
     * 构造函�?     */
    public InputHandler(Player player, GameStateManager gameStateManager) {
        this.player = player;
        this.gameStateManager = gameStateManager;
    }
    
    /**
     * 处理按键按下事件
     */
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                upPressed = true;
                updatePlayerMovement();
                break;
            case KeyEvent.VK_DOWN:
                downPressed = true;
                updatePlayerMovement();
                break;
            case KeyEvent.VK_LEFT:
                leftPressed = true;
                updatePlayerMovement();
                break;
            case KeyEvent.VK_RIGHT:
                rightPressed = true;
                updatePlayerMovement();
                break;
            case KeyEvent.VK_Z: // Z�?- 射击
                zPressed = true;
                if (player != null) {
                    player.setShooting(true);
                }
                break;
            case KeyEvent.VK_SHIFT: // Shift�?- 低速模�?                shiftPressed = true;
                if (player != null) {
                    player.setSlowMode(true);
                }
                break;
            case KeyEvent.VK_X: // X�?                xPressed = true;
                break;
        }
    }
    
    /**
     * 处理按键释放事件
     */
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                upPressed = false;
                updatePlayerMovement();
                break;
            case KeyEvent.VK_DOWN:
                downPressed = false;
                updatePlayerMovement();
                break;
            case KeyEvent.VK_LEFT:
                leftPressed = false;
                updatePlayerMovement();
                break;
            case KeyEvent.VK_RIGHT:
                rightPressed = false;
                updatePlayerMovement();
                break;
            case KeyEvent.VK_Z: // Z�?- 射击
                zPressed = false;
                if (player != null) {
                    player.setShooting(false);
                }
                break;
            case KeyEvent.VK_SHIFT: // Shift�?- 低速模�?                shiftPressed = false;
                if (player != null) {
                    player.setSlowMode(false);
                }
                break;
            case KeyEvent.VK_X: // X�?                xPressed = false;
                break;
        }
    }
    
    /**
     * 根据当前按键状态更新玩家移动方�?     */
    private void updatePlayerMovement() {
        if (player == null) return;
        
        // 水平方向:同时按下左右键时保持静止
        if (leftPressed && rightPressed) {
            player.stopHorizontal();
        } else if (leftPressed) {
            player.moveLeft();
        } else if (rightPressed) {
            player.moveRight();
        } else {
            player.stopHorizontal();
        }
        
        // 垂直方向:同时按下上下键时保持静止
        if (upPressed && downPressed) {
            player.stopVertical();
        } else if (upPressed) {
            player.moveUp();
        } else if (downPressed) {
            player.moveDown();
        } else {
            player.stopVertical();
        }
    }
    
    @Override
    public boolean isUpPressed() { return upPressed; }
    @Override
    public boolean isDownPressed() { return downPressed; }
    @Override
    public boolean isLeftPressed() { return leftPressed; }
    @Override
    public boolean isRightPressed() { return rightPressed; }
    @Override
    public boolean isZPressed() { return zPressed; }
    @Override
    public boolean isShiftPressed() { return shiftPressed; }
    @Override
    public boolean isXPressed() { return xPressed; }
    
    /**
     * 设置玩家
     */
    public void setPlayer(Player player) {
        this.player = player;
    }
    
    /**
     * 获取玩家
     */
    public Player getPlayer() {
        return player;
    }
}
