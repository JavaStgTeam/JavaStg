package user.stage;

import stg.core.GameWorld;
import stg.stage.Stage;
import stg.stage.StageCompletionCondition;
import stg.entity.base.Obj;
import user.boss.__MinorikoBoss;

/**
 * 演示关卡3 - Boss符卡展示
 * 展示 __MinorikoBoss 及其完整的符卡攻击模式
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

        if (frame == 60 && !bossSpawned) {
            System.out.println("[DemoStage3] __MinorikoBoss with spellcards: "
                + "NonSpellcard1 -> Spellcard1 -> NonSpellcard2 -> Spellcard2");
            __MinorikoBoss boss = Obj.create(__MinorikoBoss.class, 0, -250);
            addEnemy(boss);
            bossSpawned = true;
        }
    }
}