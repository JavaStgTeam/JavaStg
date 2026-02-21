package stg.service.core;

import stg.service.di.DependencyContainer;

public class ServiceManager {
    private static ServiceManager instance;
    private final DependencyContainer container;

    private ServiceManager() {
        container = DependencyContainer.getInstance();
    }

    public static synchronized ServiceManager getInstance() {
        if (instance == null) {
            instance = new ServiceManager();
        }
        return instance;
    }

    public <T> void registerService(Class<T> serviceType, T serviceInstance) {
        container.register(serviceType, serviceInstance);
    }

    public <T> void registerServiceProvider(Class<T> serviceType, DependencyContainer.ServiceProvider<T> provider) {
        container.registerProvider(serviceType, provider);
    }

    public <T> T getService(Class<T> serviceType) {
        return container.get(serviceType);
    }

    public void unregisterService(Class<?> serviceType) {
        container.unregister(serviceType);
    }

    public void initializeAllServices() {
        // 初始化所有注册的服务
        // 这里可以添加服务初始化逻辑
    }

    public void shutdownAllServices() {
        // 关闭所有服务
        container.clear();
    }
}
