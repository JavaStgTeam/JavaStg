package user.stageGroup;

import stg.game.stage.StageGroup;
import stg.game.stage.StageGroupInfo;
import stg.game.ui.GameCanvas;
import user.stage.TestStage;

/**
 * 自定义关卡组示例
 */
@StageGroupInfo(
    name = "自定义关卡组",
    description = "玩家自定义的关卡组",
    difficulty = StageGroup.Difficulty.NORMAL
)
public class CustomStageGroup extends StageGroup {
    public CustomStageGroup(GameCanvas gameCanvas) {
        super("自定义关卡组", "玩家自定义的关卡组", StageGroup.Difficulty.NORMAL, gameCanvas);
    }

    @Override
    protected void initStages() {
        // 添加测试关卡
        addStage(new TestStage(1, "测试关卡", getGameCanvas()));
    }
}
