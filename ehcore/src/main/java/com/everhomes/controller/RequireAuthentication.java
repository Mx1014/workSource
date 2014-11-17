package com.everhomes.controller;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that is used by REST controllers to indicate the requirement of authenticated access for their actions
 * 
 * @author Kelven Yang
 *
 */
@Target(value={ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RequireAuthentication {
    boolean value() default false;
}
