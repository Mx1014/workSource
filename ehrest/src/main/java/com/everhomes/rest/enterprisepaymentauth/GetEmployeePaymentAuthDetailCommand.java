package com.everhomes.rest.enterprisepaymentauth;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 总公司ID</li>
 * <li>userId: 用户Id,默认当前用户</li>
 * </ul>
 */
public class GetEmployeePaymentAuthDetailCommand {
    private Long organizationId;
    private Long userId;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
