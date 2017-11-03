package com.everhomes.rest.acl;

import com.everhomes.util.StringHelper;

public class ServiceModuleAppsAuthorizationsDto {
    private ServiceModuleAuthorizationsDTO communityControlApps;
    private ServiceModuleAuthorizationsDTO orgControlApps;
    private ServiceModuleAuthorizationsDTO unlimitControlApps;

    public ServiceModuleAuthorizationsDTO getCommunityControlApps() {
        return communityControlApps;
    }

    public void setCommunityControlApps(ServiceModuleAuthorizationsDTO communityControlApps) {
        this.communityControlApps = communityControlApps;
    }

    public ServiceModuleAuthorizationsDTO getOrgControlApps() {
        return orgControlApps;
    }

    public void setOrgControlApps(ServiceModuleAuthorizationsDTO orgControlApps) {
        this.orgControlApps = orgControlApps;
    }

    public ServiceModuleAuthorizationsDTO getUnlimitControlApps() {
        return unlimitControlApps;
    }

    public void setUnlimitControlApps(ServiceModuleAuthorizationsDTO unlimitControlApps) {
        this.unlimitControlApps = unlimitControlApps;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
