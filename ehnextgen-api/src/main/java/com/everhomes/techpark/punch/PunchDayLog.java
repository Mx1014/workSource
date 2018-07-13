package com.everhomes.techpark.punch;

import com.everhomes.server.schema.tables.pojos.EhPunchDayLogs;
import com.everhomes.util.StringHelper;

public class PunchDayLog extends EhPunchDayLogs{

	/**
	 * 
	 */
	private static final long serialVersionUID = 508657593842107325L;
	private Integer statisticsCount;
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	public Integer getStatisticsCount() {
		return statisticsCount;
	}
	public void setStatisticsCount(Integer statisticsCount) {
		this.statisticsCount = statisticsCount;
	}
}
