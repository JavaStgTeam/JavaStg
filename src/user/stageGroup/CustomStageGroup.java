package user.stageGroup;

import stg.core.GameWorld;
import stg.stage.StageGroup;
import stg.stage.StageGroupInfo;
import user.stage.TestStage;
/**
 * 自定义关卡组
 * @since 2026-02-26
 */
@StageGroupInfo(
    name = "自定义关卡组",
    description = "玩家自定义的关卡组",
    difficulty = StageGroup.Difficulty.NORMAL,
    order = 1
)
public class CustomStageGroup extends StageGroup {
    public CustomStageGroup(GameWorld gameWorld) {
        super("自定义关卡组", "玩家自定义的关卡组", StageGroup.Difficulty.NORMAL, gameWorld);
    }

    @Override
    protected void initStages() {
        addStage(new TestStage(1, "测试关卡", getGameWorld()));
    }
}
