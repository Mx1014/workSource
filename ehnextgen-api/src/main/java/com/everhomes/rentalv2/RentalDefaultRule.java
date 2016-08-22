package com.everhomes.rentalv2;

import com.everhomes.server.schema.tables.pojos.EhRentalv2DefaultRules;
import com.everhomes.util.StringHelper;

public class RentalDefaultRule extends EhRentalv2DefaultRules {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = -3234626307734017079L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
