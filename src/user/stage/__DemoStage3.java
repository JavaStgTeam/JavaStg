package user.stage;

import stg.core.GameWorld;
import stg.stage.Stage;
import stg.stage.StageCompletionCondition;
import stg.entity.base.Obj;
import user.boss.__MinorikoBoss;
import user.enemy.__MidFairyEnemy;

/**
 * 演示关卡3 - Boss符卡展示
 * 展示 __MinorikoBoss 完整的符卡攻击模式（非符×2 + 符卡×2）
 * @since 2026-06-03
 */
public class __DemoStage3 extends Stage {

    private static final int MAX_FRAME = 3600;
    private boolean bossSpawned = false;

    public __DemoStage3(int stageId, String stageName, GameWorld gameWorld) {
        super(stageId, stageName, gameWorld);
        setCompletionCondition(new StageCompletionCondition() {
            @Override
            public boolean isCompleted(Stage stage) {
                return stage.getCurrentFrame() >= MAX_FRAME;
            }
        });
    }

    @Override
    public void load() {
        setLoaded();
    }

    @Override
    public Stage nextStage() {
        return null;
    }

    @Override
    protected void initStage() {
        bossSpawned = false;
    }

    @Override
    protected void onStageStart() {
        bossSpawned = false;
    }

    @Override
    protected void updateWaveLogic() {
        int frame = getCurrentFrame();

        if (frame >= 60 && frame < 420 && frame % 120 == 0) {
            float x = (float) (Math.random() * 200 - 100);
            __MidFairyEnemy midFairy = Obj.create(__MidFairyEnemy.class, x, 260);
            midFairy.setVy(-2.0f);
            addEnemy(midFairy);
        }

        if (frame == 480 && !bossSpawned) {
            System.out.println("[DemoStage3] __MinorikoBoss: "
                + "NonSpellcard1 -> Spellcard1(秋之符) -> NonSpellcard2 -> Spellcard2");
            __MinorikoBoss boss = Obj.create(__MinorikoBoss.class, 0, -100);
            addEnemy(boss);
            bossSpawned = true;
        }
    }
}