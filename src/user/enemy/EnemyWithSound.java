package user.enemy;

import stg.game.ui.GameCanvas;
import stg.util.AudioManager;

/**
 * 带音效的敌人示例 - 演示如何使用AudioManager播放音效
 * @since 2026-01-24
 */
public class EnemyWithSound extends BasicEnemy {
	private AudioManager audioManager;
	private boolean soundEnabled;
	private boolean deathSoundPlayed;
	
	public EnemyWithSound(float spawnX, float spawnY, float moveSpeed, GameCanvas gameCanvas) {
		super(spawnX, spawnY, moveSpeed, gameCanvas);
		this.audioManager = AudioManager.getInstance();
		this.soundEnabled = true;
		this.deathSoundPlayed = false;
	}
	
	public EnemyWithSound(float spawnX, float spawnY, float moveSpeed, GameCanvas gameCanvas, boolean soundEnabled) {
		super(spawnX, spawnY, moveSpeed, gameCanvas);
		this.audioManager = AudioManager.getInstance();
		this.soundEnabled = soundEnabled;
		this.deathSoundPlayed = false;
	}
	
	@Override
	public void update() {
		super.update();
		
		if (soundEnabled && !isAlive() && !deathSoundPlayed) {
			playDeathSound();
			deathSoundPlayed = true;
		}
	}
	
	private void playDeathSound() {
		audioManager.playSoundEffect("explosion.wav");
	}
	
	public void playHitSound() {
		if (soundEnabled && isAlive()) {
			audioManager.playSoundEffect("hit.wav");
		}
	}
	
	public void playShootSound() {
		if (soundEnabled && isAlive()) {
			audioManager.playSoundEffect("enemy_shoot.wav");
		}
	}
	
	public void setSoundEnabled(boolean enabled) {
		this.soundEnabled = enabled;
	}
	
	public boolean isSoundEnabled() {
		return soundEnabled;
	}
}

