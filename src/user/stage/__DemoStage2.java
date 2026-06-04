package user.stage;

import java.awt.Color;

import stg.core.GameWorld;
import stg.stage.Stage;
import stg.stage.StageCompletionCondition;
import stg.entity.base.Obj;
import stg.entity.laser.LaserColor;
import user.boss.__StandbyBoss;
import user.enemy.__FairyEnemy;
import user.laser.TestLaser;

/**
 * 演示关卡2 - 激光与Boss展示
 * 前半段展示9色放射激光，后半段展示 __StandbyBoss（3符卡）
 * @since 2026-06-03
 */
public class __DemoStage2 extends Stage {

    private static final int MAX_FRAME = 3000;
    private boolean laserSpawned = false;
    private boolean bossSpawned = false;

    public __DemoStage2(int stageId, String stageName, GameWorld gameWorld) {
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

        if (frame == 60 && !laserSpawned && getGameWorld() != null) {
            System.out.println("[DemoStage2] Phase 1: Laser showcase (9 colors, radial)");
            spawnTestLasers();
            laserSpawned = true;
        }

        if (frame >= 180 && frame < 600 && frame % 90 == 0) {
            float x = (float) (Math.random() * 200 - 100);
            __FairyEnemy fairy = Obj.create(__FairyEnemy.class, x, 260);
            fairy.setVy(-3.0f);
            addEnemy(fairy);
        }

        if (frame == 900 && !bossSpawned) {
            System.out.println("[DemoStage2] Phase 2: __StandbyBoss (3 spellcards)");
            __StandbyBoss boss = Obj.create(__StandbyBoss.class, 0, -120);
            addEnemy(boss);
            bossSpawned = true;
        }
    }

    private void spawnTestLasers() {
        GameWorld world = getGameWorld();
        if (world == null) return;

        float startX = 0;
        float startY = 0;
        float length = 250;
        float width = 28;

        LaserColor[] laserColors = LaserColor.values();
        Color[] colors = {
            Color.RED,
            new Color(128, 0, 128),
            new Color(0, 0, 128),
            Color.BLUE,
            Color.CYAN,
            Color.GREEN,
            Color.YELLOW,
            Color.ORANGE,
            Color.GRAY
        };

        for (int i = 0; i < laserColors.length; i++) {
            float angle = (float) Math.PI * 2 / laserColors.length * i;
            TestLaser laser = new TestLaser(startX, startY, angle, length, width, colors[i], laserColors[i]);
            world.addObject(laser);
        }
    }
}