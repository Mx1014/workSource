package com.everhomes.rest.organization.pm;

import javax.validation.constraints.NotNull;

/**
 *  <ul>
 *      <li>organizationId: 公司id</li>
 *      <li>ownerId: 业主/客户的id</li>
 *      <li>addressId: 地址标示符</li>
 *      <li>behaviorType: 操作类型 {@link com.everhomes.rest.organization.pm.OrganizationOwnerBehaviorType}</li>
 *  </ul>
 */
public class UpdateOrganizationOwnerAddressStatusCommand {

    @NotNull private Long ownerId;
    @NotNull private Long organizationId;
    @NotNull private Long addressId;
    private String behaviorType;

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

    public String getBehaviorType() {
        return behaviorType;
    }

    public void setBehaviorType(String behaviorType) {
        this.behaviorType = behaviorType;
    }
}
