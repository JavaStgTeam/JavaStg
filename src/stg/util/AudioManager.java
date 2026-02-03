package stg.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.*;

/**
 * 音频管理类 - 负责加载和播放游戏音频（背景音乐、音效等）
 * @since 2026-01-24
 */
public class AudioManager {
	private static final AudioManager instance = new AudioManager();
	private final Map<String, Clip> musicClips = new HashMap<>();
	private final Map<String, Clip> soundEffects = new HashMap<>();
	private String resourcePath;
	private Clip currentMusic;
	private float musicVolume;
	private float soundVolume;
	
	private AudioManager() {
		this.resourcePath = "resources/audio/";
		this.currentMusic = null;
		this.musicVolume = 0.7f;
		this.soundVolume = 1.0f;
	}
	
	public static AudioManager getInstance() {
		return instance;
	}
	
	public void setResourcePath(String path) {
		this.resourcePath = path;
	}
	
	public void setMusicVolume(float volume) {
		this.musicVolume = Math.max(0.0f, Math.min(1.0f, volume));
		if (currentMusic != null && currentMusic.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
			FloatControl gainControl = (FloatControl) currentMusic.getControl(FloatControl.Type.MASTER_GAIN);
			float dB = (float) (Math.log(this.musicVolume) / Math.log(10.0) * 20.0);
			gainControl.setValue(dB);
		}
	}
	
	public void setSoundVolume(float volume) {
		this.soundVolume = Math.max(0.0f, Math.min(1.0f, volume));
	}
	
	public float getMusicVolume() {
		return musicVolume;
	}
	
	public float getSoundVolume() {
		return soundVolume;
	}
	
	public Clip loadMusic(String filename) {
		if (musicClips.containsKey(filename)) {
			return musicClips.get(filename);
		}
		
		try {
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath + "music/" + filename);
			if (inputStream == null) {
				System.err.println("【音频加载失败】音乐文件不存在: " + resourcePath + "music/" + filename);
				return null;
			}
			
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream);
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			
			musicClips.put(filename, clip);
			return clip;
		} catch (UnsupportedAudioFileException e) {
			System.err.println("【音频加载失败】不支持的音频格式 " + filename);
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			System.err.println("【音频加载失败】无法读取音频文件 " + filename);
			e.printStackTrace();
			return null;
		} catch (LineUnavailableException e) {
			System.err.println("【音频加载失败】音频线路不可用: " + filename);
			e.printStackTrace();
			return null;
		}
	}
	
	public Clip loadSoundEffect(String filename) {
		if (soundEffects.containsKey(filename)) {
			return soundEffects.get(filename);
		}
		
		try {
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath + "sfx/" + filename);
			if (inputStream == null) {
				System.err.println("【音频加载失败】音效文件不存在: " + resourcePath + "sfx/" + filename);
				return null;
			}
			
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream);
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			
			soundEffects.put(filename, clip);
			return clip;
		} catch (UnsupportedAudioFileException e) {
			System.err.println("【音频加载失败】不支持的音频格式 " + filename);
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			System.err.println("【音频加载失败】无法读取音频文件 " + filename);
			e.printStackTrace();
			return null;
		} catch (LineUnavailableException e) {
			System.err.println("【音频加载失败】音频线路不可用: " + filename);
			e.printStackTrace();
			return null;
		}
	}
	
	public void playMusic(String filename, boolean loop) {
		stopMusic();
		
		Clip clip = loadMusic(filename);
		if (clip == null) {
			return;
		}
		
		currentMusic = clip;
		
		if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			float dB = (float) (Math.log(musicVolume) / Math.log(10.0) * 20.0);
			gainControl.setValue(dB);
		}
		
		if (loop) {
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} else {
			clip.start();
		}
	}
	
	public void stopMusic() {
		if (currentMusic != null) {
			currentMusic.stop();
			currentMusic.setFramePosition(0);
			currentMusic = null;
		}
	}
	
	public void pauseMusic() {
		if (currentMusic != null && currentMusic.isRunning()) {
			currentMusic.stop();
		}
	}
	
	public void resumeMusic() {
		if (currentMusic != null && !currentMusic.isRunning()) {
			currentMusic.start();
		}
	}
	
	public void playSoundEffect(String filename) {
		Clip clip = loadSoundEffect(filename);
		if (clip == null) {
			return;
		}
		
		if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			float dB = (float) (Math.log(soundVolume) / Math.log(10.0) * 20.0);
			gainControl.setValue(dB);
		}
		
		clip.setFramePosition(0);
		clip.start();
	}
	
	public void unloadMusic(String filename) {
		Clip clip = musicClips.get(filename);
		if (clip != null) {
			if (clip.isRunning()) {
				clip.stop();
			}
			clip.close();
			musicClips.remove(filename);
		}
	}
	
	public void unloadSoundEffect(String filename) {
		Clip clip = soundEffects.get(filename);
		if (clip != null) {
			if (clip.isRunning()) {
				clip.stop();
			}
			clip.close();
			soundEffects.remove(filename);
		}
	}
	
	public void clearMusic() {
		for (Clip clip : musicClips.values()) {
			if (clip.isRunning()) {
				clip.stop();
			}
			clip.close();
		}
		musicClips.clear();
		currentMusic = null;
	}
	
	public void clearSoundEffects() {
		for (Clip clip : soundEffects.values()) {
			if (clip.isRunning()) {
				clip.stop();
			}
			clip.close();
		}
		soundEffects.clear();
	}
	
	public void clearAll() {
		clearMusic();
		clearSoundEffects();
	}
	
	public boolean isMusicPlaying() {
		return currentMusic != null && currentMusic.isRunning();
	}
	
	public boolean isMusicLoaded(String filename) {
		return musicClips.containsKey(filename);
	}
	
	public boolean isSoundEffectLoaded(String filename) {
		return soundEffects.containsKey(filename);
	}
}

