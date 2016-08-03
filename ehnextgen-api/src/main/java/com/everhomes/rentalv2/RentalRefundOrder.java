package com.everhomes.rentalv2;

import com.everhomes.server.schema.tables.pojos.EhRentalv2RefundOrders;
import com.everhomes.util.StringHelper;

public class RentalRefundOrder extends EhRentalv2RefundOrders {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4779194716439864017L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
