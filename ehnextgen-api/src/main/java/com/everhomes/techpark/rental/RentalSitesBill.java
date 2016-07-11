package com.everhomes.techpark.rental;

import com.everhomes.server.schema.tables.pojos.EhRentalSitesBills;
import com.everhomes.util.StringHelper;

public class RentalSitesBill extends EhRentalSitesBills {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1054988598000397112L;
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
