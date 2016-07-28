package com.everhomes.rentalv2;

import com.everhomes.server.schema.tables.pojos.EhRentalv2ResourceTypes;
import com.everhomes.util.StringHelper;

public class RentalResourceType extends EhRentalv2ResourceTypes {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4829921296192221492L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
