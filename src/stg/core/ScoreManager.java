package stg.core;

/**
 * 分数管理器 - 管理游戏分数、最高分和擦弹计数
 * @since 2026-06-02
 * @author JavaSTG Team
 */
public class ScoreManager {
    private int score = 0;
    private int highScore = 0;
    private int graze = 0;

    /**
     * 构造分数管理器，初始化所有计数值为0
     */
    public ScoreManager() {
        this.score = 0;
        this.highScore = 0;
        this.graze = 0;
    }

    /**
     * 添加分数，若超过最高分则自动更新
     * @param points 要添加的分数
     */
    public void addScore(int points) {
        score += points;
        if (score > highScore) {
            highScore = score;
        }
    }

    /**
     * 添加擦弹计数
     * @param count 要添加的擦弹数
     */
    public void addGraze(int count) {
        graze += count;
    }

    /**
     * 重置分数和擦弹计数为0，保留最高分
     */
    public void reset() {
        score = 0;
        graze = 0;
    }

    /**
     * 获取当前分数
     * @return 当前分数
     */
    public int getScore() {
        return score;
    }

    /**
     * 获取最高分数
     * @return 最高分数
     */
    public int getHighScore() {
        return highScore;
    }

    /**
     * 获取擦弹计数
     * @return 擦弹计数
     */
    public int getGraze() {
        return graze;
    }

    /**
     * 设置当前分数
     * @param score 要设置的分数值
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * 设置最高分数
     * @param highScore 要设置的最高分数值
     */
    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    /**
     * 设置擦弹计数
     * @param graze 要设置的擦弹计数值
     */
    public void setGraze(int graze) {
        this.graze = graze;
    }

    /**
     * 格式化分数，添加千位分隔符
     * @param score 要格式化的分数
     * @return 格式化后的分数字符串，如 "1,234,567"
     */
    public static String formatScore(int score) {
        String num = String.valueOf(score);
        StringBuilder sb = new StringBuilder();
        int len = num.length();
        for (int i = 0; i < len; i++) {
            if (i > 0 && (len - i) % 3 == 0) {
                sb.append(',');
            }
            sb.append(num.charAt(i));
        }
        return sb.toString();
    }
}