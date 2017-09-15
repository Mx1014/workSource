// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.server.schema.tables.pojos.EhStatEventParamLogs;
import com.everhomes.util.StringHelper;

public class StatEventParamLog extends EhStatEventParamLogs {
	
	private static final long serialVersionUID = -3619518234118923325L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}