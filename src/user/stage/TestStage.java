package user.stage;

import stg.game.stage.Stage;
import stg.game.ui.GameCanvas;

/**
 * 测试关卡
 */
public class TestStage extends Stage {
    public TestStage(int stageId, String stageName, GameCanvas gameCanvas) {
        super(stageId, stageName, gameCanvas);
    }

    @Override
    protected void initStage() {
        // 初始化关卡
    }

    @Override
    protected void onStageStart() {
        // 关卡开始逻辑
    }

    @Override
    protected void onStageEnd() {
        // 关卡结束逻辑
    }

    @Override
    protected void updateWaveLogic() {
        // 每60帧生成一个敌人
        if (getCurrentFrame() % 60 == 0) {
            // 生成一个敌人
            System.out.println("生成敌人");
        }
    }

    @Override
    public Stage nextStage() {
        // 返回下一关，这里返回null表示没有下一关
        return null;
    }

    @Override
    public void load() {
        // 加载关卡资源
        setLoaded();
    }
}
