package stg.game.stage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 关卡组信息注解
 * 用于标记关卡组类，提供关卡组的基本信息
 * 系统会通过扫描此注解来自动发现关卡组
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface StageGroupInfo {
    /**
     * 关卡组名称
     * @return 关卡组名称
     */
    String name();
    
    /**
     * 关卡组描述
     * @return 关卡组描述
     */
    String description();
    
    /**
     * 难度级别
     * @return 难度级别
     */
    StageGroup.Difficulty difficulty();
    
    /**
     * 图标路径（可选）
     * @return 图标路径
     */
    String iconPath() default "";
}