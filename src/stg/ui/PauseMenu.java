package stg.ui;

import java.awt.Color;
import java.awt.Font;

import stg.core.GameStateManager;
import stg.service.render.IRenderer;

/**
 * 暂停菜单 - 处理游戏暂停时的菜单显示和交互
 * @since 2026-02-17
 * @author JavaSTG Team
 */
public class PauseMenu {
    private static final String[] MENU_ITEMS = {
        "继续游戏",
        "重新开始",
        "返回主菜单"
    };
    
    private int selectedIndex = 0;
    private GameStateManager gameStateManager;
    private GameCanvas gameCanvas;
    
    /**
     * 构造函数
     * @param gameStateManager 游戏状态管理器
     */
    public PauseMenu(GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
    }
    
    /**
     * 构造函数
     * @param gameStateManager 游戏状态管理器
     * @param gameCanvas 游戏画布
     */
    public PauseMenu(GameStateManager gameStateManager, GameCanvas gameCanvas) {
        this.gameStateManager = gameStateManager;
        this.gameCanvas = gameCanvas;
    }
    
    /**
     * 绘制暂停菜单（使用IRenderer）
     * @param renderer OpenGL渲染器
     * @param width 窗口宽度
     * @param height 窗口高度
     */
    public void render(IRenderer renderer, int width, int height) {
        // 半透明遮罩
        renderer.setColor(new Color(0, 0, 0, 180));
        renderer.drawRect(0, 0, width, height, new Color(0, 0, 0, 180));
        
        // 菜单框
        int menuWidth = 300;
        int menuHeight = 250;
        int menuX = (width - menuWidth) / 2;
        int menuY = (height - menuHeight) / 2;
        
        renderer.setColor(new Color(30, 30, 50, 230));
        renderer.drawRect(menuX, menuY, menuWidth, menuHeight, new Color(30, 30, 50, 230));
        renderer.setColor(new Color(100, 100, 150));
        renderer.drawRect(menuX, menuY, menuWidth, menuHeight, new Color(100, 100, 150));
        
        // 标题
        Font font = new Font("Microsoft YaHei", Font.BOLD, 32);
        renderer.setFont(font);
        renderer.setColor(new Color(255, 200, 100));
        String title = "暂停";
        renderer.drawText(title, menuX + 50, menuY + 50, font, new Color(255, 200, 100));
        
        // 菜单项
        int menuItemHeight = 50;
        font = new Font("Microsoft YaHei", Font.BOLD, 24);
        renderer.setFont(font);
        
        for (int i = 0; i < MENU_ITEMS.length; i++) {
            int itemY = menuY + 80 + i * menuItemHeight;
            
            if (i == selectedIndex) {
                renderer.setColor(new Color(255, 200, 100));
                renderer.drawText("▶", menuX + 40, itemY + 8, font, new Color(255, 200, 100));
            }
            
            renderer.setColor(i == selectedIndex ? new Color(255, 200, 100) : new Color(220, 220, 220));
            renderer.drawText(MENU_ITEMS[i], menuX + 80, itemY + 8, font, i == selectedIndex ? new Color(255, 200, 100) : new Color(220, 220, 220));
        }
        
        // 操作提示
        font = new Font("Microsoft YaHei", Font.PLAIN, 14);
        renderer.setFont(font);
        renderer.setColor(new Color(150, 150, 150));
        renderer.drawText("↑↓ 选择", menuX + 40, menuY + menuHeight - 40, font, new Color(150, 150, 150));
        renderer.drawText("Z/Enter 确认", menuX + 40, menuY + menuHeight - 20, font, new Color(150, 150, 150));
    }
    
    /**
     * 处理向上按键
     */
    public void moveUp() {
        selectedIndex = (selectedIndex - 1 + MENU_ITEMS.length) % MENU_ITEMS.length;
    }
    
    /**
     * 处理向下按键
     */
    public void moveDown() {
        selectedIndex = (selectedIndex + 1) % MENU_ITEMS.length;
    }
    
    /**
     * 执行选中的菜单项
     */
    public void executeSelectedAction() {
        switch (selectedIndex) {
            case 0: // 继续游戏
                gameStateManager.togglePause();
                break;
            case 1: // 重新开始
                gameStateManager.reset();
                // 重置游戏世界
                if (gameCanvas != null) {
                    gameCanvas.resetGame();
                    // 重置关卡组
                    stg.stage.StageGroup stageGroup = gameCanvas.getStageGroup();
                    if (stageGroup != null) {
                        stageGroup.reset();
                        stageGroup.start();
                    }
                }
                break;
            case 2: // 返回主菜单
                Main.Main.returnToTitle();
                break;
        }
    }
    
    /**
     * 获取选中的索引
     */
    public int getSelectedIndex() {
        return selectedIndex;
    }
    
    /**
     * 设置选中的索引
     */
    public void setSelectedIndex(int index) {
        if (index >= 0 && index < MENU_ITEMS.length) {
            this.selectedIndex = index;
        }
    }
    
    /**
     * 获取菜单项数量
     */
    public int getMenuItemCount() {
        return MENU_ITEMS.length;
    }
}
