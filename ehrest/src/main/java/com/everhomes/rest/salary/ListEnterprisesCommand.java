package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 *
 * <ul>
 * <li>organizationId: 总公司id</li>
 * </ul>
 */
public class ListEnterprisesCommand {
    private Long organizationId;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }
}
