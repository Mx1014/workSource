// @formatter:off
package com.everhomes.rest.launchpad;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>organizationId: 企业ID</li>
 *     <li>namespaceId: 域空间ID</li>
 * </ul>
 */
public class ListWorkPlatformAppCommand {
    private Long organizationId;

    private Integer namespaceId;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
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
