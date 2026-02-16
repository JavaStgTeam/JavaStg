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
        updateBullets(canvasWidth, canvasHeight);
        updateItems(canvasWidth, canvasHeight);
        
        // 检测子弹与敌人的碰撞
        checkBulletEnemyCollision();
    }
    
    /**
     * 检测子弹与敌人的碰撞
     */
    private void checkBulletEnemyCollision() {
        // 遍历所有敌人
        for (Enemy enemy : enemies) {
            if (!enemy.isAlive()) continue;
            
            float enemyX = enemy.getX();
            float enemyY = enemy.getY();
            float enemyRadius = enemy.getHitboxRadius();
            
            // 遍历所有玩家子弹
            for (Bullet bullet : playerBullets) {
                if (!bullet.isActive()) continue;
                
                float bulletX = bullet.getX();
                float bulletY = bullet.getY();
                float bulletRadius = bullet.getHitboxRadius();
                
                // 计算子弹与敌人之间的距离
                float distance = (float) Math.sqrt(
                    Math.pow(bulletX - enemyX, 2) + Math.pow(bulletY - enemyY, 2)
                );
                
                // 如果距离小于两者半径之和，发生碰撞
                if (distance < enemyRadius + bulletRadius) {
                    // 敌人受到伤害
                    enemy.takeDamage(10); // 假设每发子弹造成10点伤害
                    
                    // 子弹消失
                    bullet.setActive(false);
                    playerBullets.remove(bullet);
                }
            }
        }
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
    private void updateBullets(int canvasWidth, int canvasHeight) {
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
    private void updateItems(int canvasWidth, int canvasHeight) {
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
