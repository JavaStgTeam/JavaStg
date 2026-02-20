package stg.util.objectpool;

/**
 * 对象工厂接口
 * 用于创建新的对象实例
 * 
 * @param <T> 对象类型
 * @date 2026-02-20
 * @author JavaSTG Team
 */
@FunctionalInterface
public interface ObjectFactory<T> {
    
    /**
     * 创建新的对象实例
     * 
     * @return 新创建的对象
     */
    T create();
}
