package com.everhomes.rest.rentalv2.admin;

public class GetGeneralAccountSettingCommand {
    private Long communityId;
    private Long resourceTypeId;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getResourceTypeId() {
        return resourceTypeId;
    }

    public void setResourceTypeId(Long resourceTypeId) {
        this.resourceTypeId = resourceTypeId;
    }
}
