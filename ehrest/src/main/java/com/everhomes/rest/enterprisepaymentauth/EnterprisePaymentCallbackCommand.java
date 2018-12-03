package com.everhomes.rest.enterprisepaymentauth;

import com.everhomes.pay.order.OrderPaymentNotificationCommand;
import com.everhomes.util.StringHelper;

public class EnterprisePaymentCallbackCommand extends OrderPaymentNotificationCommand {
    private String requestToken;

    public String getRequestToken() {
        return requestToken;
    }

    public void setRequestToken(String requestToken) {
        this.requestToken = requestToken;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
