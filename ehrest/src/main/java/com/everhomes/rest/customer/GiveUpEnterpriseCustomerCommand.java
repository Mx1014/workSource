package com.everhomes.rest.customer;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: 客户id</li>
 * </ul>
 */
public class GiveUpEnterpriseCustomerCommand {

    private Long id;

    private Long orgId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
