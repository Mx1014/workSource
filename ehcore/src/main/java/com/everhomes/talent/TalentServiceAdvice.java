// @formatter:off
package com.everhomes.talent;

import static com.everhomes.util.RuntimeErrorException.errorWith;

import java.lang.reflect.Field;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.Size;
import javax.validation.metadata.ConstraintDescriptor;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.energy.util.ParamErrorCodes;
import com.everhomes.user.UserContext;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.util.RuntimeErrorException;

@Aspect
@Component
public class TalentServiceAdvice {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	
	@Before("execution(* com.everhomes.talent.TalentServiceImpl.*(..))")
	public void check(JoinPoint joinPoint) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		// 检查通用参数，通过参数上的注解检查
		Object cmd = joinPoint.getArgs()[0];
		validate(cmd);
		
		// 检查是否为管理员
		Long userId = UserContext.current().getUser().getId();
		Field field = cmd.getClass().getDeclaredField("organizationId");
		field.setAccessible(true);
		Long organizationId = (Long) field.get(cmd);
		
		checkAdmin(userId, organizationId);
	}

	private boolean checkAdmin(Long userId, Long organizationId) {
		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
		boolean result = resolver.checkSuperAdmin(userId, organizationId);
		if (!result) {
			throw RuntimeErrorException.errorWith(TalentServiceErrorCode.SCOPE,
					TalentServiceErrorCode.NOT_ADMIN, "not admin");
		}
		return result;
	}
	
    private void validate(Object o) {
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
