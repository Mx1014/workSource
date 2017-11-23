package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>enterpriseId: 企业Id</li>
 * </ul>
 */
public class ListPunchSupportiveAddressCommand {

    private Long enterpriseId;

    public ListPunchSupportiveAddressCommand() {
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
