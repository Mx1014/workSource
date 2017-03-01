package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>orgOwnerId: 业主id</li>
 * <li>addressId: 地址id</li>
 * <li>organizationId: 公司id</li>
 * <li>livingStatus: 是否在户</li>
 * <li>ownerType: ownerType EhCommunities 权限校验时用的</li>
 * <li>ownerId: ownerId, communityId</li>
 * </ul>
 */
public class AddOrganizationOwnerAddressCommand {

    @NotNull private Long orgOwnerId;
    @NotNull private Long addressId;
    @NotNull private Long organizationId;
    @NotNull private Byte livingStatus;

    private String ownerType;
    private Long ownerId;

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

    public Long getOrgOwnerId() {
        return orgOwnerId;
    }

    public void setOrgOwnerId(Long orgOwnerId) {
        this.orgOwnerId = orgOwnerId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Byte getLivingStatus() {
        return livingStatus;
    }

    public void setLivingStatus(Byte livingStatus) {
        this.livingStatus = livingStatus;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
