package com.everhomes.rentalv2;

import com.everhomes.server.schema.tables.pojos.EhRentalv2ResourceRanges;
import com.everhomes.util.StringHelper;

public class RentalSiteRange extends EhRentalv2ResourceRanges {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1306005790352473339L;
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
