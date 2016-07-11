package com.everhomes.cooperation;

import com.everhomes.server.schema.tables.pojos.EhCooperationRequests;
import com.everhomes.util.StringHelper;

public class CooperationRequests extends EhCooperationRequests {

	/**
	 * 
	 */
	private static final long serialVersionUID = -963790717750905557L;

	public CooperationRequests() {
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
