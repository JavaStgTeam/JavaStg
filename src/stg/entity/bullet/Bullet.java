package stg.entity.bullet;

import java.awt.Color;
import java.awt.Graphics2D;

import stg.entity.base.Obj;
import stg.util.objectpool.Pooled;
import stg.util.objectpool.Resettable;

/**
 * 子弹类
 * @date 2026-01-19 使用中心原点坐标
 * @date 2026-02-20 支持对象池管理
 * @date 2026-03-10 完全重构，实现基本功能
 */
@Pooled(initialCapacity = 100, maxCapacity = 500, name = "BulletPool")
public class Bullet extends Obj implements Resettable, IBullet {
    protected int damage = 0; // 子弹伤害，默认0
    protected float lifeTime = -1; // 生命周期（-1表示无限）
    protected float currentLifeTime = 0; // 当前生命周期
    protected boolean isPlayerBullet = false; // 是否为玩家子弹
    protected int pierceCount = 0; // 穿透次数（0表示不可穿透）
    protected int bounceCount = 0; // 反弹次数（0表示不可反弹）
    protected boolean homing = false; // 是否跟踪目标
    protected float homingStrength = 0.1f; // 跟踪强度
    protected float damageMultiplier = 1.0f; // 伤害倍率
    protected float speedMultiplier = 1.0f; // 速度倍率
    protected float sizeMultiplier = 1.0f; // 大小倍率
    protected boolean hasTrail = false; // 是否有轨迹效果
    protected float trailLength = 10.0f; // 轨迹长度
    
    /**
     * 构造函数
     * @param x 初始X坐标
     * @param y 初始Y坐标
     * @param vx X方向速度
     * @param vy Y方向速度
     * @param size 子弹大小
     * @param color 子弹颜色
     */
    public Bullet(float x, float y, float vx, float vy, float size, Color color) {
        super(x, y, vx, vy, size, color);
        // 设置碰撞判定半径为size的5倍，确保高速子弹不会穿透敌人
        setHitboxRadius(size * 5.0f);
    }

    // ========== 伤害相关 ==========

    /**
     * 获取子弹伤害
     * @return 子弹伤害值
     */
    @Override
    public int getDamage() {
        return damage;
    }

    /**
     * 设置子弹伤害
     * @param damage 伤害值
     */
    @Override
    public void setDamage(int damage) {
        this.damage = damage;
    }

    // ========== 速度和方向相关 ==========

    /**
     * 获取子弹速度
     * @return 子弹速度
     */
    @Override
    public float getSpeed() {
        float vx = getVx();
        float vy = getVy();
        return (float) Math.sqrt(vx * vx + vy * vy);
    }

    /**
     * 设置子弹速度
     * @param speed 子弹速度
     */
    @Override
    public void setSpeed(float speed) {
        float vx = getVx();
        float vy = getVy();
        float currentSpeed = (float) Math.sqrt(vx * vx + vy * vy);
        if (currentSpeed > 0) {
            float ratio = speed / currentSpeed;
            setVx(vx * ratio);
            setVy(vy * ratio);
        }
    }

    /**
     * 获取子弹方向（角度，单位：度）
     * @return 子弹方向
     */
    @Override
    public float getDirection() {
        return (float) Math.toDegrees(Math.atan2(getVy(), getVx()));
    }

    /**
     * 设置子弹方向（角度，单位：度）
     * @param direction 子弹方向
     */
    @Override
    public void setDirection(float direction) {
        float vx = getVx();
        float vy = getVy();
        float speed = (float) Math.sqrt(vx * vx + vy * vy);
        float radians = (float) Math.toRadians(direction);
        setVx((float) Math.cos(radians) * speed);
        setVy((float) Math.sin(radians) * speed);
    }

    // ========== 生命周期相关 ==========

    /**
     * 获取子弹生命周期
     * @return 生命周期
     */
    public float getLifeTime() {
        return lifeTime;
    }

    /**
     * 设置子弹生命周期
     * @param lifeTime 生命周期
     */
    public void setLifeTime(float lifeTime) {
        this.lifeTime = lifeTime;
    }

    // ========== 核心方法 ==========

    /**
     * 自定义更新逻辑
     */
    @Override
    protected void onUpdate() {
        // 生命周期管理
        updateLifeTime();
    }

    /**
     * 更新生命周期
     */
    protected void updateLifeTime() {
        if (lifeTime > 0) {
            currentLifeTime += 0.0166667f; // 60FPS的时间增量，避免每次计算
            if (currentLifeTime >= lifeTime) {
                setActive(false);
            }
        }
    }

    /**
     * 检查子弹是否超出边界
     * @param width 游戏宽度
     * @param height 游戏高度
     * @return 是否超出边界
     */
    @Override
    public boolean isOutOfBounds(int width, int height) {
        float radius = getHitboxRadius();
        return getX() < -radius || getX() > width + radius || 
               getY() < -radius || getY() > height + radius;
    }

    /**
     * 渲染子弹（Java2D版本）
     * @param g 图形上下文
     */
    @Override
    public void render(Graphics2D g) {
        if (!isActive()) return;
        
        // 优化：避免重复的坐标系统检查
        if (!isCoordinateSystemInitialized()) return;
        
        // 优化：使用缓存的坐标转换
        float[] screenCoords = toScreenCoords(getX(), getY());
        float screenX = screenCoords[0];
        float screenY = screenCoords[1];
        float renderSize = getSize() * sizeMultiplier;
        
        // 优化：避免重复设置颜色
        g.setColor(getColor());
        
        // 优化：使用整数坐标进行渲染
        int intX = Math.round(screenX - renderSize / 2);
        int intY = Math.round(screenY - renderSize / 2);
        int intSize = Math.round(renderSize);
        
        g.fillOval(intX, intY, intSize, intSize);
        
        // 渲染轨迹效果
        if (hasTrail) {
            renderTrail(g, screenX, screenY);
        }
    }
    
    /**
     * 渲染子弹轨迹
     * @param g 图形上下文
     * @param screenX 屏幕X坐标
     * @param screenY 屏幕Y坐标
     */
    protected void renderTrail(Graphics2D g, float screenX, float screenY) {
        // 优化：使用当前颜色的半透明版本
        Color originalColor = getColor();
        Color trailColor = new Color(
            originalColor.getRed(),
            originalColor.getGreen(),
            originalColor.getBlue(),
            128 // 半透明
        );
        
        g.setColor(trailColor);
        
        // 计算轨迹方向
        float vx = getVx();
        float vy = getVy();
        float speed = (float) Math.sqrt(vx * vx + vy * vy);
        
        if (speed > 0) {
            // 计算轨迹终点
            float trailX = screenX - (vx / speed) * trailLength;
            float trailY = screenY - (vy / speed) * trailLength;
            
            // 渲染轨迹线
            g.drawLine(
                Math.round(screenX),
                Math.round(screenY),
                Math.round(trailX),
                Math.round(trailY)
            );
        }
    }
    
    /**
     * 重置子弹状态
     * 当子弹被回收到对象池时调用
     */
    @Override
    public void resetState() {
        // 重置子弹的基本属性
        setActive(true);
        setX(0);
        setY(0);
        setVx(0);
        setVy(0);
        setHitboxRadius(getSize() * 5.0f);
        damage = 0;
        lifeTime = -1;
        currentLifeTime = 0;
        isPlayerBullet = false;
        pierceCount = 0;
        bounceCount = 0;
        homing = false;
        homingStrength = 0.1f;
        damageMultiplier = 1.0f;
        speedMultiplier = 1.0f;
        sizeMultiplier = 1.0f;
        hasTrail = false;
        trailLength = 10.0f;
    }
    
    // ========== 子弹属性相关方法 ==========
    
    /**
     * 获取是否为玩家子弹
     * @return 是否为玩家子弹
     */
    public boolean isPlayerBullet() {
        return isPlayerBullet;
    }
    
    /**
     * 设置是否为玩家子弹
     * @param isPlayerBullet 是否为玩家子弹
     */
    public void setPlayerBullet(boolean isPlayerBullet) {
        this.isPlayerBullet = isPlayerBullet;
    }
    
    /**
     * 获取穿透次数
     * @return 穿透次数
     */
    public int getPierceCount() {
        return pierceCount;
    }
    
    /**
     * 设置穿透次数
     * @param pierceCount 穿透次数
     */
    public void setPierceCount(int pierceCount) {
        this.pierceCount = pierceCount;
    }
    
    /**
     * 获取反弹次数
     * @return 反弹次数
     */
    public int getBounceCount() {
        return bounceCount;
    }
    
    /**
     * 设置反弹次数
     * @param bounceCount 反弹次数
     */
    public void setBounceCount(int bounceCount) {
        this.bounceCount = bounceCount;
    }
    
    /**
     * 获取是否跟踪目标
     * @return 是否跟踪目标
     */
    public boolean isHoming() {
        return homing;
    }
    
    /**
     * 设置是否跟踪目标
     * @param homing 是否跟踪目标
     */
    public void setHoming(boolean homing) {
        this.homing = homing;
    }
    
    /**
     * 获取跟踪强度
     * @return 跟踪强度
     */
    public float getHomingStrength() {
        return homingStrength;
    }
    
    /**
     * 设置跟踪强度
     * @param homingStrength 跟踪强度
     */
    public void setHomingStrength(float homingStrength) {
        this.homingStrength = homingStrength;
    }
    
    /**
     * 获取伤害倍率
     * @return 伤害倍率
     */
    public float getDamageMultiplier() {
        return damageMultiplier;
    }
    
    /**
     * 设置伤害倍率
     * @param damageMultiplier 伤害倍率
     */
    public void setDamageMultiplier(float damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }
    
    /**
     * 获取速度倍率
     * @return 速度倍率
     */
    public float getSpeedMultiplier() {
        return speedMultiplier;
    }
    
    /**
     * 设置速度倍率
     * @param speedMultiplier 速度倍率
     */
    public void setSpeedMultiplier(float speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
    }
    
    /**
     * 获取大小倍率
     * @return 大小倍率
     */
    public float getSizeMultiplier() {
        return sizeMultiplier;
    }
    
    /**
     * 设置大小倍率
     * @param sizeMultiplier 大小倍率
     */
    public void setSizeMultiplier(float sizeMultiplier) {
        this.sizeMultiplier = sizeMultiplier;
    }
    
    /**
     * 获取是否有轨迹效果
     * @return 是否有轨迹效果
     */
    public boolean hasTrail() {
        return hasTrail;
    }
    
    /**
     * 设置是否有轨迹效果
     * @param hasTrail 是否有轨迹效果
     */
    public void setHasTrail(boolean hasTrail) {
        this.hasTrail = hasTrail;
    }
    
    /**
     * 获取轨迹长度
     * @return 轨迹长度
     */
    public float getTrailLength() {
        return trailLength;
    }
    
    /**
     * 设置轨迹长度
     * @param trailLength 轨迹长度
     */
    public void setTrailLength(float trailLength) {
        this.trailLength = trailLength;
    }
    
    // ========== 辅助方法 ==========
    
    /**
     * 获取X坐标
     * @return X坐标
     */
    @Override
    public float getX() {
        return x;
    }
    
    /**
     * 获取Y坐标
     * @return Y坐标
     */
    @Override
    public float getY() {
        return y;
    }
    
    /**
     * 获取X方向速度
     * @return X方向速度
     */
    public float getVx() {
        return vx;
    }
    
    /**
     * 获取Y方向速度
     * @return Y方向速度
     */
    public float getVy() {
        return vy;
    }
    
    /**
     * 设置X方向速度
     * @param vx X方向速度
     */
    public void setVx(float vx) {
        this.vx = vx;
    }
    
    /**
     * 设置Y方向速度
     * @param vy Y方向速度
     */
    public void setVy(float vy) {
        this.vy = vy;
    }
    
    /**
     * 设置X坐标
     * @param x X坐标
     */
    public void setX(float x) {
        this.x = x;
    }
    
    /**
     * 设置Y坐标
     * @param y Y坐标
     */
    public void setY(float y) {
        this.y = y;
    }
    
    /**
     * 获取大小
     * @return 大小
     */
    @Override
    public float getSize() {
        return size;
    }
    
    /**
     * 获取碰撞检测半径
     * @return 碰撞检测半径
     */
    @Override
    public float getHitboxRadius() {
        return hitboxRadius;
    }
    
    /**
     * 设置碰撞检测半径
     * @param hitboxRadius 碰撞检测半径
     */
    public void setHitboxRadius(float hitboxRadius) {
        this.hitboxRadius = hitboxRadius;
    }
    
    /**
     * 检查是否活跃
     * @return 是否活跃
     */
    @Override
    public boolean isActive() {
        return active;
    }
    
    /**
     * 设置是否活跃
     * @param active 是否活跃
     */
    @Override
    public void setActive(boolean active) {
        this.active = active;
    }
    
    /**
     * 更新对象状态
     */
    @Override
    public void update() {
        super.update();
    }
}

