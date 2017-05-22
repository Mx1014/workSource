package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 *<li>id: 工作经验信息编号</li>
 *</ul>
 */
public class DeleteOrganizationMemberWorkExps {
    private Long id;

    public DeleteOrganizationMemberWorkExps() {
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
