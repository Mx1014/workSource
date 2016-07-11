package com.everhomes.techpark.rental;

import com.everhomes.server.schema.tables.pojos.EhRentalSiteItems;
import com.everhomes.util.StringHelper;

public class RentalSiteItem extends EhRentalSiteItems {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4943437026312490928L;
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
