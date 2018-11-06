package com.everhomes.rest.investment;

import com.everhomes.util.StringHelper;

public class GiveUpInvitedCustomerCommand {

    private Long id;

    private Long orgId;

    private Long communityId;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
