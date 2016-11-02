package com.everhomes.rest.acl;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 机构id</li>
 * </ul>
 */
public class ListAuthorizationServiceModuleCommand {

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
