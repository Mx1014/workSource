package com.everhomes.rentalv2;

import com.everhomes.server.schema.tables.pojos.EhRentalv2ResourceOrders;
import com.everhomes.util.StringHelper;

public class RentalResourceOrder extends EhRentalv2ResourceOrders {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1054988598000397112L;
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
