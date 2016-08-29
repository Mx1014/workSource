package com.everhomes.rest.organization.pm;

/**
 *  <ul>
 *      <li>organizationId: 公司id</li>
 *      <li>ownerId: 业主id</li>
 *  </ul>
 */
public class ListOrganizationOwnerCarByOrgOwnerCommand {

    private Long organizationId;
    private Long ownerId;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}
