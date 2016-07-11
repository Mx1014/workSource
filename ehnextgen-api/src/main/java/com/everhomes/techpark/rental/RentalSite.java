package com.everhomes.techpark.rental;

import com.everhomes.server.schema.tables.pojos.EhRentalSites;
import com.everhomes.util.StringHelper;

public class RentalSite extends EhRentalSites {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6936073467928653292L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
