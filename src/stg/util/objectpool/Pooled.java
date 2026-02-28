package stg.util.objectpool;

import java.lang.annotation.*;

/**
 * 对象池注解
 * 用于标记需要使用对象池管理的类
 * 被标记的类会在类加载时自动注册到对象池管理器
 * 
 * @date 2026-02-22
 * @author JavaSTG Team
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Pooled {
    /**
     * 对象池初始容量
     * @return 初始容量
     */
    int initialCapacity() default 0;
    
    /**
     * 对象池最大容量
     * @return 最大容量
     */
    int maxCapacity() default 100;
    
    /**
     * 对象池名称（可选，默认使用类名）
     * @return 对象池名称
     */
    String name() default "";
}
