package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 *<li>id: 保险信息标识号</li>
 *</ul>
 */
public class DeleteOrganizationMemberInsurancesCommand {
    private Long id;

    public DeleteOrganizationMemberInsurancesCommand() {
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
