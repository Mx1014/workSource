package com.everhomes.rest.organization.pm;

import javax.validation.constraints.NotNull;

/**
 *  <ul>
 *      <li>organizationId: 公司id</li>
 *      <li>orgOwnerId: 业主id</li>
 *  </ul>
 */
public class ListOrganizationOwnerCarByOrgOwnerCommand {

    @NotNull private Long organizationId;
    @NotNull private Long orgOwnerId;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getOrgOwnerId() {
        return orgOwnerId;
    }

    public void setOrgOwnerId(Long orgOwnerId) {
        this.orgOwnerId = orgOwnerId;
    }
}
