package com.everhomes.discover;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies item type for collection class in run time
 * 
 * @author Kelven Yang
 *
 */
@Target(value={ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ItemType {
    Class<?> value ();
}
