package stg.ui;

import java.awt.*;
import stg.service.render.IRenderer;

/**
 * 游戏状态面板 - 显示在右侧面板的游戏信息
 * @since 2026-01-24
 * */
public class GameStatusPanel {
	private int score;
	private int highScore;
	private int lives;
	private int spellCards;
	private int maxScore;
	
	public GameStatusPanel() {
		this.score = 0;
		this.highScore = 0;
		this.lives = -1;
		this.spellCards = 0;
		this.maxScore = 10000;
	}
	
	private String formatScore(int score) {
		return String.format("%010d", score);
	}
	
	private String formatLives(int lives) {
		if (lives < 0) {
			return "--";
		}
		return String.valueOf(lives);
	}
	
	private String formatSpellCards(int spellCards) {
		return String.valueOf(spellCards);
	}
	
	/**
	 * 使用OpenGL渲染器绘制游戏状态面板
	 * @param renderer OpenGL渲染器
	 * @param x 面板X坐标
	 * @param y 面板Y坐标
	 * @param width 面板宽度
	 * @param height 面板高度
	 */
	public void render(IRenderer renderer, int x, int y, int width, int height) {
		// 绘制背景
		renderer.setColor(new Color(30, 30, 40));
		renderer.drawRect(x, y, width, height, new Color(30, 30, 40));
		
		// 字体设置
		Font labelFont = new Font("Microsoft YaHei", Font.PLAIN, 14);
		Font scoreFont = new Font("Arial", Font.BOLD, 28);
		Font valueFont = new Font("Arial", Font.BOLD, 32);
		
		Color labelColor = new Color(200, 200, 220);
		Color valueColor = new Color(255, 255, 255);
		
		// 绘制高分
		int highScoreY = y + 30;
		renderer.setFont(labelFont);
		renderer.setColor(labelColor);
		renderer.drawText("HighScore", x + 25, highScoreY, labelFont, labelColor);
		renderer.setFont(scoreFont);
		renderer.setColor(valueColor);
		renderer.drawText(formatScore(highScore), x + 25, highScoreY + 35, scoreFont, valueColor);
		
		// 绘制当前分数
		int scoreY = highScoreY + 75;
		renderer.setFont(labelFont);
		renderer.setColor(labelColor);
		renderer.drawText("Score", x + 25, scoreY, labelFont, labelColor);
		renderer.setFont(scoreFont);
		renderer.setColor(valueColor);
		renderer.drawText(formatScore(score), x + 25, scoreY + 35, scoreFont, valueColor);
		
		// 绘制玩家生命
		int livesY = scoreY + 85;
		renderer.setFont(labelFont);
		renderer.setColor(labelColor);
		renderer.drawText("Player", x + 25, livesY, labelFont, labelColor);
		renderer.setFont(valueFont);
		renderer.setColor(valueColor);
		renderer.drawText(formatLives(lives), x + 25, livesY + 35, valueFont, valueColor);
		
		// 绘制符卡数量
		int spellCardsY = livesY + 75;
		renderer.setFont(labelFont);
		renderer.setColor(labelColor);
		renderer.drawText("Spell", x + 25, spellCardsY, labelFont, labelColor);
		renderer.setFont(valueFont);
		renderer.setColor(valueColor);
		renderer.drawText(formatSpellCards(spellCards), x + 25, spellCardsY + 35, valueFont, valueColor);
		
		// 绘制擦弹数
		int grazeY = spellCardsY + 85;
		renderer.setFont(labelFont);
		renderer.setColor(labelColor);
		renderer.drawText("Graze", x + 25, grazeY, labelFont, labelColor);
		renderer.setFont(scoreFont);
		renderer.setColor(valueColor);
		renderer.drawText(formatScore(maxScore), x + 25, grazeY + 35, scoreFont, valueColor);
	}
	
	public void setScore(int score) {
		this.score = score;
		if (score > highScore) {
			highScore = score;
		}
	}
	
	public void addScore(int points) {
		setScore(score + points);
	}
	
	public void setHighScore(int highScore) {
		this.highScore = highScore;
	}
	
	public void setLives(int lives) {
		this.lives = lives;
	}
	
	public void loseLife() {
		if (lives > 0) {
			lives--;
		}
	}
	
	public void setSpellCards(int spellCards) {
		this.spellCards = spellCards;
	}
	
	public void useSpellCard() {
		if (spellCards > 0) {
			spellCards--;
		}
	}
	
	public void setMaxScore(int maxScore) {
		this.maxScore = maxScore;
	}
	
	public int getScore() {
		return score;
	}
	
	public int getHighScore() {
		return highScore;
	}
	
	public int getLives() {
		return lives;
	}
	
	public int getSpellCards() {
		return spellCards;
	}
	
	public int getMaxScore() {
		return maxScore;
	}
}

