package com.everhomes.rest.techpark.expansion;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId：公司id</li>
 * </ul>
 */
public class CheckIsLeaseIssuerCommand {
    private Long organizationId;

    private Integer namespaceId;

    private Long categoryId;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

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
