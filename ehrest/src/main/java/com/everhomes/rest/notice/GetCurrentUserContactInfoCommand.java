package com.everhomes.rest.notice;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 公司id</li>
 * </ul>
 */
public class GetCurrentUserContactInfoCommand {
    private Long organizationId;

    public GetCurrentUserContactInfoCommand() {
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
