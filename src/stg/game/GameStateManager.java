package stg.game;

/**
 * 游戏状态管理器 - 管理游戏的各种状态
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
    private int score = 0;
    private int lives = 3;
    private int spellCards = 2;
    private int maxScore = 10000;
    
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
     * 添加分数
     */
    public void addScore(int points) {
        score += points;
        if (score > maxScore) {
            maxScore = score;
        }
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
     * 重置游戏状�?     */
    public void reset() {
        score = 0;
        lives = 3;
        spellCards = 2;
        setState(State.PLAYING);
    }
    
    /**
     * 获取分数
     */
    public int getScore() { return score; }
    
    /**
     * 获取生命�?     */
    public int getLives() { return lives; }
    
    /**
     * 获取符卡�?     */
    public int getSpellCards() { return spellCards; }
    
    /**
     * 获取最高分�?     */
    public int getMaxScore() { return maxScore; }
    
    /**
     * 设置分数
     */
    public void setScore(int score) { this.score = score; }
    
    /**
     * 设置生命�?     */
    public void setLives(int lives) { this.lives = lives; }
    
    /**
     * 设置符卡�?     */
    public void setSpellCards(int spellCards) { this.spellCards = spellCards; }
    
    /**
     * 设置最高分�?     */
    public void setMaxScore(int maxScore) { this.maxScore = maxScore; }
}
