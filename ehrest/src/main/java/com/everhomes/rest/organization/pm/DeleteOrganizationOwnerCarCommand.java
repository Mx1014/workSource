package com.everhomes.rest.organization.pm;

import javax.validation.constraints.NotNull;

/**
 *  <ul>
 *      <li>id: 车辆id</li>
 *      <li>organizationId: 公司id</li>
 *  </ul>
 */
public class DeleteOrganizationOwnerCarCommand {

    @NotNull private Long id;
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
}
