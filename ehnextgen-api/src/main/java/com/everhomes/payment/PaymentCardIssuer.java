package com.everhomes.payment;

import com.everhomes.server.schema.tables.pojos.EhPaymentCardIssuers;
import com.everhomes.util.StringHelper;

public class PaymentCardIssuer extends EhPaymentCardIssuers{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
