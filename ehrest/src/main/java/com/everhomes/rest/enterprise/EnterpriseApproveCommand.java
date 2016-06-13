package com.everhomes.rest.enterprise;

import javax.validation.constraints.NotNull;

public class EnterpriseApproveCommand {
    @NotNull
    Long enterpriseId;
    
    @NotNull
    Long communityId;

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }
    
    
}
