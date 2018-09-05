package com.everhomes.rentalv2;

import com.everhomes.server.schema.tables.pojos.EhRentalv2SiteResources;
import com.everhomes.util.StringHelper;

public class RentalResourcePic extends EhRentalv2SiteResources {

	/**
	 * 
	 */
	private static final long serialVersionUID = 31425624586614785L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
