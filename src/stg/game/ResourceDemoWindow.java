package stg.game;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import stg.game.ui.*;
import stg.util.*;

/**
 * 资源加载演示窗口 - 展示如何使用ResourceManager和AudioManager
 * @since 2025-01-24
 */
public class ResourceDemoWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private ResourceManager resourceManager;
	private AudioManager audioManager;
	private GameCanvas gameCanvas;
	
	private JButton loadImagesButton;
	private JButton playMusicButton;
	private JButton stopMusicButton;
	private JButton playSoundButton;
	private JButton testButton;
	
	private JLabel statusLabel;
	
	public ResourceDemoWindow() {
		this.resourceManager = ResourceManager.getInstance();
		this.audioManager = AudioManager.getInstance();
		
		initializeUI();
	}
	
	private void initializeUI() {
		setTitle("资源加载演示");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		setLayout(new BorderLayout());
		
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new GridLayout(5, 1, 10, 10));
		controlPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		loadImagesButton = new JButton("加载图片资源");
		playMusicButton = new JButton("播放背景音乐");
		stopMusicButton = new JButton("停止背景音乐");
		playSoundButton = new JButton("播放音效");
		testButton = new JButton("测试游戏集成");
		
		loadImagesButton.addActionListener(e -> loadImages());
		playMusicButton.addActionListener(e -> playMusic());
		stopMusicButton.addActionListener(e -> stopMusic());
		playSoundButton.addActionListener(e -> playSound());
		testButton.addActionListener(e -> testGameIntegration());
		
		controlPanel.add(loadImagesButton);
		controlPanel.add(playMusicButton);
		controlPanel.add(stopMusicButton);
		controlPanel.add(playSoundButton);
		controlPanel.add(testButton);
		
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		statusLabel = new JLabel("准备就绪");
		statusLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
		statusPanel.add(statusLabel);
		
		add(controlPanel, BorderLayout.CENTER);
		add(statusPanel, BorderLayout.SOUTH);
		
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void loadImages() {
		updateStatus("正在加载图片资源...");
		
		BufferedImage playerImage = resourceManager.loadImage("player.png");
		BufferedImage enemyImage = resourceManager.loadImage("enemy.png", "images");
		BufferedImage bulletImage = resourceManager.loadImage("bullet.png", "images");
		
		int loadedCount = 0;
		if (playerImage != null) loadedCount++;
		if (enemyImage != null) loadedCount++;
		if (bulletImage != null) loadedCount++;
		
		updateStatus("图片加载完成: " + loadedCount + "/3");
		
		if (loadedCount > 0) {
			JOptionPane.showMessageDialog(this, 
				"成功加载 " + loadedCount + " 张图片\n" +
				"玩家图片: " + (playerImage != null ? "√" : "×") + "\n" +
				"敌人图片: " + (enemyImage != null ? "✓" : "✗") + "\n" +
				"子弹图片: " + (bulletImage != null ? "✓" : "✗"),
				"加载结果",
				JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this, 
				"没有找到图片文件\n请确保图片文件在 resources/ 目录下",
				"加载失败",
				JOptionPane.WARNING_MESSAGE);

		}
	}
	
	private void playMusic() {
		updateStatus("正在播放背景音乐...");
		audioManager.playMusic("bgm_stage1.wav", true);
		
		if (audioManager.isMusicPlaying()) {

			updateStatus("背景音乐播放中");
		} else {
			updateStatus("背景音乐播放失败");
		}
	}
	
	private void stopMusic() {
		updateStatus("正在停止背景音乐...");
		audioManager.stopMusic();
		updateStatus("背景音乐已停止");
	}
	
	private void playSound() {
		updateStatus("正在播放音效...");
		audioManager.playSoundEffect("shoot.wav");
		updateStatus("音效播放完成");
	}
	
	private void testGameIntegration() {
		updateStatus("正在测试游戏集成...");
		
		JOptionPane.showMessageDialog(this, 
			"游戏集成测试\n\n" +
			"1. ResourceManager - 图片资源管理\n" +
			"   - 缓存机制避免重复加载\n" +
			"   - 支持子目录加载\n" +
			"   - 自动资源管理\n\n" +
			"2. AudioManager - 音频资源管理\n" +
			"   - 背景音乐循环播放\n" +
			"   - 音效即时播放\n" +
			"   - 音量控制\n\n" +
			"3. 游戏集成\n" +
			"   - PlayerWithImage - 带图片的玩家\n" +
			"   - EnemyWithSound - 带音效的敌人\n" +
			"   - GameCanvas - 游戏画布集成",
			"集成测试",
			JOptionPane.INFORMATION_MESSAGE);
		
		updateStatus("游戏集成测试完成");
	}
	
	private void updateStatus(String message) {
		statusLabel.setText(message);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new ResourceDemoWindow();
		});
	}
}

