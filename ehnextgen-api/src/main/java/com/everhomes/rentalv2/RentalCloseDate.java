package com.everhomes.rentalv2;

import com.everhomes.server.schema.tables.pojos.EhRentalv2CloseDates;
import com.everhomes.util.StringHelper;

public class RentalCloseDate extends EhRentalv2CloseDates {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2819206700248066864L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
