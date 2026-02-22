package stg.ui;

import java.util.concurrent.atomic.AtomicBoolean;

import org.lwjgl.glfw.GLFW;

import stg.base.KeyStateProvider;
import stg.entity.player.Player;
import stg.service.render.GLRenderer;
import stg.service.render.IRenderer;
import stg.util.CoordinateSystem;
import stg.util.objectpool.ObjectPoolManager;

/**
 * 游戏画布类 - 负责游戏的渲染和输入处理
 * @since 2026-01-20
 */
@SuppressWarnings("FieldMayBeFinal")
public class GameCanvas implements KeyStateProvider {
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
    private long window;
    
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
    
    // 渲染器
    private IRenderer renderer;
    private GLRenderer glRenderer;
    
    // 渲染管理器
    private stg.render.RenderManager renderManager;
    
    // 相机
    private stg.render.Camera camera;
    
    /**
     * 构造函数
     * @param window GLFW窗口句柄
     */
    public GameCanvas(long window) {
        this.window = window;
        this.coordinateSystem = new CoordinateSystem(width, height);
        
        // 设置共享坐标系统，确保所有游戏对象使用一致的坐标转换
        stg.entity.base.Obj.setSharedCoordinateSystem(coordinateSystem);
        
        // 初始化游戏世界
        this.gameWorld = new stg.core.GameWorld();
        
        // 初始化碰撞检测系统
        this.collisionSystem = new stg.core.CollisionSystem(gameWorld, null);
        
        // 初始化游戏渲染器
        this.gameRenderer = new stg.renderer.GameRenderer(gameWorld, null);
        
        // 初始化渲染器（自动切换）
        initRenderer();
        
        // 初始化游戏状态管理器
        this.gameStateManager = new stg.core.GameStateManager();
        
        // 初始化暂停菜单
        this.pauseMenu = new PauseMenu(gameStateManager, this);
        
        // 初始化渲染管理器
        this.renderManager = new stg.render.RenderManager();
        
        // 初始化相机
        this.camera = new stg.render.Camera(width, height);
        
        // 初始化对象池
        stg.entity.base.Obj.initializeObjectPools();
    }
    
    /**
     * 初始化渲染器
     */
    public void initRenderer() {
        // 初始化OpenGL渲染器
        try {
            // 创建GLRenderer实例
            glRenderer = new GLRenderer(window);
            glRenderer.init();
            renderer = glRenderer;
            System.out.println("使用OpenGL渲染器");
        } catch (Exception e) {
            System.out.println("OpenGL初始化失败: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * 获取当前渲染器
     */
    public IRenderer getRenderer() {
        return renderer;
    }
    
    /**
     * 检查是否使用OpenGL渲染器
     */
    public boolean isUsingOpenGL() {
        return glRenderer != null && renderer != null && renderer.getClass().getName().equals("stg.service.render.GLRenderer");
    }
    
    /**
     * 处理GLFW键盘事件
     */
    public void handleKeyEvent(int key, int action) {
        // 检查是否需要处理标题页面或关卡组选择页面的按键事件
        if (Main.Main.showTitleScreen) {
            // 处理标题页面的按键输入
            if (action == GLFW.GLFW_PRESS) {
                if (Main.Main.titleScreen != null) {
                    Main.Main.titleScreen.handleKeyPress(key);
                }
            }
        } else if (Main.Main.showStageGroupSelect) {
            // 处理关卡组选择页面的按键输入
            if (action == GLFW.GLFW_PRESS) {
                if (Main.Main.stageGroupSelectPanel != null) {
                    Main.Main.stageGroupSelectPanel.handleKeyPress(key);
                }
            }
        } else {
            // 检查是否处于暂停状态
            if (gameStateManager != null && gameStateManager.getState() == stg.core.GameStateManager.State.PAUSED && pauseMenu != null) {
                // 处理暂停菜单的按键输入
                if (action == GLFW.GLFW_PRESS) {
                    switch (key) {
                        case GLFW.GLFW_KEY_UP -> pauseMenu.moveUp();
                        case GLFW.GLFW_KEY_DOWN -> pauseMenu.moveDown();
                        case GLFW.GLFW_KEY_Z, GLFW.GLFW_KEY_ENTER -> pauseMenu.executeSelectedAction();
                        case GLFW.GLFW_KEY_ESCAPE -> gameStateManager.togglePause();
                    }
                }
            } else {
                // 正常游戏状态的按键处理
                if (action == GLFW.GLFW_PRESS) {
                    switch (key) {
                        case GLFW.GLFW_KEY_UP -> setUpPressed(true);
                        case GLFW.GLFW_KEY_DOWN -> setDownPressed(true);
                        case GLFW.GLFW_KEY_LEFT -> setLeftPressed(true);
                        case GLFW.GLFW_KEY_RIGHT -> setRightPressed(true);
                        case GLFW.GLFW_KEY_Z -> setZPressed(true);
                        case GLFW.GLFW_KEY_LEFT_SHIFT, GLFW.GLFW_KEY_RIGHT_SHIFT -> setShiftPressed(true);
                        case GLFW.GLFW_KEY_X -> setXPressed(true);
                        case GLFW.GLFW_KEY_ESCAPE -> gameStateManager.togglePause();
                    }
                } else if (action == GLFW.GLFW_RELEASE) {
                    switch (key) {
                        case GLFW.GLFW_KEY_UP -> setUpPressed(false);
                        case GLFW.GLFW_KEY_DOWN -> setDownPressed(false);
                        case GLFW.GLFW_KEY_LEFT -> setLeftPressed(false);
                        case GLFW.GLFW_KEY_RIGHT -> setRightPressed(false);
                        case GLFW.GLFW_KEY_Z -> setZPressed(false);
                        case GLFW.GLFW_KEY_LEFT_SHIFT, GLFW.GLFW_KEY_RIGHT_SHIFT -> setShiftPressed(false);
                        case GLFW.GLFW_KEY_X -> setXPressed(false);
                    }
                }
            }
        }
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
     * 更新游戏
     */
    public void update() {
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
                gameWorld.update(width, height);
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
     * 渲染游戏
     */
    public void render() {
        // 更新坐标系统的屏幕尺寸
        if (coordinateSystem != null) {
            coordinateSystem.updateScreenSize(width, height);
        }
        
        // 更新相机的屏幕尺寸
        if (camera != null) {
            camera.updateScreenSize(width, height);
        }
        
        // 处理渲染器
        if (renderer != null) {
            // 使用OpenGL渲染器
            renderer.beginFrame();
            
            // 清空渲染管理器并重新添加所有可渲染对象
            if (renderManager != null) {
                renderManager.clearAll();
                
                // 添加游戏世界中的所有实体
                if (gameWorld != null) {
                    // 添加敌人
                    for (stg.entity.enemy.Enemy enemy : gameWorld.getEnemies()) {
                        if (enemy != null && enemy.isActive()) {
                            renderManager.addRenderable(enemy);
                        }
                    }
                    
                    // 添加物品
                    for (stg.entity.item.Item item : gameWorld.getItems()) {
                        if (item != null && item.isActive()) {
                            renderManager.addRenderable(item);
                        }
                    }
                    
                    // 添加子弹
                    for (stg.entity.bullet.Bullet bullet : gameWorld.getPlayerBullets()) {
                        if (bullet != null && bullet.isActive()) {
                            renderManager.addRenderable(bullet);
                        }
                    }
                    for (stg.entity.bullet.Bullet bullet : gameWorld.getEnemyBullets()) {
                        if (bullet != null && bullet.isActive()) {
                            renderManager.addRenderable(bullet);
                        }
                    }
                }
                
                // 添加玩家（确保玩家总是被渲染）
                if (player != null && player.isActive()) {
                    renderManager.addRenderable(player);
                }
                
                // 渲染所有对象
                renderManager.renderAll(renderer);
            } else {
                // 降级到旧的渲染方式
                if (gameRenderer != null) {
                    gameRenderer.render(renderer, width, height);
                }
                
                if (player != null && player.isActive()) {
                    player.render(renderer);
                }
            }
            
            // 渲染暂停菜单
            if (gameStateManager != null && gameStateManager.getState() == stg.core.GameStateManager.State.PAUSED && pauseMenu != null) {
                pauseMenu.render(renderer, width, height);
            }
            
            renderer.endFrame();
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
        // 从对象池管理器获取所有对象池中的对象总数
        int poolCount = ObjectPoolManager.getInstance().getTotalObjectCount();
        
        // 加上玩家本身
        int total = poolCount;
        if (player != null) {
            total += 1;
        }
        
        return total;
    }
    
    /**
     * 获取宽度
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * 获取高度
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * 获取游戏渲染器
     */
    public stg.renderer.GameRenderer getGameRenderer() {
        return gameRenderer;
    }
    
    /**
     * 获取游戏状态管理器
     */
    public stg.core.GameStateManager getGameStateManager() {
        return gameStateManager;
    }
    
    /**
     * 获取暂停菜单
     */
    public PauseMenu getPauseMenu() {
        return pauseMenu;
    }
    
    /**
     * 清理资源
     */
    public void cleanup() {
        // 清理渲染器
        if (renderer != null) {
            renderer.cleanup();
            renderer = null;
            glRenderer = null;
        }
        
        // 清理游戏世界
        if (gameWorld != null) {
            gameWorld.clear();
            gameWorld = null;
        }
        
        // 清理碰撞检测系统
        collisionSystem = null;
        
        // 清理游戏渲染器
        gameRenderer = null;
        
        // 清理暂停菜单
        pauseMenu = null;
        
        // 清理游戏状态管理器
        gameStateManager = null;
        
        // 清理玩家
        player = null;
        
        // 清理坐标系统
        coordinateSystem = null;
        
        System.out.println("GameCanvas资源清理完成");
    }
}