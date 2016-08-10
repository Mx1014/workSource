package com.everhomes.rentalv2;

import com.everhomes.server.schema.tables.pojos.EhRentalv2Cells;
import com.everhomes.util.StringHelper;

public class RentalCell extends EhRentalv2Cells {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4545929677190416346L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
