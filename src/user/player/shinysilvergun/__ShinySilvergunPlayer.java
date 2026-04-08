package user.player.shinysilvergun;

import stg.entity.player.Player;
import stg.render.IRenderer;
import stg.util.SpriteSheetRenderer;

/**
 * 闪亮银枪自机类 - 实现闪亮银枪风格的自机
 * @since 2026-04-08
 */
public class __ShinySilvergunPlayer extends Player {
    // 动画相关
    private int animationFrame;
    private int animationCounter;
    private static final int ANIMATION_SPEED = 5;
    private int currentAnimation; // 0-站立, 1-左移, 2-右移, 3-射击
    
    // 武器系统
    private int weaponLevel; // 武器等级 1-3
    private int specialEnergy; // 特殊武器能量
    private static final int MAX_SPECIAL_ENERGY = 100;
    
    // 精灵表
    private int shipTextureId;
    
    /**
     * 构造函数
     */
    public __ShinySilvergunPlayer() {
        super(0, 0, 9.0f, 3.5f, 20);
        this.animationFrame = 0;
        this.animationCounter = 0;
        this.currentAnimation = 0;
        this.weaponLevel = 1;
        this.specialEnergy = 0;
        this.shipTextureId = -1;
    }
    
    /**
     * 构造函数
     * @param x 初始X坐标
     * @param y 初始Y坐标
     */
    public __ShinySilvergunPlayer(float x, float y) {
        super(x, y, 9.0f, 3.5f, 20);
        this.animationFrame = 0;
        this.animationCounter = 0;
        this.currentAnimation = 0;
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
            animationFrame = (animationFrame + 1) % 8;
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
    
    /**
     * 设置精灵表纹理ID
     * @param textureId 纹理ID
     */
    public void setShipTextureId(int textureId) {
        this.shipTextureId = textureId;
    }
    
    /**
     * 获取精灵表纹理ID
     * @return 纹理ID
     */
    public int getShipTextureId() {
        return shipTextureId;
    }
    
    @Override
    protected void shoot() {
        // 主武器射击
        fireMainWeapon();
        
        // 副武器射击（根据武器等级）
        if (weaponLevel > 1) {
            fireSecondaryWeapon();
        }
        
        // 更新射击动画
        currentAnimation = 3;
    }
    
    /**
     * 发射主武器
     */
    private void fireMainWeapon() {
        // 实现主武器射击逻辑
        // 根据武器等级发射不同数量和模式的子弹
        System.out.println("Shiny Silvergun: Main weapon fired (Level " + weaponLevel + ")");
    }
    
    /**
     * 发射副武器
     */
    private void fireSecondaryWeapon() {
        // 实现副武器射击逻辑
        // 向左右两侧发射子弹
        System.out.println("Shiny Silvergun: Secondary weapon fired");
    }
    
    /**
     * 发射特殊武器
     */
    public void fireSpecialWeapon() {
        if (specialEnergy >= 50) {
            // 实现特殊武器逻辑
            // 消耗能量，产生范围攻击
            specialEnergy -= 50;
            System.out.println("Shiny Silvergun: Special weapon fired! Energy left: " + specialEnergy);
        }
    }
    
    /**
     * 升级武器
     */
    public void upgradeWeapon() {
        if (weaponLevel < 3) {
            weaponLevel++;
            System.out.println("Shiny Silvergun: Weapon upgraded to Level " + weaponLevel);
        }
    }
    
    /**
     * 添加特殊武器能量
     * @param amount 能量值
     */
    public void addSpecialEnergy(int amount) {
        specialEnergy = Math.min(specialEnergy + amount, MAX_SPECIAL_ENERGY);
    }
    
    /**
     * 获取当前武器等级
     * @return 武器等级
     */
    public int getWeaponLevel() {
        return weaponLevel;
    }
    
    /**
     * 获取当前特殊武器能量
     * @return 能量值
     */
    public int getSpecialEnergy() {
        return specialEnergy;
    }
    
    /**
     * 获取最大特殊武器能量
     * @return 最大能量值
     */
    public int getMaxSpecialEnergy() {
        return MAX_SPECIAL_ENERGY;
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
        float screen