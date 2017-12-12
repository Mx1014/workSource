package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/9/20.
 */
public class ListOrganizationAddressesCommand {
    private Integer namespaceId;
    private Long organizationId;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
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
}
