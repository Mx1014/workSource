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
    LocaleText name() default @LocaleText("");

    /**
     * 参数描述
     */
    LocaleText desc() default @LocaleText("");

    /**
     * 参数校验器
     */
    Class<? extends FunctionParamValidator> validator() default NothingFunctionParamValidator.class;
}