package com.everhomes.techpark.rental;

import com.everhomes.server.schema.tables.pojos.EhRentalItemsBills;
import com.everhomes.util.StringHelper;

public class RentalItemsBill extends EhRentalItemsBills {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1910035514856038407L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
