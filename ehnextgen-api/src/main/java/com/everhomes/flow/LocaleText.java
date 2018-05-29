package com.everhomes.flow;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by xq.tian on 2018/4/24.
 */
// @Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LocaleText {

    String value();

    /**
     * locale scope
     */
    String scope() default "";

    /**
     * locale scope code
     */
    String code() default "";
}