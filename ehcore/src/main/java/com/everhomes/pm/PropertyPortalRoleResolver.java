// @formatter:off
package com.everhomes.pm;

import org.springframework.stereotype.Component;

import com.everhomes.acl.PortalRoleResolver;
import com.everhomes.app.AppConstants;

@Component(PortalRoleResolver.PORTAL_ROLE_RESOLVER_PREFIX + AppConstants.APPID_PM)
public class PropertyPortalRoleResolver implements PortalRoleResolver {

    @Override
    public void assumePortalRole(Long appDefinedPortalRoleId) {
        // TODO Auto-generated method stub
        
    }
}
