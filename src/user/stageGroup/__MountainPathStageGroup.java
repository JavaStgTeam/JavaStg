package user.stageGroup;

import stg.stage.StageGroup;
import stg.stage.StageGroupInfo;
import stg.core.GameWorld;
import user.stage.__MountainPathStage;

@StageGroupInfo(
    name = "东方风神录一面",
    description = "秋静叶的山道",
    difficulty = StageGroup.Difficulty.NORMAL
)
public class __MountainPathStageGroup extends StageGroup {
    public __MountainPathStageGroup(GameWorld gameWorld) {
        super("东方风神录一面", "秋静叶的山道", StageGroup.Difficulty.NORMAL, gameWorld);
    }

    @Override
    protected void initStages() {
        addStage(new __MountainPathStage(1, "山道", getGameWorld()));
    }
}
