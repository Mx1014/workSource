package com.everhomes.techpark.rental;

import com.everhomes.server.schema.tables.pojos.EhRentalSiteOwners;
import com.everhomes.util.StringHelper;

public class RentalSiteOwner extends EhRentalSiteOwners {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1306005790352473339L;
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
