package com.everhomes.rest.customer.openapi;

import com.everhomes.util.StringHelper;

/**
 * Created by Rui.Jia  2018/7/13 17 :15
 */

public class EnterpriseDTO {
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
