package com.everhomes.techpark.rental;

import com.everhomes.server.schema.tables.pojos.EhRentalBills;
import com.everhomes.util.StringHelper;

public class RentalBill extends EhRentalBills {

	/**
	 * 
	 */
	private static final long serialVersionUID = 276859795301911837L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
