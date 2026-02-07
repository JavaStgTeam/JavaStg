package stg.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import stg.game.bullet.Bullet;
import stg.game.enemy.Enemy;
import stg.game.item.Item;

/**
 * 游戏世界�?- 管理游戏中的所有实�? */
public class GameWorld {
    private final List<Enemy> enemies = new ArrayList<>();
    private final List<Bullet> playerBullets = new ArrayList<>();
    private final List<Item> items = new ArrayList<>();
    
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
     * 添加物品
     */
    public void addItem(Item item) {
        if (item != null) {
            items.add(item);
        }
    }
    
    /**
     * 更新所有实�?     */
    public void update(int canvasWidth, int canvasHeight) {
        updateEnemies(canvasWidth, canvasHeight);
        updateBullets(canvasWidth, canvasHeight);
        updateItems(canvasWidth, canvasHeight);
    }
    
    /**
     * 更新敌人
     */
    private void updateEnemies(int canvasWidth, int canvasHeight) {
        Iterator<Enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            enemy.update();
            
            if (!enemy.isAlive() || enemy.isOutOfBounds(canvasWidth, canvasHeight)) {
                iterator.remove();
            }
        }
    }
    
    /**
     * 更新子弹
     */
    private void updateBullets(int canvasWidth, int canvasHeight) {
        // 更新玩家子弹
        Iterator<Bullet> playerBulletIterator = playerBullets.iterator();
        while (playerBulletIterator.hasNext()) {
            Bullet bullet = playerBulletIterator.next();
            bullet.update();
            if (bullet.isOutOfBounds(canvasWidth, canvasHeight)) {
                playerBulletIterator.remove();
            }
        }
    }
    
    /**
     * 更新物品
     */
    private void updateItems(int canvasWidth, int canvasHeight) {
        Iterator<Item> iterator = items.iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next();
            item.update();
            if (!item.isActive() || item.isOutOfBounds(canvasWidth, canvasHeight)) {
                iterator.remove();
            }
        }
    }
    
    /**
     * 获取敌人列表（只读）
     */
    public List<Enemy> getEnemies() {
        return Collections.unmodifiableList(enemies);
    }
    
    /**
     * 获取玩家子弹列表（只读）
     */
    public List<Bullet> getPlayerBullets() {
        return Collections.unmodifiableList(playerBullets);
    }
    
    /**
     * 获取物品列表（只读）
     */
    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }
    
    /**
     * 清除所有实�?     */
    public void clear() {
        enemies.clear();
        playerBullets.clear();
        items.clear();
    }
    
    /**
     * 清除所有物�?     */
    public void clearItems() {
        items.clear();
    }
    
    /**
     * 移除指定的物�?     */
    public void removeItem(Item item) {
        items.remove(item);
    }
}
