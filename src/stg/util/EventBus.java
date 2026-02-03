package stg.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * 事件总线 - 用于游戏对象间的通信
 */
public class EventBus {
    private static final EventBus instance = new EventBus();
    private final ConcurrentHashMap<Class<?>, List<Consumer<?>>> subscribers = new ConcurrentHashMap<>();
    
    /**
     * 私有构造函数
     */
    private EventBus() {}
    
    /**
     * 获取事件总线实例
     */
    public static EventBus getInstance() {
        return instance;
    }
    
    /**
     * 订阅事件
     */
    public <T> void subscribe(Class<T> eventType, Consumer<T> handler) {
        subscribers.computeIfAbsent(eventType, k -> new ArrayList<>()).add(handler);
    }
    
    /**
     * 发布事件
     */
    @SuppressWarnings("unchecked")
    public <T> void publish(T event) {
        List<Consumer<?>> handlers = subscribers.get(event.getClass());
        if (handlers != null) {
            handlers.forEach(h -> ((Consumer<T>) h).accept(event));
        }
    }
    
    /**
     * 取消订阅
     */
    public <T> void unsubscribe(Class<T> eventType, Consumer<T> handler) {
        List<Consumer<?>> handlers = subscribers.get(eventType);
        if (handlers != null) {
            handlers.remove(handler);
        }
    }
    
    /**
     * 清理所有订阅
     */
    public void clear() {
        subscribers.clear();
    }
}
