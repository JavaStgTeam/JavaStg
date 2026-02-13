package user.stage;

import stg.game.stage.Stage;
import stg.game.ui.GameCanvas;
import user.enemy.DefaultEnemy;

/**
 * 测试关卡
 */
public class TestStage extends Stage {
    private int enemyCount = 0; // 已生成的敌人数量
    private static final int MAX_ENEMIES = 10; // 最大敌人数量

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
        enemyCount = 0; // 重置敌人计数器
    }

    @Override
    protected void onStageEnd() {
        // 关卡结束逻辑
    }

    @Override
    protected void updateWaveLogic() {
        // 每60帧生成一个敌人，最多生成10个
        if (getCurrentFrame() % 60 == 0 && enemyCount < MAX_ENEMIES) {
            // 生成一个敌人，位置在(0, 100)
            DefaultEnemy enemy = new DefaultEnemy(0, 100);
            getGameCanvas().getGameWorld().addEnemy(enemy);
            System.out.println("生成敌人，位置: (0, 100)，总数: " + (enemyCount + 1));
            enemyCount++;
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
