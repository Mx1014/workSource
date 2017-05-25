package com.everhomes.rest.techpark.expansion;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityId: 园区ID</li>
 * <li>namespaceId: 域空间ID</li>
 * <li>organizationId: 公司ID</li>
 * <li>buildingId: 楼栋id</li>
 * </ul>
 */
public class ListLeaseIssuerApartmentsCommand {
    private Long communityId;

    private Integer namespaceId;

    private Long buildingId;

    private Long organizationId;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
