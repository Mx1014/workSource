package com.everhomes.rentalv2;

import com.everhomes.server.schema.tables.pojos.EhRentalv2Resources;
import com.everhomes.util.StringHelper;

public class RentalResource extends EhRentalv2Resources {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6936073467928653292L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
