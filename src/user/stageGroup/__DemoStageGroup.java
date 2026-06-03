package user.stageGroup;

import stg.core.GameWorld;
import stg.stage.StageGroup;
import user.stage.__DemoStage1;
import user.stage.__DemoStage2;
import user.stage.__DemoStage3;

/**
 * 演示用关卡组 - 配合 DemoMain 直接启动
 * 按顺序展示：敌机 → 激光+Boss → 最终Boss符卡
 * @since 2026-06-03
 */
public class __DemoStageGroup extends StageGroup {

    public __DemoStageGroup(GameWorld gameWorld) {
        super("功能演示", "展示已实现的主要功能: 敌机/激光/Boss/符卡", Difficulty.NORMAL, gameWorld);
    }

    @Override
    protected void initStages() {
        addStage(new __DemoStage1(1, "敌机展示", getGameWorld()));
        addStage(new __DemoStage2(2, "激光与Boss展示", getGameWorld()));
        addStage(new __DemoStage3(3, "Boss符卡展示", getGameWorld()));
    }
}