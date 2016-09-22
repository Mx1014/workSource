package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>carId: 车辆id</li>
 * <li>ownerId: 业主id</li>
 * <li>organizationId: 公司id</li>
 * </ul>
 */
public class AddOrganizationOwnerCarUserCommand {

    @NotNull private Long carId;
    @NotNull private Long ownerId;
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
