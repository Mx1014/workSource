package com.everhomes.rest.energy;

import javax.validation.constraints.NotNull;

/**
 * Created by ying.xiong on 2017/11/23.
 */
public class ImportTasksByEnergyPlanCommand {
    @NotNull
    private Long ownerId;
    @NotNull private String ownerType;
    @NotNull private Long communityId;
    private Integer namespaceId;

    private Long planId;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }
}
