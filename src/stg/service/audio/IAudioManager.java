package stg.service.audio;

public interface IAudioManager {
    void init();
    
    void loadMusic(String name, String path);
    void loadSound(String name, String path);
    
    void playMusic(String name, boolean loop);
    void playSound(String name);
    
    void stopMusic(String name);
    void stopAllSounds();
    
    void setMusicVolume(float volume);
    void setSoundVolume(float volume);
    
    void pauseMusic(String name);
    void resumeMusic(String name);
    
    void unloadMusic(String name);
    void unloadSound(String name);
    void unloadAll();
    
    void cleanup();
    boolean isInitialized();
}
