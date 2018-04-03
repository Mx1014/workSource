// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.server.schema.tables.pojos.EhStatEventLogs;
import com.everhomes.util.StringHelper;

public class StatEventLog extends EhStatEventLogs {
	
	private static final long serialVersionUID = 7051974372034584482L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}