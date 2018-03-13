package com.everhomes.rest.organization;

/**
 * Created by ying.xiong on 2018/3/13.
 */
public class CheckSetAdminPrivilegeCommand {
    private Long manageOrganizationId;
    private Long communityId;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getManageOrganizationId() {
        return manageOrganizationId;
    }

    public void setManageOrganizationId(Long manageOrganizationId) {
        this.manageOrganizationId = manageOrganizationId;
    }
}
