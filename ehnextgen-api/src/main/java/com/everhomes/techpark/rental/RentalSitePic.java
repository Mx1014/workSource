package com.everhomes.techpark.rental;

import com.everhomes.server.schema.tables.pojos.EhRentalSitePics;
import com.everhomes.util.StringHelper;

public class RentalSitePic extends EhRentalSitePics {

	/**
	 * 
	 */
	private static final long serialVersionUID = 31425624586614785L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
