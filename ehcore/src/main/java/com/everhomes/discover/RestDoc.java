package com.everhomes.discover;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies REST API related documentation on methods, parameters and fields in returned objects
 * 
 * @author Kelven Yang
 *
 */
@Target(value={ ElementType.FIELD, ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RestDoc {
    String value();
}
