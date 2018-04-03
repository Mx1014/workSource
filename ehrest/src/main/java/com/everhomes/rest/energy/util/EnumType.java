package com.everhomes.rest.energy.util;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.*;

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

    Class<? extends Payload>[] payload() default { };

    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        NotNull[] value();
    }
}
