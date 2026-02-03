package stg.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.FontMetrics;
import stg.game.GameStateManager;

/**
 * 暂停菜单 - 处理游戏暂停时的菜单显示和交互 */
public class PauseMenu {
    private static final String[] MENU_ITEMS = {
        "继续游戏",
        "重新开始",
        "返回主菜单"
    };
    
    private int selectedIndex = 0;
    private GameStateManager gameStateManager;
    
    /**
     * 构造函数
     */
    public PauseMenu(GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
    }
    
    /**
     * 绘制暂停菜单
     */
    public void render(Graphics2D g, int width, int height) {
        // 半透明遮罩
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, width, height);
        
        // 菜单框
        int menuWidth = 300;
        int menuHeight = 250;
        int menuX = (width - menuWidth) / 2;
        int menuY = (height - menuHeight) / 2;
        
        g.setColor(new Color(30, 30, 50, 230));
        g.fillRoundRect(menuX, menuY, menuWidth, menuHeight, 20, 20);
        g.setColor(new Color(100, 100, 150));
        g.drawRoundRect(menuX, menuY, menuWidth, menuHeight, 20, 20);
        
        // 标题
        g.setFont(new Font("Microsoft YaHei", Font.BOLD, 32));
        g.setColor(new Color(255, 200, 100));
        String title = "暂停";
        FontMetrics fm = g.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        g.drawString(title, menuX + (menuWidth - titleWidth) / 2, menuY + 50);
        
        // 菜单项
        int menuItemHeight = 50;
        g.setFont(new Font("Microsoft YaHei", Font.BOLD, 24));
        
        for (int i = 0; i < MENU_ITEMS.length; i++) {
            int itemY = menuY + 80 + i * menuItemHeight;
            
            if (i == selectedIndex) {
                g.setColor(new Color(255, 200, 100));
                g.drawString("▶", menuX + 40, itemY + 8);
            }
            
            g.setColor(i == selectedIndex ? new Color(255, 200, 100) : new Color(220, 220, 220));
            g.drawString(MENU_ITEMS[i], menuX + 80, itemY + 8);
        }
        
        // 操作提示
        g.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        g.setColor(new Color(150, 150, 150));
        g.drawString("↑↓ 选择", menuX + 40, menuY + menuHeight - 40);
        g.drawString("Z/Enter 确认", menuX + 40, menuY + menuHeight - 20);
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
                // TODO: 需要重置游戏世界
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
