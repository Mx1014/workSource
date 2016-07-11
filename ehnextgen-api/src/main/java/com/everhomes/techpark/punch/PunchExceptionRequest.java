package com.everhomes.techpark.punch;

import com.everhomes.server.schema.tables.pojos.EhPunchExceptionRequests;
import com.everhomes.util.StringHelper;

public class PunchExceptionRequest extends EhPunchExceptionRequests {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7791831577785512923L;


	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
