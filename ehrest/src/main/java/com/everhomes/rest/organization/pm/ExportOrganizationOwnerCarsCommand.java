package com.everhomes.rest.organization.pm;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *  <li>communityId: 物业小区id</li>
 *  <li>organizationId: 公司id</li>
 * </ul>
 */
public class ExportOrganizationOwnerCarsCommand {
    @NotNull
    private Long organizationId;
    @NotNull
    private Long communityId;

    public Long getCommunityId() {
        return communityId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public void setCommunityId(Long communityId) {

        this.communityId = communityId;
    }
}
