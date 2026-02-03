package stg.game;

import stg.util.*;

/**
 * 资源加载测试程序
 * @since 2026-01-24
 * */ 
public class ResourceTest {
	public static void main(String[] args) {
		System.out.println("=== 资源加载测试 ===\n");
		
		ResourceManager resourceManager = ResourceManager.getInstance();
		AudioManager audioManager = AudioManager.getInstance();
		
		System.out.println("1. 测试 ResourceManager");
		System.out.println("   资源路径: " + "resources/");
		
		try {
			java.awt.image.BufferedImage testImage = resourceManager.loadImage("player.png");
			if (testImage != null) {
				System.out.println("   玩家图片加载成功: " + testImage.getWidth() + "x" + testImage.getHeight());
			} else {
				System.out.println("   玩家图片加载失败（文件可能不存在）");
			}
		} catch (Exception e) {
			System.out.println("   玩家图片加载异常: " + e.getMessage());
		}
		
		System.out.println("\n2. 测试 AudioManager");
		System.out.println("   音乐路径: " + "resources/audio/music/");
		System.out.println("   音效路径: " + "resources/audio/sfx/");
		
		System.out.println("\n3. 音量控制");
		audioManager.setMusicVolume(0.7f);
		audioManager.setSoundVolume(0.8f);
		System.out.println("   音乐音量: " + audioManager.getMusicVolume());
		System.out.println("   音效音量: " + audioManager.getSoundVolume());
		
		System.out.println("\n4. 资源管理器功能");
		System.out.println("   - 单例模式");
		System.out.println("   - 图片缓存");
		System.out.println("   - 音频缓存");
		System.out.println("   - 音量控制");
		System.out.println("   - 循环播放");
		
		System.out.println("\n=== 测试完成 ===");
		System.out.println("\n提示：");
		System.out.println("- 将图片文件放入 resources/ 目录下");
		System.out.println("- 将音乐文件放入 resources/audio/music/ 目录下");
		System.out.println("- 将音效文件放入 resources/audio/sfx/ 目录下");
		System.out.println("- 支持的图片格式: PNG, JPEG, GIF, BMP");
		System.out.println("- 支持的音频格式: WAV, AU, AIFF, MIDI");
	}
}

