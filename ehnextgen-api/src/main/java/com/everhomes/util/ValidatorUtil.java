// @formatter:off
package com.everhomes.util;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.energy.util.ParamErrorCodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.Size;
import javax.validation.metadata.ConstraintDescriptor;
import java.util.Set;

import static com.everhomes.util.RuntimeErrorException.errorWith;

public class ValidatorUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(ValidatorUtil.class);
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	
	public static void validate(Object o) {
		// LOGGER.debug(o == null ? "null" : o.toString());
        Set<ConstraintViolation<Object>> result = validator.validate(o);
        for (ConstraintViolation<Object> v : result) {
            ConstraintDescriptor<?> constraintDescriptor = v.getConstraintDescriptor();
            String constraintAnnotationClassName = constraintDescriptor.getAnnotation().annotationType().getName();
            switch (constraintAnnotationClassName) {
                // 参数长度检查
                case "javax.validation.constraints.Size":
                    Size size = (Size) constraintDescriptor.getAnnotation();
                    int max = size.max();
                    int min = size.min();
                    int length = v.getInvalidValue().toString().length();
                    if (length < min) {
						if (min == 1) {
							throw errorWith(ParamErrorCodes.SCOPE, ParamErrorCodes.ERROR_EMPTY_STRING,
	                                "Parameter cannot be empty: %s", v.getPropertyPath());
						}else {
							throw errorWith(ParamErrorCodes.SCOPE, ParamErrorCodes.ERROR_UNDER_LENGTH,
	                                "Parameter under length: %s [ %s ]", v.getPropertyPath(), v.getInvalidValue());
						}
					}else if (length > max) {
						throw errorWith(ParamErrorCodes.SCOPE, ParamErrorCodes.ERROR_OVER_LENGTH,
                                "Parameter over length: %s [ %s ]", v.getPropertyPath(), v.getInvalidValue());
					}
                case "javax.validation.constraints.NotNull":
                	throw errorWith(ParamErrorCodes.SCOPE, ParamErrorCodes.ERROR_EMPTY_STRING,
                            "Parameter cannot be empty: %s", v.getPropertyPath());
                // 其他参数检查
                default:
                    throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                            "Invalid parameter %s [ %s ]", v.getPropertyPath(), v.getInvalidValue());
            }
        }
    }
}
