package com.everhomes.payment;

import com.everhomes.server.schema.tables.pojos.EhPaymentCardRechargeOrders;
import com.everhomes.util.StringHelper;

public class PaymentCardRechargeOrder extends EhPaymentCardRechargeOrders{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
