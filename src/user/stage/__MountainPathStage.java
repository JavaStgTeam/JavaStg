package user.stage;

import stg.stage.Stage;
import stg.core.GameWorld;
import stg.entity.base.Obj;
import user.boss.__MinorikoBoss;
import user.enemy.__FairyEnemy;
import user.enemy.__MidFairyEnemy;
import user.enemy.Elf;

public class __MountainPathStage extends Stage {
    private int fairyCount = 0;
    private int midFairyCount = 0;
    private int elfCount = 0;
    private boolean hasSpawnedBoss = false;
    
    private static final int MAX_FAIRIES = 15;
    private static final int MAX_MID_FAIRIES = 5;

    public __MountainPathStage(int stageId, String stageName, GameWorld gameWorld) {
        super(stageId, stageName, gameWorld);
    }

    @Override
    protected void initStage() {
        fairyCount = 0;
        midFairyCount = 0;
        elfCount = 0;
        hasSpawnedBoss = false;
    }

    @Override
    protected void onStageStart() {
        fairyCount = 0;
        midFairyCount = 0;
        elfCount = 0;
        hasSpawnedBoss = false;
    }

    @Override
    protected void onStageEnd() {
    }

    @Override
    protected void updateWaveLogic() {
        int frame = getCurrentFrame();
        
        System.out.println("updateWaveLogic called, frame: " + frame + ", elfCount: " + elfCount);
        
        if (frame >= 60 && frame % 90 == 0 && fairyCount < MAX_FAIRIES) {
            float x = (float) (Math.random() * 600 - 300);
            __FairyEnemy fairy = Obj.create(__FairyEnemy.class, x, -400);
            addEnemy(fairy);
            fairyCount++;
            System.out.println("生成Fairy敌人: " + fairyCount);
        }
        
        if (frame >= 300 && frame % 180 == 0 && midFairyCount < MAX_MID_FAIRIES) {
            float x = (float) (Math.random() * 400 - 200);
            __MidFairyEnemy midFairy = Obj.create(__MidFairyEnemy.class, x, -400);
            addEnemy(midFairy);
            midFairyCount++;
            System.out.println("生成MidFairy敌人: " + midFairyCount);
        }
        
        // 每隔1秒（60帧）在屏幕正中心生成一个Elf敌人
        if (frame >= 60 && frame % 60 == 0) {
            Elf elf = Obj.create(Elf.class, 0, 0);
            // 设置Elf敌人向上移动
            elf.setVy(3.0f);
            addEnemy(elf);
            elfCount++;
            System.out.println("生成Elf敌人: " + elfCount);
        }
        
        if (frame >= 900 && !hasSpawnedBoss) {
            __MinorikoBoss boss = Obj.create(__MinorikoBoss.class, 0, -300);
            addEnemy(boss);
            hasSpawnedBoss = true;
            System.out.println("生成Boss敌人");
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
