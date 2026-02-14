package stg.game;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Font;
import java.awt.Color;
import stg.game.player.Player;
import stg.game.enemy.Boss;
import stg.util.CoordinateSystem;

/**
 * 游戏渲染器 - 处理游戏的渲染逻辑
 */
public class GameRenderer {
    private GameWorld world;
    private Player player;
    private CoordinateSystem coordinateSystem;
    
    /**
     * 构造函数
     */
    public GameRenderer(GameWorld world, Player player, CoordinateSystem coordinateSystem) {
        this.world = world;
        this.player = player;
        this.coordinateSystem = coordinateSystem;
    }
    
    /**
     * 渲染游戏画面
     */
    public void render(Graphics2D g, int canvasWidth, int canvasHeight) {
        enableAntiAliasing(g);
        
        renderPlayerBullets(g);
        renderEnemies(g);
        renderItems(g);
        renderPlayer(g);
        renderSpellcardInfo(g, canvasWidth, canvasHeight);
    }
    
    /**
     * 渲染符卡信息
     */
    private void renderSpellcardInfo(Graphics2D g, int canvasWidth, int canvasHeight) {
        // 查找当前活跃的Boss
        Boss currentBoss = findCurrentBoss();
        if (currentBoss != null) {
            // 获取当前符卡
            var spellcard = currentBoss.getCurrentSpellcard();
            if (spellcard != null && spellcard.isActive() && spellcard.isSpellcardPhase()) {
                // 渲染符卡名称在右上角
                g.setFont(new Font("Monospace", Font.BOLD, 20));
                g.setColor(Color.WHITE);
                String spellcardName = spellcard.getName();
                int stringWidth = g.getFontMetrics().stringWidth(spellcardName);
                int x = canvasWidth - stringWidth - 20;
                int y = 40;
                
                // 绘制背景矩形增强可读性
                g.setColor(new Color(0, 0, 0, 150));
                g.fillRect(x - 10, y - 25, stringWidth + 20, 30);
                
                // 绘制符卡名称
                g.setColor(Color.WHITE);
                g.drawString(spellcardName, x, y);
            }
        }
    }
    
    /**
     * 查找当前活跃的Boss
     */
    private Boss findCurrentBoss() {
        for (var enemy : world.getEnemies()) {
            if (enemy instanceof Boss && enemy.isActive()) {
                return (Boss) enemy;
            }
        }
        return null;
    }
    
    /**
     * 兼容旧版本的render方法
     */
    public void render(Graphics2D g) {
        render(g, 800, 600); // 默认画布尺寸
    }
    
    /**
     * 启用抗锯齿
     */
    private void enableAntiAliasing(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }
    
    /**
     * 渲染敌人
     */
    private void renderEnemies(Graphics2D g) {
        for (stg.game.enemy.Enemy enemy : world.getEnemies()) {
            enemy.render(g);
        }
    }
    
    /**
     * 渲染物品
     */
    private void renderItems(Graphics2D g) {
        for (stg.game.item.Item item : world.getItems()) {
            item.render(g);
        }
    }
    
    /**
     * 渲染玩家子弹
     */
    private void renderPlayerBullets(Graphics2D g) {
        for (stg.game.bullet.Bullet bullet : world.getPlayerBullets()) {
            bullet.render(g);
        }
    }
    
    /**
     * 渲染玩家
     */
    private void renderPlayer(Graphics2D g) {
        if (player != null) {
            player.render(g);
        }
    }
    
    /**
     * 设置游戏世界
     */
    public void setWorld(GameWorld world) {
        this.world = world;
    }
    
    /**
     * 设置玩家
     */
    public void setPlayer(stg.game.player.Player player) {
        this.player = player;
    }
    
    /**
     * 设置坐标系统
     */
    public void setCoordinateSystem(CoordinateSystem coordinateSystem) {
        this.coordinateSystem = coordinateSystem;
    }
}
