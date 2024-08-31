package com.hncu.commons;

import java.lang.annotation.*;

/**
 * 数据范围注解
 *
 * @Author caimeisahng
 * @Date 2024/6/30 19:45
 * @Version 1.0
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {
    //在sql语句的末尾添加一个过滤条件

    /**
     * 表的别名
     */
    public String tableAlias() default "";

    /**
     * 表的字段名
     */
    public String tableField() default "";

}
