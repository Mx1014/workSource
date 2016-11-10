package com.everhomes.pmtask;

import com.everhomes.server.schema.tables.pojos.EhPmTaskTargets;
import com.everhomes.util.StringHelper;

public class PmTaskTarget extends EhPmTaskTargets{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	} 
}
