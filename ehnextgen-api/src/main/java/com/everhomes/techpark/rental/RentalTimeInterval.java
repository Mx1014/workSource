package com.everhomes.techpark.rental;

import com.everhomes.server.schema.tables.pojos.EhRentalTimeInterval;
import com.everhomes.util.StringHelper;

public class RentalTimeInterval extends EhRentalTimeInterval {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 5967522342169724307L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
