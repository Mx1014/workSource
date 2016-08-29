package com.everhomes.rest.organization.pm;

import javax.validation.constraints.NotNull;

/**
 *  <ul>
 *      <li>organizationId: 公司id</li>
 *      <li>ownerId: 业主/客户的id</li>
 *      <li>addressId: 地址id</li>
 *  </ul>
 */
public class DeleteOrganizationOwnerAddressCommand {

    @NotNull
    private Long ownerId;
    @NotNull
    private Long organizationId;
    @NotNull
    private Long addressId;

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
}
