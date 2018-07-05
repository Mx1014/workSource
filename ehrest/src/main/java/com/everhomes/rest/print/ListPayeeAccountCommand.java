// @formatter:off
package com.everhomes.rest.print;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间id</li>
 * <li>ownerType: 归属的类型，{@link com.everhomes.rest.parking.ParkingOwnerType}</li>
 * <li>communityId: 归属的ID，如小区ID</li>
 * <li>organizationId: 公司id</li>
 * </ul>
 */
public class ListPayeeAccountCommand {
    private Integer namespaceId;
    private String ownerType;
    private Long communityId;
    private Long organizationId;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
