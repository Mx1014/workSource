package com.everhomes.rentalv2;

import com.everhomes.server.schema.tables.pojos.EhRentalv2ItemsOrders;
import com.everhomes.util.StringHelper;

public class RentalItemsOrder extends EhRentalv2ItemsOrders {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1910035514856038407L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
