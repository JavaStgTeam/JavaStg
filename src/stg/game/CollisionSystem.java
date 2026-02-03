package stg.game;

import stg.game.bullet.Bullet;
import stg.game.enemy.Enemy;
import stg.game.item.Item;
import stg.game.player.Player;
import user.enemy.EnemyBullet;
import user.laser.EnemyLaser;

/**
 * 碰撞检测系�?- 处理游戏中的碰撞检�? */
public class CollisionSystem {
    private GameWorld world;
    private Player player;
    private static final int DEFAULT_BULLET_DAMAGE = 8;
    
    /**
     * 构造函数 - 创建碰撞检测系统实例
     * @param world 游戏世界实例
     * @param player 玩家实例
     */
    public CollisionSystem(GameWorld world, Player player) {
        this.world = world;
        this.player = player;
    }
    
    /**
     * 执行碰撞检测 - 检查玩家子弹、敌人子弹、敌人激光、玩家与物品的碰撞
     */
    public void checkCollisions() {
        checkPlayerBulletsVsEnemies();
        checkEnemyBulletsVsPlayer();
        checkEnemyLasersVsPlayer();
        checkPlayerVsItems();
    }
    
    /**
     * 检测玩家子弹与敌人的碰撞 - 若发生碰撞，敌人会受到伤害
     */
    private void checkPlayerBulletsVsEnemies() {
        for (Bullet bullet : world.getPlayerBullets()) {
            for (Enemy enemy : world.getEnemies()) {
                if (checkCollision(bullet, enemy)) {
                    int damage = bullet.getDamage() > 0 ? bullet.getDamage() : DEFAULT_BULLET_DAMAGE;
                    enemy.takeDamage(damage);
                    // 注意：这里不能直接移除子弹，因为我们使用的是只读列表
                    // 子弹的移除应该由GameWorld在update时处理
                    break;
                }
            }
        }
    }
    
    /**
     * 检测敌方子弹与玩家的碰撞 - 若发生碰撞，玩家会受到伤害
     */
    private void checkEnemyBulletsVsPlayer() {
        if (player == null || player.isInvincible()) return;
        
        for (EnemyBullet bullet : world.getEnemyBullets()) {
            if (checkCollision(bullet, player)) {
                player.onHit();
                // 注意：这里不能直接移除子弹，因为我们使用的是只读列表
            }
        }
    }
    
    /**
     * 检测敌方激光与玩家的碰撞 - 若发生碰撞，玩家会受到伤害
     */
    private void checkEnemyLasersVsPlayer() {
        if (player == null || player.isInvincible()) return;
        
        for (EnemyLaser laser : world.getEnemyLasers()) {
            if (laser.canHit() && laser.checkCollision(player.getX(), player.getY())) {
                player.onHit();
                laser.onHitPlayer(); // 启动冷却
            }
        }
    }
    
    /**
     * 检测玩家与物品的碰撞 - 若发生碰撞，物品会被采集
     */
    private void checkPlayerVsItems() {
        if (player == null) return;
        
        for (Item item : world.getItems()) {
            if (!item.isActive()) continue;
            
            float dx = item.getX() - player.getX();
            float dy = item.getY() - player.getY();
            float distance = (float)Math.sqrt(dx * dx + dy * dy);
            
            // 对于 Item，使用 getSize() 作为碰撞判定半径，因为可能没有 getHitboxRadius() 方法
            if (distance < item.getSize() + player.getSize()) {
                item.onCollect();
                // 注意：这里不能直接移除物品，因为我们使用的是只读列表
            }
        }
    }
    
    /**
     * 检测两个圆形对象的碰撞
     */
    private boolean checkCollision(Object obj1, Object obj2) {
        float x1, y1, size1;
        float x2, y2, size2;
        
        // 获取对象1的属性
        if (obj1 instanceof Bullet) {
            Bullet bullet = (Bullet)obj1;
            x1 = bullet.getX();
            y1 = bullet.getY();
            // 对于 Bullet，使用 getSize() 作为碰撞判定半径，因为可能没有 getHitboxRadius() 方法
            size1 = bullet.getSize();
        } else if (obj1 instanceof EnemyBullet) {
            EnemyBullet bullet = (EnemyBullet)obj1;
            x1 = bullet.getX();
            y1 = bullet.getY();
            // 对于 EnemyBullet，使用 getSize() 作为碰撞判定半径，因为可能没有 getHitboxRadius() 方法
            size1 = bullet.getSize();
        } else {
            return false;
        }
        
        // 获取对象2的属性
        if (obj2 instanceof Enemy) {
            Enemy enemy = (Enemy)obj2;
            x2 = enemy.getX();
            y2 = enemy.getY();
            size2 = enemy.getSize();
        } else if (obj2 instanceof Player) {
            Player player = (Player)obj2;
            x2 = player.getX();
            y2 = player.getY();
            // 对于 Player，使用 getSize() 作为碰撞判定半径，因为可能没有 getHitboxRadius() 方法
            size2 = player.getSize();
        } else {
            return false;
        }
        
        // 计算距离
        float dx = x1 - x2;
        float dy = y1 - y2;
        float distance = (float)Math.sqrt(dx * dx + dy * dy);
        
        // 判断是否碰撞
        return distance < (size1 + size2);
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
    public void setPlayer(Player player) {
        this.player = player;
    }
}
