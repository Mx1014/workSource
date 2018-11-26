package com.everhomes.officecubicle;

import com.everhomes.server.schema.tables.pojos.EhOfficeCubicleOrders;
import com.everhomes.server.schema.tables.pojos.EhOfficeCubicleRentOrders;
import com.everhomes.util.StringHelper;

public class OfficeCubicleRentOrder extends EhOfficeCubicleRentOrders {

	/**
	 * 
	 */
	private static final long serialVersionUID = 293074929695719308L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
