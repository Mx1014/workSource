package com.everhomes.rentalv2;

import com.everhomes.server.schema.tables.pojos.EhRentalv2Items;
import com.everhomes.util.StringHelper;

public class RentalItem extends EhRentalv2Items {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4943437026312490928L;
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
