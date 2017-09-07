// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.server.schema.tables.pojos.EhStatEventPortalStatistics;
import com.everhomes.util.StringHelper;

public class StatEventPortalStatistic extends EhStatEventPortalStatistics {
	
	private static final long serialVersionUID = 5343586233400499438L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}