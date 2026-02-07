package stg.game.stage;

/**
 * 关卡完成条件接口 - stg.game包专用
 */
public interface StageCompletionCondition {
    /**
     * 检查关卡是否完成
     * @param stage 关卡对象
     * @return 是否完成
     */
    boolean isCompleted(Stage stage);
}

