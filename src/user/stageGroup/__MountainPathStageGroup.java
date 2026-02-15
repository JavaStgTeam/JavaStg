package user.stageGroup;

import stg.game.stage.StageGroup;
import stg.game.stage.StageGroupInfo;
import stg.game.ui.GameCanvas;
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
