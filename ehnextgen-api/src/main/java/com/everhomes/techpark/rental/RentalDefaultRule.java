package com.everhomes.techpark.rental;

import com.everhomes.server.schema.tables.pojos.EhRentalDefaultRules;
import com.everhomes.util.StringHelper;

public class RentalDefaultRule extends EhRentalDefaultRules {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = -3234626307734017079L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
