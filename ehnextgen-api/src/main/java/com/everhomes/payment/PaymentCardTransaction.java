package com.everhomes.payment;

import com.everhomes.server.schema.tables.pojos.EhPaymentCardTransactions;
import com.everhomes.util.StringHelper;

public class PaymentCardTransaction extends EhPaymentCardTransactions{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String cardNo;
	
	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
