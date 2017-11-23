// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.server.schema.tables.pojos.EhStatEventContentLogs;
import com.everhomes.util.StringHelper;

public class StatEventLogContent extends EhStatEventContentLogs {
	
	private static final long serialVersionUID = 3121046519657364119L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}