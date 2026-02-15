package user.stage;

import stg.game.stage.Stage;
import stg.game.ui.GameCanvas;
import user.boss.__MinorikoBoss;
import user.enemy.__FairyEnemy;
import user.enemy.__MidFairyEnemy;

public class __MountainPathStage extends Stage {
    private int fairyCount = 0;
    private int midFairyCount = 0;
    private boolean hasSpawnedBoss = false;
    
    private static final int MAX_FAIRIES = 15;
    private static final int MAX_MID_FAIRIES = 5;

    public __MountainPathStage(int stageId, String stageName, GameCanvas gameCanvas) {
        super(stageId, stageName, gameCanvas);
    }

    @Override
    protected void initStage() {
        fairyCount = 0;
        midFairyCount = 0;
        hasSpawnedBoss = false;
    }

    @Override
    protected void onStageStart() {
        fairyCount = 0;
        midFairyCount = 0;
        hasSpawnedBoss = false;
    }

    @Override
    protected void onStageEnd() {
    }

    @Override
    protected void updateWaveLogic() {
        int frame = getCurrentFrame();
        
        if (frame >= 60 && frame % 90 == 0 && fairyCount < MAX_FAIRIES) {
            float x = (float) (Math.random() * 600 - 300);
            __FairyEnemy fairy = new __FairyEnemy(x, -400, getGameCanvas());
            addEnemy(fairy);
            fairyCount++;
        }
        
        if (frame >= 300 && frame % 180 == 0 && midFairyCount < MAX_MID_FAIRIES) {
            float x = (float) (Math.random() * 400 - 200);
            __MidFairyEnemy midFairy = new __MidFairyEnemy(x, -400, getGameCanvas());
            addEnemy(midFairy);
            midFairyCount++;
        }
        
        if (frame >= 900 && !hasSpawnedBoss) {
            __MinorikoBoss boss = new __MinorikoBoss(0, -300);
            addEnemy(boss);
            hasSpawnedBoss = true;
        }
    }

    @Override
    public Stage nextStage() {
        return null;
    }

    @Override
    public void load() {
        setLoaded();
    }
}
