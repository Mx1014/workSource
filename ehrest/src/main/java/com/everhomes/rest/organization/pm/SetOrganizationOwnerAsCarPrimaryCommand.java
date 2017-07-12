package com.everhomes.rest.organization.pm;

import javax.validation.constraints.NotNull;

/**
 *  <ul>
 *      <li>carId: 车辆id</li>
 *      <li>orgOwnerId: 业主id</li>
 *      <li>organizationId: 公司id</li>
 *  </ul>
 */
public class SetOrganizationOwnerAsCarPrimaryCommand {

    @NotNull private Long carId;
    @NotNull private Long orgOwnerId;
    @NotNull private Long organizationId;

    public Long getOrgOwnerId() {
        return orgOwnerId;
    }

    public void setOrgOwnerId(Long orgOwnerId) {
        this.orgOwnerId = orgOwnerId;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }
}
