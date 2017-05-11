// @formatter:off
package com.everhomes.talent;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.talent.TalentOwnerType;
import com.everhomes.user.UserContext;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.util.RuntimeErrorException;

@Aspect
@Component
public class TalentServiceAdvice {
	
	@Before("execution(* com.everhomes.talent.TalentServiceImpl.*(..))")
	public void check(JoinPoint joinPoint) {
		HttpServletRequest request = ( (ServletRequestAttributes) RequestContextHolder.getRequestAttributes() ).getRequest();
		String organizationIdString = request.getParameter("organizationId");
		Long userId = UserContext.current().getUser().getId();
		//TalentServiceImpl所有接口都要检查是不是管理员
		checkAdmin(userId, organizationIdString);
		//TalentServiceImpl所有不是跟分类相关的接口都要检查owner
		if (!joinPoint.getSignature().getName().contains("TalentCateogr")) {
			String ownerType = request.getParameter("ownerType");
			String ownerIdString = request.getParameter("ownerId");
			checkOwner(ownerType, ownerIdString);
		}
	}

	private void checkOwner(String ownerType, String ownerIdString) {
		if (StringUtils.isEmpty(ownerType) || StringUtils.isEmpty(ownerIdString) || TalentOwnerType.fromCode(ownerType) == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameters");
		}
	}

	private boolean checkAdmin(Long userId, String organizationIdString) {
		if (StringUtils.isEmpty(organizationIdString)) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameters");
		}
		Long organizationId = Long.parseLong(organizationIdString);
		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
		boolean result = resolver.checkSuperAdmin(userId, organizationId);
		if (!result) {
			throw RuntimeErrorException.errorWith(TalentServiceErrorCode.SCOPE,
					TalentServiceErrorCode.NOT_ADMIN, "not admin");
		}
		return result;
	}
}
