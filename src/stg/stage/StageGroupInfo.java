package stg.stage;

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
     * 关卡组显示名称
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
    
    /**
     * 关卡组排序顺序，数值越小越靠前
     * 默认 0，未设置注解时按类名字母序排列
     * @return 排序顺序
     */
    int order() default 0;
}