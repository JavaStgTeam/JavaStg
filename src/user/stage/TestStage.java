package user.stage;

import stg.stage.Stage;
import stg.ui.GameCanvas;
import stg.entity.base.Obj;
import user.boss.TestBoss;
import user.enemy.DefaultEnemy;

/**
 * 测试关卡
 */
public class TestStage extends Stage {
    private int enemyCount = 0; // 已生成的敌人数量
    private static final int MAX_ENEMIES = 10; // 最大敌人数量
    private boolean hasSpawnedBoss = false; // 是否已经生成Boss

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
        hasSpawnedBoss = false; // 重置Boss生成状态
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
            DefaultEnemy enemy = Obj.create(DefaultEnemy.class, 0, 100);
            addEnemy(enemy);
            System.out.println("生成敌人，位置: (0, 100)，总数: " + (enemyCount + 1));
            enemyCount++;
        }
        
        // 关卡开始12秒时生成Boss（假设60帧/秒）
        if (getCurrentFrame() == 720 && !hasSpawnedBoss) {
            // 生成TestBoss，位置在(0, 150)，屏幕上半部分
            TestBoss boss = Obj.create(TestBoss.class, 0, 150);
            addEnemy(boss);
            System.out.println("生成Boss，位置: (0, 150)");
            hasSpawnedBoss = true;
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
