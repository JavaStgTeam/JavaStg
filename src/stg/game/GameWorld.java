package stg.game;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import stg.game.bullet.Bullet;
import stg.game.enemy.Enemy;
import stg.game.item.Item;

/**
 * 游戏世界 - 管理游戏中的所有实体
 */
public class GameWorld {
    private final List<Enemy> enemies = new CopyOnWriteArrayList<>();
    private final List<Bullet> playerBullets = new CopyOnWriteArrayList<>();
    private final List<Bullet> enemyBullets = new CopyOnWriteArrayList<>();
    private final List<Item> items = new CopyOnWriteArrayList<>();
    
    /**
     * 添加敌人
     */
    public void addEnemy(Enemy enemy) {
        if (enemy != null) {
            enemies.add(enemy);
        }
    }
    
    /**
     * 添加玩家子弹
     */
    public void addPlayerBullet(Bullet bullet) {
        if (bullet != null) {
            playerBullets.add(bullet);
        }
    }
    
    /**
     * 添加敌人子弹
     */
    public void addEnemyBullet(Bullet bullet) {
        if (bullet != null) {
            enemyBullets.add(bullet);
        }
    }
    
    /**
     * 添加物品
     */
    public void addItem(Item item) {
        if (item != null) {
            items.add(item);
        }
    }
    
    /**
     * 更新所有实体
     */
    public void update(int canvasWidth, int canvasHeight) {
        updateEnemies(canvasWidth, canvasHeight);
        updateBullets();
        updateItems();
    }
    

    
    /**
     * 更新敌人
     */
    private void updateEnemies(int canvasWidth, int canvasHeight) {
        for (int i = enemies.size() - 1; i >= 0; i--) {
            Enemy enemy = enemies.get(i);
            enemy.update(canvasWidth, canvasHeight);
            
            if (!enemy.isAlive() || enemy.isOutOfBounds(canvasWidth, canvasHeight)) {
                enemies.remove(i);
            }
        }
    }
    
    /**
     * 更新子弹
     */
    private void updateBullets() {
        // 更新玩家子弹
        for (int i = playerBullets.size() - 1; i >= 0; i--) {
            Bullet bullet = playerBullets.get(i);
            bullet.update();
            if (bullet.isOutOfBounds()) {
                playerBullets.remove(i);
            }
        }
        
        // 更新敌人子弹
        for (int i = enemyBullets.size() - 1; i >= 0; i--) {
            Bullet bullet = enemyBullets.get(i);
            bullet.update();
            if (bullet.isOutOfBounds()) {
                enemyBullets.remove(i);
            }
        }
    }
    
    /**
     * 更新物品
     */
    private void updateItems() {
        for (int i = items.size() - 1; i >= 0; i--) {
            Item item = items.get(i);
            item.update();
            if (!item.isActive() || item.isOutOfBounds()) {
                items.remove(i);
            }
        }
    }
    
    /**
     * 获取敌人列表
     */
    public List<Enemy> getEnemies() {
        return enemies;
    }
    
    /**
     * 获取玩家子弹列表
     */
    public List<Bullet> getPlayerBullets() {
        return playerBullets;
    }
    
    /**
     * 获取敌人子弹列表
     */
    public List<Bullet> getEnemyBullets() {
        return enemyBullets;
    }
    
    /**
     * 获取物品列表
     */
    public List<Item> getItems() {
        return items;
    }
    
    /**
     * 清除所有实体
     */
    public void clear() {
        enemies.clear();
        playerBullets.clear();
        enemyBullets.clear();
        items.clear();
    }
    
    /**
     * 清除所有物品
     */
    public void clearItems() {
        items.clear();
    }
    
    /**
     * 移除指定的物品
     */
    public void removeItem(Item item) {
        items.remove(item);
    }
}
