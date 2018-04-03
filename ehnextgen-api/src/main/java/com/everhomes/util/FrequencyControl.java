package com.everhomes.util;

import java.lang.annotation.*;

/**
 * Created by lei.lv on 2018/1/9.
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface FrequencyControl {
    /**
     *
     * 允许访问的次数，默认值MAX_VALUE
     */
    int count() default Integer.MAX_VALUE;

    /**
     *
     * 时间段，单位为毫秒，默认值2秒钟
     */
    long time() default 2000;

    String[] key() default "";

}
