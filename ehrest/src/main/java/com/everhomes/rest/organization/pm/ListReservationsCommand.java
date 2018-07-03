//@formatter:off
package com.everhomes.rest.organization.pm;

/**
 * Created by Wentian Wang on 2018/6/12.
 */
/**
 *<ul>
 * <li>addressId:门牌id</li>
 *</ul>
 */
public class ListReservationsCommand {
    private Long addressId;
    private Integer namespaceId;
    private Long communityId;
    private Long organizationId;
    private String buildingName;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
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

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }
}
