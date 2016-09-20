package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerId: 业主id</li>
 * <li>organizationId: 公司id</li>
 * <li>addressId: 地址id</li>
 * <li>livingStatus: 是否在户</li>
 * </ul>
 */
public class AddOrganizationOwnerAddressCommand {

    private Long ownerId;
    private Long organizationId;
    private Long addressId;
    private Byte livingStatus;

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
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
