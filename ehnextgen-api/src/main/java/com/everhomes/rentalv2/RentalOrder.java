package com.everhomes.rentalv2;

import com.everhomes.server.schema.tables.pojos.EhRentalv2Orders;
import com.everhomes.util.StringHelper;

public class RentalOrder extends EhRentalv2Orders {

	/**
	 * 
	 */
	private static final long serialVersionUID = 276859795301911837L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
