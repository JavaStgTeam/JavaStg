package stg.renderer;

import java.awt.Color;
import java.awt.Font;
import stg.core.GameWorld;
import stg.entity.enemy.Boss;
import stg.entity.enemy.EnemySpellcard;
import stg.entity.player.Player;
import stg.service.render.IRenderer;

/**
 * 游戏渲染器 - 处理游戏的渲染逻辑
 * 
 * @author JavaSTG Team
 * @since 2026-02-17
 */
public class GameRenderer {
    private GameWorld world;
    private Player player;
    
    /**
     * 构造函数
     */
    public GameRenderer(GameWorld world, Player player) {
        this.world = world;
        this.player = player;
    }
    
    /**
     * 渲染游戏画面
     */
    public void render(IRenderer renderer, int canvasWidth, int canvasHeight) {
        renderPlayerBullets(renderer);
        renderEnemyBullets(renderer);
        renderEnemies(renderer);
        renderItems(renderer);
        renderPlayer(renderer);
        renderSpellcardInfo(renderer, canvasWidth, canvasHeight);
    }
    
    /**
     * 渲染符卡信息
     */
    private void renderSpellcardInfo(IRenderer renderer, int canvasWidth, int canvasHeight) {
        // 查找当前活跃的Boss
        Boss currentBoss = findCurrentBoss();
        if (currentBoss != null) {
            // 获取当前符卡
            var spellcard = currentBoss.getCurrentSpellcard();
            if (spellcard != null && spellcard.isActive()) {
                // 渲染符卡名称在右上角
                if (spellcard.isSpellcardPhase()) {
                    Font font = new Font("Monospace", Font.BOLD, 20);
                    renderer.setFont(font);
                    
                    String spellcardName = spellcard.getName();
                    // 简化处理，假设文本宽度
                    int stringWidth = 200;
                    int x = canvasWidth - stringWidth - 20;
                    int y = 40;
                    
                    // 绘制背景矩形增强可读性
                    renderer.setColor(new Color(0, 0, 0, 150));
                    renderer.drawRect(x - 10, y - 25, stringWidth + 20, 30, new Color(0, 0, 0, 150));
                    
                    // 绘制符卡名称
                    renderer.setColor(Color.WHITE);
                    renderer.drawText(spellcardName, x, y, font, Color.WHITE);
                }
                
                // 渲染倒计时在屏幕上方中央
                renderPhaseTimer(renderer, canvasWidth, canvasHeight, spellcard);
            }
        }
    }
    
    /**
     * 渲染阶段倒计时
     */
    private void renderPhaseTimer(IRenderer renderer, int canvasWidth, int canvasHeight, EnemySpellcard spellcard) {
        // 所有阶段都显示倒计时，只要有持续时间
        if (spellcard.getDuration() > 0) {
            int totalFrames = spellcard.getDuration();
            int currentFrame = spellcard.getCurrentFrame();
            int remainingFrames = Math.max(0, totalFrames - currentFrame);
            
            // 转换为秒数，保留一位小数
            double remainingSeconds = remainingFrames / 60.0;
            
            // 应用显示规则：超过100秒显示99.9，否则显示实际秒数
            String timeString;
            if (remainingSeconds > 100) {
                timeString = "99.9";
            } else {
                timeString = String.format("%.1f", remainingSeconds);
            }
            
            // 渲染倒计时在屏幕上方中央
            Font font = new Font("Monospace", Font.BOLD, 24);
            renderer.setFont(font);
            
            // 简化处理，假设文本宽度
            int stringWidth = 100;
            int x = (canvasWidth - stringWidth) / 2;
            int y = 60;
            
            // 绘制背景矩形增强可读性
            renderer.setColor(new Color(0, 0, 0, 150));
            renderer.drawRect(x - 15, y - 30, stringWidth + 30, 40, new Color(0, 0, 0, 150));
            
            // 绘制倒计时
            renderer.setColor(Color.WHITE);
            renderer.drawText(timeString, x, y, font, Color.WHITE);
        }
    }
    
    /**
     * 查找当前活跃的Boss
     */
    private Boss findCurrentBoss() {
        if (world != null) {
            for (var enemy : world.getEnemies()) {
                if (enemy instanceof Boss && enemy.isActive()) {
                    return (Boss) enemy;
                }
            }
        }
        return null;
    }
    
    /**
     * 渲染敌人
     */
    private void renderEnemies(IRenderer renderer) {
        if (world != null) {
            for (stg.entity.enemy.Enemy enemy : world.getEnemies()) {
                if (enemy != null && enemy.isActive()) {
                    enemy.render(renderer);
                }
            }
        }
    }
    
    /**
     * 渲染物品
     */
    private void renderItems(IRenderer renderer) {
        if (world != null) {
            for (stg.entity.item.Item item : world.getItems()) {
                if (item != null && item.isActive()) {
                    item.render(renderer);
                }
            }
        }
    }
    
    /**
     * 渲染玩家子弹
     */
    private void renderPlayerBullets(IRenderer renderer) {
        if (world != null) {
            for (stg.entity.bullet.Bullet bullet : world.getPlayerBullets()) {
                if (bullet != null && bullet.isActive()) {
                    bullet.render(renderer);
                }
            }
        }
    }
    
    /**
     * 渲染敌人子弹
     */
    private void renderEnemyBullets(IRenderer renderer) {
        if (world != null) {
            for (stg.entity.bullet.Bullet bullet : world.getEnemyBullets()) {
                if (bullet != null && bullet.isActive()) {
                    bullet.render(renderer);
                }
            }
        }
    }
    
    /**
     * 渲染玩家
     */
    private void renderPlayer(IRenderer renderer) {
        if (player != null && player.isActive()) {
            player.render(renderer);
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
    public void setPlayer(stg.entity.player.Player player) {
        this.player = player;
    }
}
