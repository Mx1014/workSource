package com.everhomes.rest.organization;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>organizationId: 公司id</li>
 * </ul>
 */
public class ListCommunityOrganizationTreeCommand {
	
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
