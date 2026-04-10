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
    
    // 按键状态提供者
    private stg.base.KeyStateProvider weaponKeyStateProvider;
    
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
        this.weaponKeyStateProvider = null;
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
        this.weaponKeyStateProvider = null;
    }
    
    @Override
    public void setKeyStateProvider(stg.base.KeyStateProvider provider) {
        super.setKeyStateProvider(provider);
        this.weaponKeyStateProvider = provider;
    }
    
    @Override
    protected void onUpdate() {
        updateAnimation();
        handleWeaponSwitching();
    }
    
    /**
     * 处理武器切换逻辑
     */
    private void handleWeaponSwitching() {
        if (weaponKeyStateProvider == null) {
            return;
        }
        
        boolean zPressed = weaponKeyStateProvider.isZPressed();
        boolean xPressed = weaponKeyStateProvider.isXPressed();
        boolean cPressed = weaponKeyStateProvider.isCPressed();
        
        // 根据ZXC按键组合切换武器模式
        if (zPressed && xPressed && cPressed) {
            // Z+X+C = ABC模式
            switchWeaponMode(WeaponMode.ABC);
        } else if (zPressed && xPressed) {
            // Z+X = AB模式
            switchWeaponMode(WeaponMode.AB);
        } else if (zPressed && cPressed) {
            // Z+C = AC模式
            switchWeaponMode(WeaponMode.AC);
        } else if (xPressed && cPressed) {
            // X+C = BC模式
            switchWeaponMode(WeaponMode.BC);
        } else if (zPressed) {
            // Z = A模式
            switchWeaponMode(WeaponMode.A);
        } else if (xPressed) {
            // X = B模式
            switchWeaponMode(WeaponMode.B);
        } else if (cPressed) {
            // C = C模式
            switchWeaponMode(WeaponMode.C);
        }
        // 没有按键按下时保持当前模式
    }
    
    /**
     * 检查是否正在射击
     */
    private boolean isShooting() {
        return weaponKeyStateProvider != null && weaponKeyStateProvider.isZPressed();
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
        // 火神炮（A）：向前方直线连续发射密集小型光束，弹道集中
        System.out.println("Firing Vulcan Cannon (A): Dense small beams forward, concentrated弹道");
    }
    
    private void fireBWeapon() {
        // 跟踪弹（B）：发射可自动追踪锁定敌机的能量弹，追踪性强
        System.out.println("Firing Homing Missiles (B): Auto-tracking energy projectiles with strong homing capability");
    }
    
    private void fireCWeapon() {
        // 扩散弹（C）：向斜上方大范围扇形扩散射击，覆盖左右广阔区域
        System.out.println("Firing Spread Shot (C): Wide fan-shaped spread shooting to cover large area");
    }
    
    private void fireABWeapon() {
        // 跟踪等离子（A+B）：自动锁定目标，持续输出高伤害等离子激光
        System.out.println("Firing Homing Plasma (A+B): Auto-lock target, continuous high-damage plasma laser");
    }
    
    private void fireACWeapon() {
        // 后射扩散（A+C）：前方直线射击，同时向斜后方发射扩散弹幕，前后兼顾
        System.out.println("Firing Rear Spread (A+C): Forward straight shot with backward spread弹幕");
    }
    
    private void fireBCWeapon() {
        // 锁定扩散（B+C）：对范围内多个敌人同时锁定，发射多束跟踪弹进行集火
        System.out.println("Firing Lock-on Spread (B+C): Multi-target lock-on with multiple homing projectiles");
    }
    
    private void fireABCWeapon() {
        // 光辉剑（A+B+C）：展开巨大近战光剑，近身斩击敌人，可格挡并吸收敌方弹幕
        System.out.println("Firing Radiant Sword (A+B+C): Giant melee lightsaber, can block and absorb enemy弹幕");
    }
    
    public void switchWeaponMode(WeaponMode mode) {
        if (this.currentWeaponMode != mode) {
            this.currentWeaponMode = mode;
            System.out.println("Switched to weapon mode: " + mode);
        }
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