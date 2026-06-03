package user.stage;

import stg.core.GameWorld;
import stg.stage.Stage;
import stg.stage.StageCompletionCondition;
import stg.entity.base.Obj;
import user.enemy.__FairyEnemy;
import user.enemy.__MidFairyEnemy;
import user.enemy.Elf;

/**
 * 演示关卡1 - 敌机展示
 * 依次展示三种敌机：FairyEnemy、MidFairyEnemy、Elf
 * @since 2026-06-03
 */
public class __DemoStage1 extends Stage {

    private static final int MAX_FRAME = 1200;

    private int phase = 0;

    public __DemoStage1(int stageId, String stageName, GameWorld gameWorld) {
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
        phase = 0;
    }

    @Override
    protected void onStageStart() {
        phase = 0;
    }

    @Override
    protected void updateWaveLogic() {
        int frame = getCurrentFrame();

        if (frame == 1) {
            System.out.println("[DemoStage1] Phase 1: __FairyEnemy wave");
        }
        if (frame == 300) {
            System.out.println("[DemoStage1] Phase 2: __MidFairyEnemy wave");
        }
        if (frame == 600) {
            System.out.println("[DemoStage1] Phase 3: Elf wave");
        }

        if (frame < 300) {
            if (frame >= 30 && frame % 60 == 0) {
                float x = (float) (Math.random() * 400 - 200);
                __FairyEnemy fairy = Obj.create(__FairyEnemy.class, x, 250);
                addEnemy(fairy);
            }
        } else if (frame < 600) {
            if (frame >= 330 && frame % 90 == 0) {
                float x = (float) (Math.random() * 300 - 150);
                __MidFairyEnemy midFairy = Obj.create(__MidFairyEnemy.class, x, 250);
                addEnemy(midFairy);
            }
        } else {
            if (frame >= 630 && frame % 60 == 0) {
                Elf elf = Obj.create(Elf.class, 0, 150);
                elf.setVy(2.0f);
                addEnemy(elf);
            }
        }
    }
}