package com.everhomes.rentalv2;

import com.everhomes.server.schema.tables.pojos.EhRentalv2ResourceNumbers;
import com.everhomes.util.StringHelper;

public class RentalResourceNumber extends EhRentalv2ResourceNumbers {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8089379365327432832L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
