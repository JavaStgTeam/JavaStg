package stg.game.stage;

import java.util.List;
import stg.game.enemy.Enemy;
import stg.game.ui.GameCanvas;
import user.stage.StageState;

/**
 * 关卡类 - 管理单个关卡的逻辑
 * @since 2026-01-30
 */
public abstract class Stage {
    private final String stageName;
    private final int stageId;
    private StageState state;
    private final GameCanvas gameCanvas;
    private StageCompletionCondition completionCondition;
    
    // 波次管理相关字段
    protected int currentFrame = 0;

    /**
     * 构造函数
     * @param stageId 关卡ID
     * @param stageName 关卡名称
     * @param gameCanvas 游戏画布引用
     */
    public Stage(int stageId, String stageName, GameCanvas gameCanvas) {
        this.stageId = stageId;
        this.stageName = stageName;
        this.gameCanvas = gameCanvas;
        this.state = StageState.CREATED;
        initStage();
        // 移除自动加载和开始逻辑，由调用者显式调用load()和start()
    }

    /**
     * 初始化关卡
     * 由子类重写，初始化关卡相关资源
     */
    protected void initStage() {
        // 子类可以重写此方法初始化关卡
    }

    /**
     * 开始关卡
     * 由调用者显式调用，开始关卡逻辑
     */
    public void start() {
        if (state == StageState.LOADED) {
            state = StageState.STARTED;
            onStageStart();
        }
    }

    /**
     * 结束关卡
     * 由调用者显式调用，结束关卡逻辑
     */
    public void end() {
        if (state == StageState.STARTED) {
            state = StageState.COMPLETED;
            onStageEnd();
        }
    }

    /**
     * 检查关卡是否激活
     * @return 是否激活
     */
    public boolean isActive() {
        return state == StageState.STARTED;
    }

    /**
     * 跳转到下一关卡
     * @return 下一关的Stage对象
     */
    public abstract Stage nextStage();

    /**
     * 加载关卡
     */
    public abstract void load();

    /**
     * 设置关卡状态为LOADED
     * 由子类在load()方法完成后调用，标记关卡为已加载
     */
    protected void setLoaded() {
        this.state = StageState.LOADED;
    }

    /**
     * 清理关卡资源
     */
    public void cleanup() {
        if (state != StageState.CLEANED_UP) {
            // 敌人清理逻辑由GameWorld负责
            state = StageState.CLEANED_UP;
        }
    }

    /**
     * 添加敌人到关卡
     * @param enemy 敌人对象
     */
    public void addEnemy(Enemy enemy) {
        if (enemy != null) {
            if (gameCanvas != null) {
                // 暂时注释掉，因为 getWorld() 返回 Object 类型，可能没有 addEnemy 方法
                // GameCanvas 可能也没有 addEnemy 方法
                // Object world = gameCanvas.getWorld();
                // if (world != null) {
                //     // 这里需要类型转换，但是暂时注释掉
                //     // ((GameWorld)world).addEnemy(enemy);
                // }
                // gameCanvas.addEnemy(enemy);
            }
        }
    }

    /**
     * 移除敌人
     * @param enemy 敌人对象
     */
    public void removeEnemy(Enemy enemy) {
        // 敌人移除逻辑由GameWorld负责
    }

    /**
     * 检查关卡是否完成
     * @return 是否完成
     */
    public boolean isCompleted() {
        return state == StageState.COMPLETED;
    }

    /**
     * 检查关卡是否已开始
     * @return 是否已开始
     */
    public boolean isStarted() {
        return state == StageState.STARTED;
    }

    /**
     * 获取关卡名称
     * @return 关卡名称
     */
    public String getStageName() {
        return stageName;
    }

    /**
     * 获取关卡ID
     * @return 关卡ID
     */
    public int getStageId() {
        return stageId;
    }

    /**
     * 获取当前关卡的敌人列表
     * @return 敌人列表（不可修改） 
     */
    public List<Enemy> getEnemies() {
        if (gameCanvas != null) {
            // 暂时注释掉，因为 getWorld() 返回 Object 类型，可能没有 getEnemies 方法
            // Object world = gameCanvas.getWorld();
            // if (world != null) {
            //     // 这里需要类型转换，但是暂时注释掉
            //     // return ((GameWorld)world).getEnemies();
            // }
        }
        return java.util.Collections.emptyList();
    }

    /**
     * 获取游戏画布
     * @return 游戏画布
     */
    protected GameCanvas getGameCanvas() {
        return gameCanvas;
    }

    /**
     * 关卡开始时调用
     */
    protected void onStageStart() {
        // 子类可以重写此方法处理关卡开始逻辑
    }

    /**
     * 关卡结束时调用
     */
    protected void onStageEnd() {
        // 子类可以重写此方法处理关卡结束逻辑
    }

    /**
     * 设置关卡完成条件
     * @param condition 关卡完成条件
     */
    public void setCompletionCondition(StageCompletionCondition condition) {
        this.completionCondition = condition;
    }

    /**
     * 检查关卡完成条件
     */
    protected void checkCompletion() {
        if (completionCondition != null && completionCondition.isCompleted(this)) {
            end();
        }
    }

    /**
     * 更新关卡逻辑
     */
    public void update() {
        if (isActive()) {
            currentFrame++;
            updateWaveLogic();
        }
        checkCompletion();
    }

    /**
     * 更新波次逻辑
     * 子类可以重写此方法实现具体的波次管理
     */
    protected void updateWaveLogic() {
        // 子类可以重写此方法实现具体的波次管理
    }

    /**
     * 获取当前帧数
     * @return 当前帧数
     */
    public int getCurrentFrame() {
        return currentFrame;
    }

    /**
     * 重置关卡
     */
    public void reset() {
        this.state = StageState.CREATED;
        initStage();
    }
}
