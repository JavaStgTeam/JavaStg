package stg.ui;

import java.awt.Component;
import java.util.concurrent.atomic.AtomicBoolean;
import stg.base.KeyStateProvider;
import stg.entity.player.Player;
import stg.util.CoordinateSystem;

/**
 * 游戏画布类 - 负责游戏的渲染和输入处理
 * @since 2026-01-20
 */
@SuppressWarnings("FieldMayBeFinal")
public class GameCanvas extends Component implements KeyStateProvider {
    // 按键状态
    private AtomicBoolean upPressed = new AtomicBoolean(false);
    private AtomicBoolean downPressed = new AtomicBoolean(false);
    private AtomicBoolean leftPressed = new AtomicBoolean(false);
    private AtomicBoolean rightPressed = new AtomicBoolean(false);
    private AtomicBoolean zPressed = new AtomicBoolean(false);
    private AtomicBoolean shiftPressed = new AtomicBoolean(false);
    private AtomicBoolean xPressed = new AtomicBoolean(false);
    
    // 画布尺寸
    private int width = 800;
    private int height = 600;
    
    /**
     * 设置画布尺寸
     */
    @Override
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        super.setSize(width, height);
    }
    
    /**
     * 设置按键状态
     */
    public void setUpPressed(boolean upPressed) {
        this.upPressed.set(upPressed);
    }

    public void setDownPressed(boolean downPressed) {
        this.downPressed.set(downPressed);
    }

    public void setLeftPressed(boolean leftPressed) {
        this.leftPressed.set(leftPressed);
    }

    public void setRightPressed(boolean rightPressed) {
        this.rightPressed.set(rightPressed);
    }

    public void setZPressed(boolean zPressed) {
        this.zPressed.set(zPressed);
    }

    public void setShiftPressed(boolean shiftPressed) {
        this.shiftPressed.set(shiftPressed);
    }

    public void setXPressed(boolean xPressed) {
        this.xPressed.set(xPressed);
    }

    // 实现KeyStateProvider接口
    @Override
    public boolean isUpPressed() {
        return upPressed.get();
    }

    @Override
    public boolean isDownPressed() {
        return downPressed.get();
    }

    @Override
    public boolean isLeftPressed() {
        return leftPressed.get();
    }

    @Override
    public boolean isRightPressed() {
        return rightPressed.get();
    }

    @Override
    public boolean isZPressed() {
        return zPressed.get();
    }

    @Override
    public boolean isShiftPressed() {
        return shiftPressed.get();
    }

    @Override
    public boolean isXPressed() {
        return xPressed.get();
    }
    
    @SuppressWarnings("unused")
    private GameStatusPanel gameStatusPanel;
    
    // 玩家
    private Player player;
    
    // 坐标系统
    private CoordinateSystem coordinateSystem;
    
    // 游戏渲染器
    private stg.renderer.GameRenderer gameRenderer;
    
    // 游戏世界
    private stg.core.GameWorld gameWorld;
    
    // 碰撞检测系统
    private stg.core.CollisionSystem collisionSystem;
    
    // 关卡组
    private stg.stage.StageGroup stageGroup;
    
    // 游戏状态管理器
    private stg.core.GameStateManager gameStateManager;
    
    // 暂停菜单
    private PauseMenu pauseMenu;
    
    /**
     * 构造函数
     */
    public GameCanvas() {
        this.coordinateSystem = new CoordinateSystem(width, height);
        
        // 设置共享坐标系统，确保所有游戏对象使用一致的坐标转换
        stg.entity.base.Obj.setSharedCoordinateSystem(coordinateSystem);
        
        // 初始化游戏世界
        this.gameWorld = new stg.core.GameWorld();
        
        // 初始化碰撞检测系统
        this.collisionSystem = new stg.core.CollisionSystem(gameWorld, null);
        
        // 初始化游戏渲染器
        this.gameRenderer = new stg.renderer.GameRenderer(gameWorld, null);
        
        // 初始化游戏状态管理器
        this.gameStateManager = new stg.core.GameStateManager();
        
        // 初始化暂停菜单
        this.pauseMenu = new PauseMenu(gameStateManager, this);
        
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
        // 检查是否处于暂停状态
        if (gameStateManager != null && gameStateManager.getState() == stg.core.GameStateManager.State.PAUSED && pauseMenu != null) {
            // 处理暂停菜单的按键输入
            switch (e.getKeyCode()) {
                case java.awt.event.KeyEvent.VK_UP -> pauseMenu.moveUp();
                case java.awt.event.KeyEvent.VK_DOWN -> pauseMenu.moveDown();
                case java.awt.event.KeyEvent.VK_Z, java.awt.event.KeyEvent.VK_ENTER -> pauseMenu.executeSelectedAction();
                case java.awt.event.KeyEvent.VK_ESCAPE -> gameStateManager.togglePause();
            }
            // 重绘以更新暂停菜单
            repaint();
        } else {
            // 正常游戏状态的按键处理
            switch (e.getKeyCode()) {
                case java.awt.event.KeyEvent.VK_UP -> setUpPressed(true);
                case java.awt.event.KeyEvent.VK_DOWN -> setDownPressed(true);
                case java.awt.event.KeyEvent.VK_LEFT -> setLeftPressed(true);
                case java.awt.event.KeyEvent.VK_RIGHT -> setRightPressed(true);
                case java.awt.event.KeyEvent.VK_Z -> setZPressed(true);
                case java.awt.event.KeyEvent.VK_SHIFT -> setShiftPressed(true);
                case java.awt.event.KeyEvent.VK_X -> setXPressed(true);
                case java.awt.event.KeyEvent.VK_ESCAPE -> gameStateManager.togglePause();
            }
        }
    }
    
    private void handleKeyRelease(java.awt.event.KeyEvent e) {
        // 检查是否处于暂停状态，如果是则不处理释放事件
        if (gameStateManager == null || gameStateManager.getState() != stg.core.GameStateManager.State.PAUSED) {
            switch (e.getKeyCode()) {
                case java.awt.event.KeyEvent.VK_UP -> setUpPressed(false);
                case java.awt.event.KeyEvent.VK_DOWN -> setDownPressed(false);
                case java.awt.event.KeyEvent.VK_LEFT -> setLeftPressed(false);
                case java.awt.event.KeyEvent.VK_RIGHT -> setRightPressed(false);
                case java.awt.event.KeyEvent.VK_Z -> setZPressed(false);
                case java.awt.event.KeyEvent.VK_SHIFT -> setShiftPressed(false);
                case java.awt.event.KeyEvent.VK_X -> setXPressed(false);
            }
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
        
        // 设置游戏世界引用，用于发射子弹
        this.player.setGameWorld(gameWorld);
        
        // 更新游戏渲染器中的玩家引用
        if (gameRenderer != null) {
            gameRenderer.setPlayer(player);
        }
        
        // 更新碰撞检测系统中的玩家引用
        if (collisionSystem != null) {
            collisionSystem.setPlayer(player);
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
        // 清理GameWorld中的所有对象
        if (gameWorld != null) {
            gameWorld.clear();
        }
        
        // 重置游戏状态管理器
        if (gameStateManager != null) {
            gameStateManager.reset();
        }
        
        // 重置暂停菜单
        if (pauseMenu != null) {
            pauseMenu.setSelectedIndex(0);
        }
        
        // 重置玩家状态
        if (player != null) {
            player.reset();
        }
    }
    
    /**
     * 设置关卡组
     */
    public void setStageGroup(stg.stage.StageGroup stageGroup) {
        this.stageGroup = stageGroup;
    }
    
    /**
     * 获取关卡组
     */
    public stg.stage.StageGroup getStageGroup() {
        return stageGroup;
    }
    
    /**
     * 请求焦点
     */
    @Override
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
        
        // 检查游戏状态
        if (gameStateManager != null && gameStateManager.getState() == stg.core.GameStateManager.State.PLAYING) {
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
            
            // 执行碰撞检测
            if (collisionSystem != null) {
                collisionSystem.checkCollisions();
            }
            
            // 更新关卡组状态
            if (stageGroup != null) {
                stageGroup.update();
            }
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
    public stg.core.GameWorld getWorld() {
        return gameWorld;
    }
    
    /**
     * 获取当前游戏对象的数量
     * @return 游戏对象数量
     */
    public int getObjectCount() {
        int count = 0;
        if (gameWorld != null) {
            // 计算敌人数量
            count += gameWorld.getEnemies().size();
            // 计算玩家子弹数量
            count += gameWorld.getPlayerBullets().size();
            // 计算敌人子弹数量
            count += gameWorld.getEnemyBullets().size();
            // 计算物品数量
            count += gameWorld.getItems().size();
        }
        // 加上玩家本身
        if (player != null) {
            count += 1;
        }
        return count;
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
        
        // 更新坐标系统的屏幕尺寸
        if (coordinateSystem != null) {
            coordinateSystem.updateScreenSize(actualWidth, actualHeight);
        }
        
        // 绘制背景
        g2d.setColor(java.awt.Color.BLACK);
        g2d.fillRect(0, 0, actualWidth, actualHeight);
        
        // 使用游戏渲染器渲染所有游戏对象
        if (gameRenderer != null) {
            gameRenderer.render(g2d, actualWidth, actualHeight);
        }
        
        // 绘制关卡组信息
        if (stageGroup != null) {
            g2d.setColor(java.awt.Color.WHITE);
            g2d.setFont(new java.awt.Font("Monospace", java.awt.Font.PLAIN, 12));
            g2d.drawString("关卡组: " + stageGroup.getDisplayName(), 10, 20);
            g2d.drawString("当前关卡: " + (stageGroup.getCurrentStage() != null ? stageGroup.getCurrentStage().getStageName() : "无"), 10, 40);
        }
        
        // 检查游戏状态，如果是暂停状态则绘制暂停菜单
        if (gameStateManager != null && gameStateManager.getState() == stg.core.GameStateManager.State.PAUSED && pauseMenu != null) {
            pauseMenu.render(g2d, actualWidth, actualHeight);
        }
    }
}