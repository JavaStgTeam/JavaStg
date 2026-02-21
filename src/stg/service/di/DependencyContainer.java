package stg.service.di;

import java.util.HashMap;
import java.util.Map;

public class DependencyContainer {
    private static DependencyContainer instance;
    private final Map<Class<?>, Object> services;
    private final Map<Class<?>, ServiceProvider<?>> providers;

    private DependencyContainer() {
        services = new HashMap<>();
        providers = new HashMap<>();
    }

    public static synchronized DependencyContainer getInstance() {
        if (instance == null) {
            instance = new DependencyContainer();
        }
        return instance;
    }

    public <T> void register(Class<T> type, T instance) {
        services.put(type, instance);
    }

    public <T> void registerProvider(Class<T> type, ServiceProvider<T> provider) {
        providers.put(type, provider);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> type) {
        T service = (T) services.get(type);
        if (service == null) {
            ServiceProvider<T> provider = (ServiceProvider<T>) providers.get(type);
            if (provider != null) {
                service = provider.provide();
                services.put(type, service);
            }
        }
        return service;
    }

    public void unregister(Class<?> type) {
        services.remove(type);
        providers.remove(type);
    }

    public void clear() {
        services.clear();
        providers.clear();
    }

    public interface ServiceProvider<T> {
        T provide();
    }
}
