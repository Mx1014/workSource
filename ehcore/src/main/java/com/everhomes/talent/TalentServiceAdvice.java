// @formatter:off
package com.everhomes.talent;

import static com.everhomes.util.RuntimeErrorException.errorWith;

import java.lang.reflect.Field;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.talent.TalentOwnerType;
import com.everhomes.user.UserContext;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.ValidatorUtil;

@Aspect
@Component
public class TalentServiceAdvice {

    @Autowired
    private CommunityProvider communityProvider;
    
	@Before("execution(* com.everhomes.talent.TalentServiceImpl.*(..))")
	public void check(JoinPoint joinPoint) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		// 检查通用参数，通过参数上的注解检查
		Object cmd = joinPoint.getArgs()[0];
		ValidatorUtil.validate(cmd);
		
		// 检查owner
		checkOwner(cmd);
		
		// 检查是否为管理员
		checkAdmin(cmd);
	}

	private void checkOwner(Object cmd) throws IllegalArgumentException, IllegalAccessException{
		try {
			Field ownerTypeField = cmd.getClass().getDeclaredField("ownerType");
			ownerTypeField.setAccessible(true);
			String ownerType = (String) ownerTypeField.get(cmd);
			
			Field ownerIdField = cmd.getClass().getDeclaredField("ownerId");
			ownerIdField.setAccessible(true);
			Long ownerId = (Long) ownerIdField.get(cmd);
			
			TalentOwnerType talentOwnerType = TalentOwnerType.fromCode(ownerType);
			if (talentOwnerType == null) {
				throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                        "Invalid parameter ownerType");
			}
			
			if (talentOwnerType == TalentOwnerType.COMMUNITY) {
				Community community = communityProvider.findCommunityById(ownerId);
				if (community == null || community.getNamespaceId().intValue() != UserContext.getCurrentNamespaceId().intValue()) {
					throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
	                        "Invalid parameter ownerId");
				}
			}
		} catch (NoSuchFieldException e) {
			// 如果没有ownerType就不检查
		}
	}

	private void checkAdmin(Object cmd) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
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
	
    
}
