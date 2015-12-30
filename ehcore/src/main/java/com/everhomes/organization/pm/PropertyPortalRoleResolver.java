// @formatter:off
package com.everhomes.organization.pm;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.acl.PortalRoleResolver;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.RuntimeErrorException;

@Component(PortalRoleResolver.PORTAL_ROLE_RESOLVER_PREFIX + AppConstants.APPID_PM)
public class PropertyPortalRoleResolver implements PortalRoleResolver {
	@Autowired
	private PropertyMgrProvider propertyMgrProvider;

    @Override
    public void assumePortalRole(Long appDefinedPortalRoleId) {
        User user = UserContext.current().getUser();
        
        // The property manager should login
        if(user == null) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, 
                    "Access denied");
        }
        
        // The user should have at lease one role in property management
        List<CommunityPmMember> members = propertyMgrProvider.findPmMemberByTargetTypeAndId(PmTargetType.USER.getCode(), user.getId());
        if(members == null || members.size() == 0) {
        	throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, 
                    "Access denied");
        }
    }
}
