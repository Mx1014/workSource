package com.everhomes.techpark.rental;

import com.everhomes.server.schema.tables.pojos.EhRentalCloseDates;
import com.everhomes.util.StringHelper;

public class RentalCloseDate extends EhRentalCloseDates {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2819206700248066864L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
