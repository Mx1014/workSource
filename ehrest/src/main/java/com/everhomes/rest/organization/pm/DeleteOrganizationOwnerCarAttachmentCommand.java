package com.everhomes.rest.organization.pm;

import javax.validation.constraints.NotNull;

/**
 *  <ul>
 *      <li>id: 附件Id</li>
 *      <li>carId: 车辆Id</li>
 *      <li>organizationId: 公司Id</li>
 *  </ul>
 */
public class DeleteOrganizationOwnerCarAttachmentCommand {

    @NotNull private Long id;
    @NotNull private Long carId;
    @NotNull private Long organizationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
