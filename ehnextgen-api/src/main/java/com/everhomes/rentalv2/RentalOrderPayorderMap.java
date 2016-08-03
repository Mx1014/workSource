package com.everhomes.rentalv2;

import com.everhomes.server.schema.tables.pojos.EhRentalv2OrderPayorderMap;
import com.everhomes.util.StringHelper;

public class RentalOrderPayorderMap extends EhRentalv2OrderPayorderMap {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3135043736647614222L;
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
