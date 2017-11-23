package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 *<li>id: 合同信息标识号</li>
 *</ul>
 */
public class DeleteOrganizationMemberContractsCommand {

    private Long id;

    public DeleteOrganizationMemberContractsCommand() {
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
