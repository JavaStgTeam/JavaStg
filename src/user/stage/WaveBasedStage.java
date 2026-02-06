package user.stage;

import stg.game.stage.Stage;
import stg.game.ui.GameCanvas;

/**
 * 基于波次的关卡类 - 简单的占位符实现
 * @since 2026-02-06
 */
public class WaveBasedStage extends Stage {

    /**
     * 构造函数
     * @param stageId 关卡ID
     * @param stageName 关卡名称
     * @param gameCanvas 游戏画布引用
     */
    public WaveBasedStage(int stageId, String stageName, GameCanvas gameCanvas) {
        super(stageId, stageName, gameCanvas);
    }

    @Override
    public void load() {
        setLoaded();
    }

    @Override
    public Stage nextStage() {
        return null;
    }
}