package com.everhomes.techpark.punch;

import com.everhomes.server.schema.tables.pojos.EhPunchDayLogs;
import com.everhomes.util.StringHelper;

public class PunchDayLog extends EhPunchDayLogs{

	/**
	 * 
	 */
	private static final long serialVersionUID = 508657593842107325L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
