package com.everhomes.payment;

import com.everhomes.server.schema.tables.pojos.EhPaymentCards;
import com.everhomes.util.StringHelper;

public class PaymentCard extends EhPaymentCards{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
