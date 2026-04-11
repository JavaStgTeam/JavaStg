package stg.core;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import stg.entity.base.Obj;
import stg.entity.bullet.Bullet;
import stg.entity.enemy.Enemy;
import stg.entity.item.Item;
import stg.entity.laser.Laser;


/**
 * 游戏世界 - 管理游戏中的所有实体
 * 
 * @author JavaSTG Team
 * @since 2026-02-17
 */
public class GameWorld {
    private final List<Enemy> enemies = new CopyOnWriteArrayList<>();
    private final List<Bullet> playerBullets = new CopyOnWriteArrayList<>();
    private final List<Bullet> enemyBullets = new CopyOnWriteArrayList<>();
    private final List<Item> items = new CopyOnWriteArrayList<>();
    private final List<Laser> lasers = new CopyOnWriteArrayList<>();
    
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
     * 添加激光
     */
    public void addObject(Laser laser) {
        if (laser != null) {
            lasers.add(laser);
        }
    }
    
    /**
     * 更新所有实体
     */
    public void update(int canvasWidth, int canvasHeight) {
        updateEnemies(canvasWidth, canvasHeight);
        updateBullets();
        updateItems();
        updateLasers(canvasWidth, canvasHeight);
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
                try {
                    Obj.release(enemy);
                } catch (Exception e) {
                    System.err.println("[GameWorld] 释放敌人对象失败: " + e.getMessage());
                }
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
            if (bullet.isOutOfBounds() || !bullet.isActive()) {
                playerBullets.remove(i);
                try {
                    Obj.release(bullet);
                } catch (Exception e) {
                    System.err.println("[GameWorld] 释放玩家子弹对象失败: " + e.getMessage());
                }
            }
        }
        
        // 更新敌人子弹
        for (int i = enemyBullets.size() - 1; i >= 0; i--) {
            Bullet bullet = enemyBullets.get(i);
            bullet.update();
            if (bullet.isOutOfBounds() || !bullet.isActive()) {
                enemyBullets.remove(i);
                try {
                    Obj.release(bullet);
                } catch (Exception e) {
                    System.err.println("[GameWorld] 释放敌人子弹对象失败: " + e.getMessage());
                }
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
                try {
                    Obj.release(item);
                } catch (Exception e) {
                    System.err.println("[GameWorld] 释放物品对象失败: " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * 更新激光
     */
    private void updateLasers(int canvasWidth, int canvasHeight) {
        for (int i = lasers.size() - 1; i >= 0; i--) {
            Laser laser = lasers.get(i);
            laser.update();
            if (!laser.isVisible() || laser.isOutOfBounds(canvasWidth, canvasHeight)) {
                lasers.remove(i);
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
     * 获取激光列表
     */
    public List<Laser> getLasers() {
        return lasers;
    }
    
    /**
     * 清除所有实体
     */
    public void clear() {
        enemies.clear();
        playerBullets.clear();
        enemyBullets.clear();
        items.clear();
        lasers.clear();
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
    
    /**
     * 清理游戏世界资源
     */
    public void cleanup() {
        clear();
    }
}