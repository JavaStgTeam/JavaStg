package user.stageGroup;

import stg.core.GameWorld;
import stg.stage.StageGroup;
import stg.stage.StageGroupInfo;
import user.stage.__MountainPathStage;

@StageGroupInfo(
    name = "测试关卡",
    description = "秋静叶的山道",
    difficulty = StageGroup.Difficulty.NORMAL
)
public class __MountainPathStageGroup extends StageGroup {
    public __MountainPathStageGroup(GameWorld gameWorld) {
        super("测试关卡", "秋静叶的山道", StageGroup.Difficulty.NORMAL, gameWorld);
    }

    @Override
    protected void initStages() {
        addStage(new __MountainPathStage(1, "山道", getGameWorld()));
    }
}
