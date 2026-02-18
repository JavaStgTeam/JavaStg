package user.stageGroup;

import stg.stage.StageGroup;
import stg.stage.StageGroupInfo;
import stg.ui.GameCanvas;
import user.stage.__MountainPathStage;

@StageGroupInfo(
    name = "东方风神录一面",
    description = "秋静叶的山道",
    difficulty = StageGroup.Difficulty.NORMAL
)
public class __MountainPathStageGroup extends StageGroup {
    public __MountainPathStageGroup(GameCanvas gameCanvas) {
        super("东方风神录一面", "秋静叶的山道", StageGroup.Difficulty.NORMAL, gameCanvas);
    }

    @Override
    protected void initStages() {
        addStage(new __MountainPathStage(1, "山道", getGameCanvas()));
    }
}
