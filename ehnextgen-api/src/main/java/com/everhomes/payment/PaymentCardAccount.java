package com.everhomes.payment;

import com.everhomes.server.schema.tables.pojos.EhPaymentCardAccounts;
import com.everhomes.util.StringHelper;

public class PaymentCardAccount extends EhPaymentCardAccounts {
    private static final long serialVersionUID = -5640029348415938629L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
