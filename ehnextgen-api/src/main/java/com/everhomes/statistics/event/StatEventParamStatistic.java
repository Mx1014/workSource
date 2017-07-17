// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.server.schema.tables.pojos.EhStatEventParamStatistics;
import com.everhomes.util.StringHelper;

public class StatEventParamStatistic extends EhStatEventParamStatistics {
	
	private static final long serialVersionUID = 1633956033340723402L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}