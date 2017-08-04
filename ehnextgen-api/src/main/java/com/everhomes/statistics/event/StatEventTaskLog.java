// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.server.schema.tables.pojos.EhStatEventTaskLogs;
import com.everhomes.util.StringHelper;

public class StatEventTaskLog extends EhStatEventTaskLogs {
	
	private static final long serialVersionUID = -7239659747077085377L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}