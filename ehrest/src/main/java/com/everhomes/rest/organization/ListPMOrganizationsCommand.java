package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: 域空间id</li>
 * </ul>
 */
public class ListPMOrganizationsCommand {
    private Integer namespaceId;

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
