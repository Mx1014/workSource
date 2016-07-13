package com.everhomes.techpark.rental;

import com.everhomes.server.schema.tables.pojos.EhRentalSitesBillNumbers;
import com.everhomes.util.StringHelper;

public class RentalSitesBillNumber extends EhRentalSitesBillNumbers {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5685367409237584246L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
