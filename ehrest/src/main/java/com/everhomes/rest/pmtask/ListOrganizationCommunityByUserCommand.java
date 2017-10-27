package com.everhomes.rest.pmtask;

import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/10/20.
 */
public class ListOrganizationCommunityByUserCommand {
    private Long organizationId;

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
