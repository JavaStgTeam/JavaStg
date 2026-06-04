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
 * 三种敌机依次从屏幕顶部进入视野，向下飞行穿过画面
 * @since 2026-06-03
 */
public class __DemoStage1 extends Stage {

    private static final int MAX_FRAME = 1200;

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
    protected void updateWaveLogic() {
        int frame = getCurrentFrame();

        if (frame == 30) {
            System.out.println("[DemoStage1] Phase 1: __FairyEnemy (size=20, hp=100)");
        }
        if (frame == 330) {
            System.out.println("[DemoStage1] Phase 2: __MidFairyEnemy (size=30, hp=300, 5way spread)");
        }
        if (frame == 630) {
            System.out.println("[DemoStage1] Phase 3: Elf (size=40, hp=150, texture render)");
        }

        if (frame < 300) {
            if (frame >= 30 && frame % 60 == 0) {
                float x = (float) (Math.random() * 280 - 140);
                __FairyEnemy fairy = Obj.create(__FairyEnemy.class, x, 260);
                fairy.setVy(-3.0f);
                addEnemy(fairy);
            }
        } else if (frame < 600) {
            if (frame >= 330 && frame % 90 == 0) {
                float x = (float) (Math.random() * 260 - 130);
                __MidFairyEnemy midFairy = Obj.create(__MidFairyEnemy.class, x, 260);
                midFairy.setVy(-2.0f);
                addEnemy(midFairy);
            }
        } else {
            if (frame >= 630 && frame % 60 == 0) {
                float x = (float) (Math.random() * 200 - 100);
                Elf elf = Obj.create(Elf.class, x, 280);
                elf.setVy(-2.0f);
                addEnemy(elf);
            }
        }
    }
}
