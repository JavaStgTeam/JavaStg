package user.player.shinysilvergun;

import stg.entity.player.Player;
import stg.render.IRenderer;
import stg.util.SpriteSheetRenderer;

public class __ShinySilvergunPlayer extends Player {
    // 动画相关
    private int animationFrame;
    private int animationCounter;
    private static final int ANIMATION_SPEED = 5;
    private int currentAnimation; // 0-站立, 1-左移, 2-右移, 3-射击
    
    // 武器系统
    public enum WeaponMode {
        A, B, C, AB, AC, BC, ABC
    }
    private WeaponMode currentWeaponMode; // 当前武器模式
    private int weaponLevel; // 武器等级 1-3
    private int specialEnergy; // 特殊武器能量
    private static final int MAX_SPECIAL_ENERGY = 100;
    
    // 精灵表
    private int shipTextureId;
    
    // 构造函数
    public __ShinySilvergunPlayer() {
        super(0, 0, 9.0f, 3.5f, 20);
        this.animationFrame = 0;
        this.animationCounter = 0;
        this.currentAnimation = 0;
        this.currentWeaponMode = WeaponMode.A; // 默认A火力
        this.weaponLevel = 1;
        this.specialEnergy = 0;
        this.shipTextureId = -1;
    }
    
    public __ShinySilvergunPlayer(float x, float y) {
        super(x, y, 9.0f, 3.5f, 20);
        this.animationFrame = 0;
        this.animationCounter = 0;
        this.currentAnimation = 0;
        this.currentWeaponMode = WeaponMode.A; // 默认A火力
        this.weaponLevel = 1;
        this.specialEnergy = 0;
        this.shipTextureId = -1;
    }
    
    @Override
    protected void onUpdate() {
        updateAnimation();
    }
    
    /**
     * 更新动画状态
     */
    private void updateAnimation() {
        animationCounter++;
        if (animationCounter >= ANIMATION_SPEED) {
            animationCounter = 0;
            animationFrame = (animationFrame + 1) % 8; // 8帧循环
        }
        
        // 根据移动状态更新动画
        if (getVelocityX() < 0) {
            currentAnimation = 1; // 左移
        } else if (getVelocityX() > 0) {
            currentAnimation = 2; // 右移
        } else if (!isShooting()) {
            currentAnimation = 0; // 站立
        }
        // 射击动画由shoot()方法控制
    }
    
    @Override
    protected void shoot() {
        // 根据当前武器模式射击
        switch (currentWeaponMode) {
            case A:
                fireAWeapon();
                break;
            case B:
                fireBWeapon();
                break;
            case C:
                fireCWeapon();
                break;
            case AB:
                fireABWeapon();
                break;
            case AC:
                fireACWeapon();
                break;
            case BC:
                fireBCWeapon();
                break;
            case ABC:
                fireABCWeapon();
                break;
        }
        
        // 更新射击动画
        currentAnimation = 3;
    }
    
    private void fireAWeapon() {
        // A火力：前方直线射击
        System.out.println("Firing A weapon (straight forward)");
    }
    
    private void fireBWeapon() {
        // B火力：追踪敌人，最小转向半径为5
        System.out.println("Firing B weapon (homing with minimum turn radius 5)");
    }
    
    private void fireCWeapon() {
        // C火力：斜向射击，15°和165°
        System.out.println("Firing C weapon (diagonal at 15° and 165°)");
    }
    
    private void fireABWeapon() {
        // AB火力：两条线，指向敌人时锁定
        System.out.println("Firing AB weapon (double lines, lock on when aiming at enemies)");
    }
    
    private void fireACWeapon() {
        // AC火力：前方低威力，和后方扇形射击
        System.out.println("Firing AC weapon (low power forward, fan-shaped backward)");
    }
    
    private void fireBCWeapon() {
        // BC火力：展开圆形立场，锁定攻击立场内敌人
        System.out.println("Firing BC weapon (circular field, lock on enemies within field)");
    }
    
    private void fireABCWeapon() {
        // ABC火力：展开一个光剑
        System.out.println("Firing ABC weapon (lightsaber)");
    }
    
    public void switchWeaponMode(WeaponMode mode) {
        this.currentWeaponMode = mode;
        System.out.println("Switched to weapon mode: " + mode);
    }
    
    public WeaponMode getCurrentWeaponMode() {
        return currentWeaponMode;
    }
    
    public void upgradeWeapon() {
        if (weaponLevel < 3) {
            weaponLevel++;
            System.out.println("Weapon upgraded to level: " + weaponLevel);
        }
    }
    
    public void addSpecialEnergy(int amount) {
        specialEnergy = Math.min(specialEnergy + amount, MAX_SPECIAL_ENERGY);
    }
    
    public int getWeaponLevel() {
        return weaponLevel;
    }
    
    public int getSpecialEnergy() {
        return specialEnergy;
    }
    
    public void setShipTextureId(int textureId) {
        this.shipTextureId = textureId;
    }
    
    public int getShipTextureId() {
        return shipTextureId;
    }
    
    @Override
    public void render(IRenderer renderer) {
        // 无敌闪烁效果
        boolean shouldRender = true;
        if (getInvincibleTimer() > 0) {
            int flashPhase = getInvincibleTimer() % 10;
            if (flashPhase < 5) {
                shouldRender = false;
            }
        }
        
        if (!shouldRender) return;
        
        // 转换为屏幕坐标
        requireCoordinateSystem();
        float[] screenCoords = toScreenCoords(getX(), getY());
        float screenX = screenCoords[0];
        float screenY = screenCoords[1];
        
        // 绘制精灵
        if (shipTextureId != -1) {
            final int SPRITE_WIDTH = 48;
            final int SPRITE_HEIGHT = 48;
            final int SPRITES_PER_ROW = 8;
            final int SPRITE_SHEET_WIDTH = 384;
            final int SPRITE_SHEET_HEIGHT = 272;
            final float SCALE = 1.5f;
            
            SpriteSheetRenderer.drawSpriteFrame(
                renderer,
                shipTextureId,
                screenX,
                screenY,
                SPRITE_WIDTH,
                SPRITE_HEIGHT,
                SPRITE_SHEET_WIDTH,
                SPRITE_SHEET_HEIGHT,
                currentAnimation,
                animationFrame,
                SPRITES_PER_ROW,
                SCALE
            );
        } else {
            //  fallback: 绘制默认形状
            renderer.drawCircle(screenX, screenY, getSize()/2, 0.8f, 0.8f, 0.9f, 1.0f);
        }
        
        // 低速模式时显示受击判定点
        if (isSlowMode()) {
            float hitboxRadius = getHitboxRadius() * 2.0f;
            renderer.drawCircle(screenX, screenY, hitboxRadius, 1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
}