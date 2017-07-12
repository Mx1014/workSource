package com.everhomes.pmtask;

import com.everhomes.server.schema.tables.pojos.EhPmTaskTargetStatistics;
import com.everhomes.util.StringHelper;

public class PmTaskTargetStatistic extends EhPmTaskTargetStatistics{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
