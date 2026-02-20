package stg.util.objectpool;

/**
 * 可重置接口
 * 用于对象池中的对象，在回收时重置状态
 * 
 * @date 2026-02-20
 * @author JavaSTG Team
 */
public interface Resettable {
    
    /**
     * 重置对象状态
     * 当对象被回收到对象池时调用
     * 应将对象恢复到初始状态，准备下次使用
     */
    void resetState();
}
