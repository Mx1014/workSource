package com.everhomes.rest.enterprise;

import com.everhomes.util.StringHelper;

public class LeaveEnterpriseCommand {
    private Long enterpriseId;

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
