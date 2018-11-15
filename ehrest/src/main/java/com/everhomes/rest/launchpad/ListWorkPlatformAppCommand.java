// @formatter:off
package com.everhomes.rest.launchpad;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>organizationId: 企业ID</li>
 * </ul>
 */
public class ListWorkPlatformAppCommand {
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
