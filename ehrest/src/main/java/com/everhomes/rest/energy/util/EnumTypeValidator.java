package com.everhomes.rest.energy.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 参数枚举类型校验器
 * Created by xq.tian on 2016/11/9.
 */
public class EnumTypeValidator implements ConstraintValidator<EnumType, Object> {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private EnumType enumType;

    @Override
    public void initialize(EnumType constraintAnnotation) {
        enumType = constraintAnnotation;
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return enumType.nullValue();
        }
        try {
            Class<?> enumClass = enumType.value();
            Method fromCodeMethod = enumClass.getMethod("fromCode", value.getClass());
            Object retVal = fromCodeMethod.invoke(enumClass, value);
            return retVal != null;
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            LOGGER.error("EnumType validator error");
        }
        return true;
    }
}
