package stg.core;

/**
 * 游戏状态管理器 - 管理游戏的各种状态
 * @since 2026-02-17
 * @author JavaSTG Team
 */
public class GameStateManager {
    /**
     * 游戏状态枚举
     */
    public enum State {
        TITLE,      // 标题界面
        PLAYING,    // 游戏进行中
        PAUSED,     // 暂停
        GAME_OVER   // 游戏结束
    }
    
    private State currentState = State.PLAYING;
    private int lives = 3;
    private int spellCards = 2;
    
    /**
     * 设置游戏状态
     */
    public void setState(State state) {
        this.currentState = state;
        onStateChanged(state);
    }
    
    /**
     * 获取当前游戏状态
     */
    public State getState() {
        return currentState;
    }
    
    /**
     * 检查是否暂停
     */
    public boolean isPaused() {
        return currentState == State.PAUSED;
    }
    
    /**
     * 检查是否在游戏进行中
     */
    public boolean isPlaying() {
        return currentState == State.PLAYING || currentState == State.PAUSED;
    }
    
    /**
     * 切换暂停状态
     */
    public void togglePause() {
        if (currentState == State.PLAYING) {
            setState(State.PAUSED);
        } else if (currentState == State.PAUSED) {
            setState(State.PLAYING);
        }
    }
    
    /**
     * 状态变更回调
     */
    private void onStateChanged(State newState) {
        System.out.println("游戏状态变更为: " + newState);
    }
    
    /**
     * 失去生命
     */
    public void loseLife() {
        lives--;
        if (lives < 0) {
            lives = 0;
            setState(State.GAME_OVER);
        }
    }
    
    /**
     * 获得生命
     */
    public void gainLife() {
        lives++;
    }
    
    /**
     * 使用符卡
     */
    public void useSpellCard() {
        if (spellCards > 0) {
            spellCards--;
        }
    }
    
    /**
     * 获得符卡
     */
    public void gainSpellCard() {
        spellCards++;
    }
    
    /**
     * 重置游戏状态
     */
    public void reset() {
        lives = 3;
        spellCards = 2;
        setState(State.PLAYING);
    }
    
    /**
     * 获取生命值
     */
    public int getLives() { return lives; }
    
    /**
     * 获取符卡数
     */
    public int getSpellCards() { return spellCards; }
    
    /**
     * 设置生命值
     */
    public void setLives(int lives) { this.lives = lives; }
    
    /**
     * 设置符卡数
     */
    public void setSpellCards(int spellCards) { this.spellCards = spellCards; }
}