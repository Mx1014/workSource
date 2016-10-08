package com.everhomes.rest.organization.pm;

import javax.validation.constraints.NotNull;

/**
 *  <ul>
 *      <li>carId: 车辆id</li>
 *      <li>organizationId: 公司id</li>
 *  </ul>
 */
public class ListOrganizationOwnerCarAttachmentCommand {

    @NotNull private Long carId;
    @NotNull private Long organizationId;

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
