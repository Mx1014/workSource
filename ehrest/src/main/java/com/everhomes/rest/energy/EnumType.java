package com.everhomes.rest.energy;

import javax.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <p>
 * 验证参数为枚举类型
 * 只需在command中需要验证为枚举类型的字段上打上该注解,并提供枚举类型即可
 * </p>
 * Created by xq.tian on 2016/11/9.
 */
@Target(ElementType.FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = EnumTypeValidator.class)
@Documented
public @interface EnumType {

    Class<? extends Enum<?>> value();

    String message() default "The enum type is unexpected";

    boolean nullValue() default false;

    Class<?>[] groups() default { };

}
