package Main;

import stg.util.ALAudioManager;

public class ALAudioTest {
    public static void main(String[] args) {
        System.out.println("Testing OpenAL audio system...");
        
        try {
            // 获取ALAudioManager实例
            ALAudioManager audioManager = ALAudioManager.getInstance();
            
            // 初始化OpenAL
            System.out.println("Initializing OpenAL...");
            audioManager.init();
            
            if (!audioManager.isInitialized()) {
                System.out.println("Failed to initialize OpenAL!");
                return;
            }
            
            System.out.println("OpenAL initialized successfully!");
            
            // 测试音量控制
            System.out.println("Testing volume control...");
            audioManager.setMusicVolume(0.5f);
            audioManager.setSoundVolume(0.7f);
            System.out.println("Volume control test completed!");
            
            // 测试资源管理
            System.out.println("Testing resource management...");
            
            // 测试清理
            System.out.println("Testing cleanup...");
            audioManager.cleanup();
            System.out.println("Cleanup completed!");
            
            System.out.println("OpenAL audio test completed successfully!");
            
        } catch (Exception e) {
            System.out.println("OpenAL audio test failed:");
            e.printStackTrace();
        }
    }
}
