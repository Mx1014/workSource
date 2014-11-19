package com.everhomes.discover;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value={ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RestReturn {
    Class<?> value ();
    boolean collection () default false;
}
