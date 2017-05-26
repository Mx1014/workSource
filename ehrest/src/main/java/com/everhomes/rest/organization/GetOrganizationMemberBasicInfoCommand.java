package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 员工编号</li>
 * </ul>
 */
public class GetOrganizationMemberBasicInfoCommand {

    private Long id;

    public GetOrganizationMemberBasicInfoCommand() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
