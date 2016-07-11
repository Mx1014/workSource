package com.everhomes.techpark.punch;

import com.everhomes.server.schema.tables.pojos.EhPunchLogs;
import com.everhomes.util.StringHelper;

public class PunchLog extends EhPunchLogs {

	/**
	 * @author Wuhan
	 */
	private static final long serialVersionUID = 4272289944277363636L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
