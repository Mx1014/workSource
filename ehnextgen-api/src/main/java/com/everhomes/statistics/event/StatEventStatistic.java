// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.server.schema.tables.pojos.EhStatEventStatistics;
import com.everhomes.util.StringHelper;

public class StatEventStatistic extends EhStatEventStatistics {
	
	private static final long serialVersionUID = 1912441889007909440L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}