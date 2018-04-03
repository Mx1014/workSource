
package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>addressId: 地址id</li>
 * <li>organizationId: 公司id</li>
 * <li>ownerType: ownerType EhCommunities 权限校验时用的</li>
 * <li>ownerId: ownerId, communityId</li>
 * </ul>
 */
public class ListOrganizationOwnerCarsByAddressCommand {

    @NotNull private Long addressId;
    @NotNull private Long organizationId;
    private String ownerType;
    private Long ownerId;

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
