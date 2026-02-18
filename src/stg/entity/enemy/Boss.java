package stg.entity.enemy;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Boss基类
 * 管理Boss的入场、退场和符卡系统
 * @since 2026-02-14
 */
public abstract class Boss extends Enemy {
    protected List<EnemySpellcard> spellcards;
    protected EnemySpellcard currentSpellcard;
    protected int currentPhase;
    protected int maxPhase;
    protected boolean isEntering;
    protected boolean isExiting;
    protected int enterFrameCount;
    protected int exitFrameCount;
    protected static final int ENTER_DURATION = 120; // 入场动画持续120帧
    protected static final int EXIT_DURATION = 90; // 退场动画持续90帧
    
    /**
     * 构造函数
     * @param x 初始X坐标
     * @param y 初始Y坐标
     * @param size 大小
     * @param color 颜色
     */
    public Boss(float x, float y, float size, Color color) {
        super(x, y, 0, 0, size, color, 1); // 传递1作为初始生命值，因为父类需要
        this.spellcards = new ArrayList<>();
        this.currentSpellcard = null;
        this.currentPhase = 0;
        this.maxPhase = 0;
        this.isEntering = true;
        this.isExiting = false;
        this.enterFrameCount = 0;
        this.exitFrameCount = 0;
    }
    
    /**
     * 初始化符卡
     * 由子类实现，添加所有符卡
     */
    protected abstract void initSpellcards();
    
    /**
     * 更新Boss状态
     */
    @Override
    public void update() {
        // 只更新位置，不调用super.update避免生命值检查
        frame++;
        onUpdate();
        onMove();
        x += vx;
        y += vy;
        
        if (isEntering) {
            updateEnterLogic();
            return;
        }
        
        if (isExiting) {
            updateExitLogic();
            return;
        }
        
        // 更新当前符卡
        if (currentSpellcard != null && currentSpellcard.isActive()) {
            currentSpellcard.update();
            
            // 检查符卡是否被击败
            if (currentSpellcard.isDefeated()) {
                currentSpellcard.end();
                startNextSpellcard();
            }
        } else if (currentSpellcard == null) {
            // 如果没有当前符卡，开始第一个符卡
            startNextSpellcard();
        }
    }
    
    /**
     * 更新Boss状态
     * @param canvasWidth 画布宽度（兼容参数，不使用）
     * @param canvasHeight 画布高度（兼容参数，不使用）
     */
    @Override
    public void update(int canvasWidth, int canvasHeight) {
        update(); // 调用无参数版本
    }
    
    /**
     * 更新入场逻辑
     */
    protected void updateEnterLogic() {
        enterFrameCount++;
        
        // 入场动画：从屏幕上方移动到指定位置
        float targetY = getY();
        // 从游戏逻辑坐标系的顶部边界上方进入
        stg.entity.base.Obj.requireCoordinateSystem();
        stg.util.CoordinateSystem cs = stg.entity.base.Obj.getSharedCoordinateSystem();
        float topBound = cs.getTopBound();
        float enterStartY = topBound + 50; // 从顶部边界上方50像素处开始入场
        
        setY(enterStartY - (enterStartY - targetY) * (float)enterFrameCount / ENTER_DURATION);
        
        if (enterFrameCount >= ENTER_DURATION) {
            isEntering = false;
            // 初始化符卡
            initSpellcards();
            maxPhase = spellcards.size();
            // 开始第一个符卡
            startNextSpellcard();
        }
    }
    
    /**
     * 更新退场逻辑
     */
    protected void updateExitLogic() {
        exitFrameCount++;
        
        // 退场动画：向屏幕上方移动
        setY(getY() - 2.0f);
        
        if (exitFrameCount >= EXIT_DURATION) {
            setActive(false);
        }
    }
    
    /**
     * 开始下一个符卡
     */
    protected void startNextSpellcard() {
        currentPhase++;
        
        if (currentPhase > spellcards.size()) {
            // 所有符卡都已完成，开始退场
            startExit();
            return;
        }
        
        // 获取并开始当前符卡
        currentSpellcard = spellcards.get(currentPhase - 1);
        currentSpellcard.start();
    }
    
    /**
     * 开始退场
     */
    protected void startExit() {
        isExiting = true;
    }
    
    /**
     * 添加符卡
     * @param spellcard 符卡
     */
    protected void addSpellcard(EnemySpellcard spellcard) {
        spellcards.add(spellcard);
    }
    
    /**
     * 获取当前符卡
     * @return 当前符卡
     */
    public EnemySpellcard getCurrentSpellcard() {
        return currentSpellcard;
    }
    
    /**
     * 获取当前阶段
     * @return 当前阶段
     */
    public int getCurrentPhase() {
        return currentPhase;
    }
    
    /**
     * 获取最大阶段
     * @return 最大阶段
     */
    public int getMaxPhase() {
        return maxPhase;
    }
    
    /**
     * 检查是否正在入场
     * @return 是否正在入场
     */
    public boolean isEntering() {
        return isEntering;
    }
    
    /**
     * 检查是否正在退场
     * @return 是否正在退场
     */
    public boolean isExiting() {
        return isExiting;
    }
    
    /**
     * 任务开始时触发的方法
     */
    @Override
    protected void onTaskStart() {
        // 空实现，由子类根据需要重写
    }
    
    /**
     * 受到伤害
     * @param damage 伤害值
     */
    @Override
    public void takeDamage(int damage) {
        // 将伤害传递给当前符卡
        if (currentSpellcard != null && currentSpellcard.isActive()) {
            currentSpellcard.takeDamage(damage);
        }
    }
    
    /**
     * 渲染生命值条
     * @param g 图形上下文
     * @param screenX 屏幕X坐标
     * @param screenY 屏幕Y坐标
     */
    @Override
    protected void renderHealthBar(Graphics2D g, float screenX, float screenY) {
        // 如果有当前符卡，显示符卡的生命值
        if (currentSpellcard != null && currentSpellcard.isActive()) {
            int barWidth = (int)(getSize() * 3); // 符卡生命值条更宽
            int barHeight = 6; // 符卡生命值条更高
            int barX = (int)(screenX - getSize() * 1.5f);
            int barY = (int)(screenY - getSize() - 15);

            // 背景
            g.setColor(Color.GRAY);
            g.fillRect(barX, barY, barWidth, barHeight);

            // 生命值
            float hpPercent = (float)currentSpellcard.getHp() / currentSpellcard.getMaxHp();
            // 符卡阶段使用不同颜色
            if (currentSpellcard.isSpellcardPhase()) {
                g.setColor(Color.BLUE);
            } else {
                g.setColor(Color.RED);
            }
            g.fillRect(barX, barY, (int)(barWidth * hpPercent), barHeight);
            
            // 显示符卡名称
            if (currentSpellcard.isSpellcardPhase()) {
                g.setColor(Color.WHITE);
                g.setFont(new Font("Monospace", Font.PLAIN, 12));
                g.drawString(currentSpellcard.getName(), barX, barY - 5);
            }
        } else {
            // 没有符卡时显示默认生命值条
            super.renderHealthBar(g, screenX, screenY);
        }
    }
    
    /**
     * 任务结束时触发的方法
     */
    @Override
    protected void onTaskEnd() {
        // 空实现，由子类根据需要重写
    }
}