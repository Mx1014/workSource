package com.everhomes.techpark.rental;

import com.everhomes.server.schema.tables.pojos.EhRentalRefundOrders;
import com.everhomes.util.StringHelper;

public class RentalRefundOrder extends EhRentalRefundOrders {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4779194716439864017L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
