package com.everhomes.flow;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by xq.tian on 2018/4/24.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExportFunction {

    /**
     * 描述
     */
    LocaleText desc() default @LocaleText("");

    /**
     * 参数列表
     */
    FuncParam[] params() default {};

    /**
     * 版本
     */
    int version();

    /**
     * id
     */
    long id();
}
