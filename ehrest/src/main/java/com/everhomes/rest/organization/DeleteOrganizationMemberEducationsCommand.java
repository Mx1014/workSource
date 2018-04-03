package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 教育信息编号</li>
 * </ul>
 */
public class DeleteOrganizationMemberEducationsCommand {

    private Long id;

    public DeleteOrganizationMemberEducationsCommand() {
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
