package user.stageGroup;

import stg.stage.StageGroup;
import stg.stage.StageGroupInfo;
import stg.ui.GameCanvas;

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
        // 自定义关卡组 - 暂无关卡
    }
}
