package stg.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.WeakHashMap;
import java.util.Map;

/**
 * 事件总线 - 用于游戏对象间的通信
 */
public class EventBus {
    private static final EventBus instance = new EventBus();
    // 使用WeakHashMap存储订阅者，防止内存泄漏
    private final Map<Object, ConcurrentHashMap<Class<?>, List<Consumer<?>>>> subscriberMap = new WeakHashMap<>();
    
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
     * @param subscriber 订阅者对象
     * @param eventType 事件类型
     * @param handler 事件处理器
     */
    public <T> void subscribe(Object subscriber, Class<T> eventType, Consumer<T> handler) {
        subscriberMap.computeIfAbsent(subscriber, k -> new ConcurrentHashMap<>())
                    .computeIfAbsent(eventType, k -> new ArrayList<>())
                    .add(handler);
    }
    
    /**
     * 发布事件
     */
    @SuppressWarnings("unchecked")
    public <T> void publish(T event) {
        Class<?> eventType = event.getClass();
        // 遍历所有订阅者，处理事件
        for (ConcurrentHashMap<Class<?>, List<Consumer<?>>> eventHandlers : subscriberMap.values()) {
            List<Consumer<?>> handlers = eventHandlers.get(eventType);
            if (handlers != null) {
                handlers.forEach(h -> ((Consumer<T>) h).accept(event));
            }
        }
    }
    
    /**
     * 取消订阅
     * @param subscriber 订阅者对象
     * @param eventType 事件类型
     * @param handler 事件处理器
     */
    public <T> void unsubscribe(Object subscriber, Class<T> eventType, Consumer<T> handler) {
        ConcurrentHashMap<Class<?>, List<Consumer<?>>> eventHandlers = subscriberMap.get(subscriber);
        if (eventHandlers != null) {
            List<Consumer<?>> handlers = eventHandlers.get(eventType);
            if (handlers != null) {
                handlers.remove(handler);
            }
        }
    }
    
    /**
     * 取消订阅者的所有订阅
     * @param subscriber 订阅者对象
     */
    public void unsubscribeAll(Object subscriber) {
        subscriberMap.remove(subscriber);
    }
    
    /**
     * 清理所有订阅
     */
    public void clear() {
        subscriberMap.clear();
    }
}
