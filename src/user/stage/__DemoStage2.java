package user.stage;

import java.awt.Color;

import stg.core.GameWorld;
import stg.stage.Stage;
import stg.stage.StageCompletionCondition;
import stg.entity.base.Obj;
import stg.entity.laser.LaserColor;
import user.boss.__StandbyBoss;
import user.laser.TestLaser;

/**
 * 演示关卡2 - 激光与Boss展示
 * 展示激光渲染功能和 __StandbyBoss
 * @since 2026-06-03
 */
public class __DemoStage2 extends Stage {

    private static final int MAX_FRAME = 2400;
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
    protected void initStage() {
        laserSpawned = false;
        bossSpawned = false;
    }

    @Override
    protected void onStageStart() {
        laserSpawned = false;
        bossSpawned = false;
    }

    @Override
    protected void updateWaveLogic() {
        int frame = getCurrentFrame();

        if (frame == 60 && !laserSpawned && getGameWorld() != null) {
            System.out.println("[DemoStage2] Phase 1: Laser showcase");
            spawnTestLasers();
            laserSpawned = true;
        }

        if (frame == 600 && !bossSpawned) {
            System.out.println("[DemoStage2] Phase 2: __StandbyBoss");
            __StandbyBoss boss = Obj.create(__StandbyBoss.class, 0, -200);
            addEnemy(boss);
            bossSpawned = true;
        }
    }

    private void spawnTestLasers() {
        GameWorld world = getGameWorld();
        if (world == null) return;

        float startX = 0;
        float startY = 0;
        float length = 350;
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
        System.out.println("[DemoStage2] Spawned " + laserColors.length + " lasers");
    }
}