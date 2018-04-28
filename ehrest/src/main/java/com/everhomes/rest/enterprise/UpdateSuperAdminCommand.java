package com.everhomes.rest.enterprise;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>entries: 手机号</li>
 *     <li>organizationId: 组织id</li>
 *     <li>namespaceId: 域空间ID</li>
 * </ul>
 */
public class UpdateSuperAdminCommand {
    private String entries;
    private Long organizationId;
    private Integer namespaceId;



    public String getEntries() {
        return entries;
    }

    public void setEntries(String entries) {
        this.entries = entries;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
