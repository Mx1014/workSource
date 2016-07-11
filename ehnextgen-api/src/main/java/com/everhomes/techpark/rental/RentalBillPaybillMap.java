package com.everhomes.techpark.rental;

import com.everhomes.server.schema.tables.pojos.EhRentalBillPaybillMap;
import com.everhomes.util.StringHelper;

public class RentalBillPaybillMap extends EhRentalBillPaybillMap {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3135043736647614222L;
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
