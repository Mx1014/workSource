package com.everhomes.rest.profile;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 公司 id</li>
 * </ul>
 */
public class GetProfileFieldCommand {

    private Long organizationId;

    public GetProfileFieldCommand() {
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
