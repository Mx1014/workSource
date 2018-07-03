package com.everhomes.flow;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by xq.tian on 2018/4/24.
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface FuncParam {

    /**
     * 参数名称
     */
    String name() default "";

    /**
     * 参数描述
     */
    LocaleText desc() default @LocaleText("");

    /**
     * 默认值
     */
    LocaleText defaultValue() default @LocaleText("");

    /**
     * 参数校验器
     */
    Class<? extends FunctionParamValidator> validator() default NothingFunctionParamValidator.class;
}