# 闪亮银枪自机开发文档

## 1. 项目概述

本开发文档描述了在JavaSTG引擎中实现闪亮银枪（Shiny Silvergun）风格自机的详细计划和功能设计。闪亮银枪是一款经典的射击游戏，以其独特的多方向射击系统、武器升级机制和高机动性而闻名。

## 2. 开发环境

- **引擎**：JavaSTG 游戏引擎
- **开发语言**：Java
- **依赖**：LWJGL 3.3.2
- **目标平台**：Windows

## 3. 自机设计

### 3.1 基本属性

| 属性 | 值 | 说明 |
|------|-----|------|
| 普通移动速度 | 9.0f | 正常模式下的移动速度 |
| 低速移动速度 | 3.5f | 低速模式下的移动速度 |
| 大小 | 20.0f | 自机碰撞体积大小 |
| 碰撞判定半径 | 2.5f | 受击判定半径 |
| 射击间隔 | 1帧 | 基础射击频率 |
| 基础子弹伤害 | 3 | 初始子弹伤害值 |

### 3.2 武器系统

闪亮银枪自机将实现以下武器系统：

1. **火神炮（A）**：向前方直线连续发射密集小型光束，弹道集中
2. **跟踪弹（B）**：发射可自动追踪锁定敌机的能量弹，追踪性强
3. **扩散弹（C）**：向斜上方大范围扇形扩散射击，覆盖左右广阔区域
4. **后射扩散（A+C）**：前方直线射击，同时向斜后方发射扩散弹幕，前后兼顾
5. **跟踪等离子（A+B）**：自动锁定目标，持续输出高伤害等离子激光
6. **锁定扩散（B+C）**：对范围内多个敌人同时锁定，发射多束跟踪弹进行集火
7. **光辉剑（A+B+C）**：展开巨大近战光剑，近身斩击敌人，可格挡并吸收敌方弹幕

### 3.3 移动特性

- **高机动性**：比普通自机更快的移动速度
- **精准控制**：低速模式下的精细操作
- **平滑移动**：实现惯性移动效果

### 3.4 操作方式

- **移动**：方向键控制
- **低速模式**：Shift键
- **射击**：Z键
- **武器切换**：
  - **A模式**：Z键
  - **B模式**：X键
  - **C模式**：C键
  - **AB模式**：Z+X键
  - **AC模式**：Z+C键
  - **BC模式**：X+C键
  - **ABC模式**：Z+X+C键

## 4. 开发计划

### 4.1 阶段一：基础架构搭建

1. **创建自机类**：继承自 `Player` 基类
2. **实现基本移动**：重写移动相关方法
3. **设置初始属性**：配置速度、大小等参数

### 4.2 阶段二：武器系统实现

1. **A火力实现**：前方直线射击逻辑
2. **B火力实现**：追踪敌人逻辑（最小转向半径5）
3. **C火力实现**：斜向射击逻辑（15°和165°）
4. **AB火力实现**：双线射击并锁定敌人
5. **AC火力实现**：前方低威力射击和后方扇形射击
6. **BC火力实现**：圆形立场锁定攻击
7. **ABC火力实现**：光剑效果

### 4.3 阶段三：视觉效果

1. **精灵表设计**：创建闪亮银枪风格的精灵表
2. **动画实现**：移动、射击动画
3. **特效效果**：子弹特效、击中特效

### 4.4 阶段四：测试与优化

1. **功能测试**：验证所有功能正常工作
2. **性能优化**：确保流畅运行
3. **平衡调整**：调整武器伤害和移动速度

## 5. 核心功能实现

### 5.1 自机类结构

```java
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
    
    // 其他方法...
}
```

### 5.2 射击系统实现

```java
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
}

private void fireBWeapon() {
    // 跟踪弹（B）：发射可自动追踪锁定敌机的能量弹，追踪性强
}

private void fireCWeapon() {
    // 扩散弹（C）：向斜上方大范围扇形扩散射击，覆盖左右广阔区域
}

private void fireABWeapon() {
    // 跟踪等离子（A+B）：自动锁定目标，持续输出高伤害等离子激光
}

private void fireACWeapon() {
    // 后射扩散（A+C）：前方直线射击，同时向斜后方发射扩散弹幕，前后兼顾
}

private void fireBCWeapon() {
    // 锁定扩散（B+C）：对范围内多个敌人同时锁定，发射多束跟踪弹进行集火
}

private void fireABCWeapon() {
    // 光辉剑（A+B+C）：展开巨大近战光剑，近身斩击敌人，可格挡并吸收敌方弹幕
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

/**
 * 处理武器切换逻辑
 */
private void handleWeaponSwitching() {
    if (keyStateProvider == null || respawning) {
        return;
    }
    
    boolean zPressed = keyStateProvider.isZPressed();
    boolean xPressed = keyStateProvider.isXPressed();
    boolean cPressed = keyStateProvider.isCPressed();
    
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
```

### 5.3 武器升级机制

```java
public void upgradeWeapon() {
    if (weaponLevel < 3) {
        weaponLevel++;
        // 升级武器效果，增强所有武器模式的威力
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
```

### 5.4 动画系统

```java
@Override
protected void onUpdate() {
    updateAnimation();
}

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
```

### 5.5 渲染实现

```java
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
```

## 6. 资源需求

### 6.1 精灵表

- **文件名**：`shinysilvergun.png`
- **尺寸**：384x272 像素
- **包含动画**：站立、左移、右移、射击
- **每帧大小**：48x48 像素

### 6.2 音效

- **射击音效**：`se_shoot.wav`
- **升级音效**：`se_upgrade.wav`
- **特殊武器音效**：`se_special.wav`

## 7. 测试计划

1. **功能测试**：验证移动、射击、升级等功能
2. **性能测试**：确保在高弹幕情况下流畅运行
3. **平衡测试**：调整武器伤害和移动速度
4. **兼容性测试**：确保在不同硬件配置下正常运行

## 8. 预期效果

- **流畅的移动**：高机动性和精准控制
- **强大的火力**：多方向射击能力
- **视觉冲击力**：炫酷的动画和特效
- **良好的游戏体验**：平衡的武器系统和升级机制

## 9. 开发时间估计

| 阶段 | 时间估计 |
|------|----------|
| 基础架构搭建 | 1天 |
| 武器系统实现 | 3天 |
| 视觉效果 | 1天 |
| 测试与优化 | 1天 |
| **总计** | **6天** |

## 10. 结论

本开发文档详细规划了闪亮银枪自机的实现方案，包括基础属性、武器系统、移动特性等核心功能。通过遵循本计划，可以在JavaSTG引擎中实现一个具有闪亮银枪风格的自机，为玩家提供独特的游戏体验。