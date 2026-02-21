package user.test;

import stg.service.core.ServiceManager;
import stg.service.render.IRenderer;
import stg.service.audio.IAudioManager;

public class ServiceFrameworkTest {
    public static void main(String[] args) {
        System.out.println("=== 服务框架测试 ===");
        
        ServiceManager manager = ServiceManager.getInstance();
        
        // 测试服务注册
        System.out.println("1. 测试服务注册...");
        
        // 注册一个测试渲染服务
        IRenderer testRenderer = new IRenderer() {
            @Override public void init() {}
            @Override public void beginFrame() {}
            @Override public void endFrame() {}
            @Override public void drawCircle(float x, float y, float radius, java.awt.Color color) {}
            @Override public void drawRect(float x, float y, float width, float height, java.awt.Color color) {}
            @Override public void drawLine(float x1, float y1, float x2, float y2, java.awt.Color color) {}
            @Override public void drawText(String text, float x, float y, java.awt.Font font, java.awt.Color color) {}
            @Override public void drawImage(Object image, float x, float y, float width, float height) {}
            @Override public void setColor(java.awt.Color color) {}
            @Override public void setFont(java.awt.Font font) {}
            @Override public void cleanup() {}
            @Override public boolean isInitialized() { return true; }
        };
        
        manager.registerService(IRenderer.class, testRenderer);
        
        // 测试服务获取
        System.out.println("2. 测试服务获取...");
        IRenderer retrievedRenderer = manager.getService(IRenderer.class);
        System.out.println("   渲染服务获取成功: " + (retrievedRenderer != null));
        
        // 测试服务提供者
        System.out.println("3. 测试服务提供者...");
        manager.registerServiceProvider(IAudioManager.class, () -> new IAudioManager() {
            @Override public void init() {}
            @Override public void loadMusic(String name, String path) {}
            @Override public void loadSound(String name, String path) {}
            @Override public void playMusic(String name, boolean loop) {}
            @Override public void playSound(String name) {}
            @Override public void stopMusic(String name) {}
            @Override public void stopAllSounds() {}
            @Override public void setMusicVolume(float volume) {}
            @Override public void setSoundVolume(float volume) {}
            @Override public void pauseMusic(String name) {}
            @Override public void resumeMusic(String name) {}
            @Override public void unloadMusic(String name) {}
            @Override public void unloadSound(String name) {}
            @Override public void unloadAll() {}
            @Override public void cleanup() {}
            @Override public boolean isInitialized() { return true; }
        });
        
        IAudioManager audioManager = manager.getService(IAudioManager.class);
        System.out.println("   音频服务获取成功: " + (audioManager != null));
        
        // 测试服务注销
        System.out.println("4. 测试服务注销...");
        manager.unregisterService(IRenderer.class);
        IRenderer nullRenderer = manager.getService(IRenderer.class);
        System.out.println("   渲染服务注销成功: " + (nullRenderer == null));
        
        System.out.println("=== 服务框架测试完成 ===");
    }
}
