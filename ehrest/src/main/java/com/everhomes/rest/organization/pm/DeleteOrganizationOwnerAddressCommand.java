package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 *  <ul>
 *      <li>ownerId: 业主/客户的id</li>
 *      <li>addressId: 地址id</li>
 *      <li>organizationId: 公司id</li>
 *  </ul>
 */
public class DeleteOrganizationOwnerAddressCommand {

    @NotNull private Long ownerId;
    @NotNull private Long addressId;
    @NotNull private Long organizationId;

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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
