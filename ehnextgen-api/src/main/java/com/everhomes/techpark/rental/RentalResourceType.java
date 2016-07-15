package com.everhomes.techpark.rental;

import com.everhomes.server.schema.tables.pojos.EhRentalResourceTypes;
import com.everhomes.util.StringHelper;

public class RentalResourceType extends EhRentalResourceTypes {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4829921296192221492L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
