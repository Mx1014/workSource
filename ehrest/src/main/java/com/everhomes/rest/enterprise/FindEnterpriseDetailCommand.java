package com.everhomes.rest.enterprise;

import com.everhomes.util.StringHelper;

/**
 * <ul> 查看单个公司的具体属性（标准版）
 * <li>organizationId: 公司id</li>
 * <li>namespaceId: 域空间ID</li>
 * </ul>
 *
 */
public class FindEnterpriseDetailCommand {
    private Long organizationId;
    private Integer namespaceId;

    public Long getOrganizationId() {
        return organizationId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
