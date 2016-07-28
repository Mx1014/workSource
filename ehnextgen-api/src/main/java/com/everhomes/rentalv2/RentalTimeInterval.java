package com.everhomes.rentalv2;

import com.everhomes.server.schema.tables.pojos.EhRentalv2TimeInterval;
import com.everhomes.util.StringHelper;

public class RentalTimeInterval extends EhRentalv2TimeInterval {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 5967522342169724307L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
