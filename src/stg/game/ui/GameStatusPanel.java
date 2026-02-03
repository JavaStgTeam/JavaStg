package stg.game.ui;

import java.awt.*;
import javax.swing.*;

/**
 * 游戏状态面板 - 显示在右侧面板的游戏信息
 * @since 2026-01-24
 * */
public class GameStatusPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private int score;
	private int highScore;
	private int lives;
	private int spellCards;
	private int maxScore;
	
	private JLabel scoreLabel;
	private JLabel highScoreLabel;
	private JLabel livesLabel;
	private JLabel spellCardsLabel;
	private JLabel maxScoreLabel;
	
	public GameStatusPanel() {
		this.score = 0;
		this.highScore = 0;
		this.lives = -1;
		this.spellCards = 0;
		this.maxScore = 10000;
		
		initializeUI();
	}
	
	private void initializeUI() {
		setLayout(new BorderLayout());
		setBackground(new Color(30, 30, 40));
		setBorder(BorderFactory.createEmptyBorder(30, 25, 30, 25));
		
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.setBackground(new Color(30, 30, 40));
		
		Font labelFont = new Font("Microsoft YaHei", Font.PLAIN, 14);
		Font scoreFont = new Font("Arial", Font.BOLD, 28);
		Font valueFont = new Font("Arial", Font.BOLD, 32);
		
		Color labelColor = new Color(200, 200, 220);
		Color valueColor = new Color(255,255, 255);
		
		JPanel highScorePanel = new JPanel();
		highScorePanel.setLayout(new BoxLayout(highScorePanel, BoxLayout.Y_AXIS));
		highScorePanel.setBackground(new Color(30, 30, 40));
		highScorePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel highScoreTitle = new JLabel("HighScore");
		highScoreTitle.setFont(labelFont);
		highScoreTitle.setForeground(labelColor);
		highScoreTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
		highScoreLabel = new JLabel(formatScore(highScore));
		highScoreLabel.setFont(scoreFont);
		highScoreLabel.setForeground(valueColor);
		highScoreLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		highScorePanel.add(highScoreTitle);
		highScorePanel.add(Box.createVerticalStrut(5));
		highScorePanel.add(highScoreLabel);
		
		JPanel scorePanel = new JPanel();
		scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));
		scorePanel.setBackground(new Color(30, 30, 40));
		scorePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel scoreTitle = new JLabel("Score");
		scoreTitle.setFont(labelFont);
		scoreTitle.setForeground(labelColor);
		scoreTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
		scoreLabel = new JLabel(formatScore(score));
		scoreLabel.setFont(scoreFont);
		scoreLabel.setForeground(valueColor);
		scoreLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		scorePanel.add(scoreTitle);
		scorePanel.add(Box.createVerticalStrut(5));
		scorePanel.add(scoreLabel);
		
		JPanel livesPanel = new JPanel();
		livesPanel.setLayout(new BoxLayout(livesPanel, BoxLayout.Y_AXIS));
		livesPanel.setBackground(new Color(30, 30, 40));
		livesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel livesTitle = new JLabel("Player");
		livesTitle.setFont(labelFont);
		livesTitle.setForeground(labelColor);
		livesTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
		livesLabel = new JLabel(formatLives(lives));
		livesLabel.setFont(valueFont);
		livesLabel.setForeground(valueColor);
		livesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		livesPanel.add(livesTitle);
		livesPanel.add(Box.createVerticalStrut(5));
		livesPanel.add(livesLabel);
		
		JPanel spellCardsPanel = new JPanel();
		spellCardsPanel.setLayout(new BoxLayout(spellCardsPanel, BoxLayout.Y_AXIS));
		spellCardsPanel.setBackground(new Color(30, 30, 40));
		spellCardsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel spellCardsTitle = new JLabel("Spell");
		spellCardsTitle.setFont(labelFont);
		spellCardsTitle.setForeground(labelColor);
		spellCardsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
		spellCardsLabel = new JLabel(formatSpellCards(spellCards));
		spellCardsLabel.setFont(valueFont);
		spellCardsLabel.setForeground(valueColor);
		spellCardsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		spellCardsPanel.add(spellCardsTitle);
		spellCardsPanel.add(Box.createVerticalStrut(5));
		spellCardsPanel.add(spellCardsLabel);
		
		JPanel maxScorePanel = new JPanel();
		maxScorePanel.setLayout(new BoxLayout(maxScorePanel, BoxLayout.Y_AXIS));
		maxScorePanel.setBackground(new Color(30, 30, 40));
		maxScorePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel maxScoreTitle = new JLabel("Graze");
		maxScoreTitle.setFont(labelFont);
		maxScoreTitle.setForeground(labelColor);
		maxScoreTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
		maxScoreLabel = new JLabel(formatScore(maxScore));
		maxScoreLabel.setFont(scoreFont);
		maxScoreLabel.setForeground(valueColor);
		maxScoreLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		maxScorePanel.add(maxScoreTitle);
		maxScorePanel.add(Box.createVerticalStrut(5));
		maxScorePanel.add(maxScoreLabel);
		
		contentPanel.add(highScorePanel);
		contentPanel.add(Box.createVerticalStrut(25));
		contentPanel.add(scorePanel);
		contentPanel.add(Box.createVerticalStrut(35));
		contentPanel.add(livesPanel);
		contentPanel.add(Box.createVerticalStrut(25));
		contentPanel.add(spellCardsPanel);
		contentPanel.add(Box.createVerticalStrut(35));
		contentPanel.add(maxScorePanel);
		contentPanel.add(Box.createVerticalGlue());
		
		add(contentPanel, BorderLayout.CENTER);
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
	
	public void setScore(int score) {
		this.score = score;
		if (score > highScore) {
			highScore = score;
			highScoreLabel.setText(formatScore(highScore));
		}
		scoreLabel.setText(formatScore(score));
	}
	
	public void addScore(int points) {
		setScore(score + points);
	}
	
	public void setHighScore(int highScore) {
		this.highScore = highScore;
		highScoreLabel.setText(formatScore(highScore));
	}
	
	public void setLives(int lives) {
		this.lives = lives;
		livesLabel.setText(formatLives(lives));
	}
	
	public void loseLife() {
		if (lives > 0) {
			lives--;
			livesLabel.setText(formatLives(lives));
		}
	}
	
	public void setSpellCards(int spellCards) {
		this.spellCards = spellCards;
		spellCardsLabel.setText(formatSpellCards(spellCards));
	}
	
	public void useSpellCard() {
		if (spellCards > 0) {
			spellCards--;
			spellCardsLabel.setText(formatSpellCards(spellCards));
		}
	}
	
	public void setMaxScore(int maxScore) {
		this.maxScore = maxScore;
		maxScoreLabel.setText(formatScore(maxScore));
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

