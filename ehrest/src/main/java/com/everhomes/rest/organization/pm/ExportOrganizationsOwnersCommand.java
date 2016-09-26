package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *  <li>communityId: 物业小区id</li>
 *  <li>organizationId: 公司id</li>
 * </ul>
 */
public class ExportOrganizationsOwnersCommand {

    @NotNull private Long communityId;
    @NotNull private Long organizationId;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
