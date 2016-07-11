// @formatter:off
package com.everhomes.acl;

public interface PortalRoleResolver {
    public static final String PORTAL_ROLE_RESOLVER_PREFIX = "portalApp-";
    
    void assumePortalRole(Long appDefinedPortalRoleId);
}
