package com.everhomes.rest.techpark.expansion;

/**
 * Created by Administrator on 2017/3/24.
 */
public class ListLeaseIssuerApartmentsCommand {
    private Long communityId;

    private Integer namespaceId;

    private Long buildingId;

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

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }
}
