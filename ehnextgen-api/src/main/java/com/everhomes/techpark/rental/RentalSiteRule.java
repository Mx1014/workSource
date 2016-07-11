package com.everhomes.techpark.rental;

import com.everhomes.server.schema.tables.pojos.EhRentalSiteRules;
import com.everhomes.util.StringHelper;

public class RentalSiteRule extends EhRentalSiteRules {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4545929677190416346L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
