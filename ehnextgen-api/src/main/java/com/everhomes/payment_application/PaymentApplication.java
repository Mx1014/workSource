package com.everhomes.payment_application;

import com.everhomes.server.schema.tables.pojos.EhPaymentApplications;
import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/12/27.
 */
public class PaymentApplication extends EhPaymentApplications {
    private static final long serialVersionUID = 3028710036404732412L;

    private Long orgId;

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
