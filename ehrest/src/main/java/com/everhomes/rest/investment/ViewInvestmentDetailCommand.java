package com.everhomes.rest.investment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: customerId</li>
 *     <li>communityId: communityId</li>
 *     <li>orgId: orgId</li>
 * </ul>
 */
public class ViewInvestmentDetailCommand {
    private Long id;

    private Long communityId;

    private Long orgId;

    private Boolean isAdmin;

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
