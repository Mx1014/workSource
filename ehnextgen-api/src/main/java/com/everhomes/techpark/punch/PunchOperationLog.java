// @formatter:off
package com.everhomes.techpark.punch;

import com.everhomes.server.schema.tables.pojos.EhPunchOperationLogs;
import com.everhomes.util.StringHelper;

public class PunchOperationLog extends EhPunchOperationLogs {
	
	private static final long serialVersionUID = -4793060527767205254L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}