package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 *  <ul>
 *      <li>id: 活动记录Id</li>
 *      <li>ownerId: 业主Id</li>
 *      <li>organizationId: 公司Id</li>
 *  </ul>
 */
public class DeleteOrganizationOwnerBehaviorCommand {

    @NotNull private Long id;
    @NotNull private Long ownerId;
    @NotNull private Long organizationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
