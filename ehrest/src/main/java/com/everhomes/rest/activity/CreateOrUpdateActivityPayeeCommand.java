// @formatter:off
package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>organizationId: 企业ID</li>
 *     <li>payeeId: 收款方ID</li>
 * </ul>
 */
public class CreateOrUpdateActivityPayeeCommand {

    private Long organizationId;

    private Long payeeId;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(Long payeeId) {
        this.payeeId = payeeId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
