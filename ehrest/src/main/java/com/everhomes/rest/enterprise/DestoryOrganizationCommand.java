package com.everhomes.rest.enterprise;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 *     <li>organizationId: 公司Id</li>
 *     <li>namespaceId: 域空间ID</li>
 * </ul>
 */
public class DestoryOrganizationCommand {

    private Long organizationId;

    private Integer namespaceId;

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
