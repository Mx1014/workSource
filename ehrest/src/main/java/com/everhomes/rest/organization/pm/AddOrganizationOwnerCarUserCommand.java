package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>carId: 车辆id</li>
 * <li>orgOwnerId: 业主id</li>
 * <li>organizationId: 公司id</li>
 * </ul>
 */
public class AddOrganizationOwnerCarUserCommand {

    @NotNull private Long carId;
    @NotNull private Long orgOwnerId;
    @NotNull private Long organizationId;

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

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
