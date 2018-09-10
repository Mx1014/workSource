package com.everhomes.techpark.punch;

import com.everhomes.server.schema.tables.pojos.EhPunchDayLogs;
import com.everhomes.util.StringHelper;

public class PunchDayLog extends EhPunchDayLogs{

	public PunchDayLog() {
		this.setBelateCount(0);
		this.setLeaveEarlyCount(0);
		this.setBelateTimeTotal(0L);
		this.setLeaveEarlyTimeTotal(0L);
		this.setForgotPunchCountOnDuty(0);
		this.setForgotPunchCountOffDuty(0);
		this.setAskForLeaveRequestCount(0);
		this.setGoOutRequestCount(0);
		this.setBusinessTripRequestCount(0);
		this.setOvertimeRequestCount(0);
		this.setPunchExceptionRequestCount(0);
		this.setOvertimeTotalLegalHoliday(0L);
		this.setOvertimeTotalRestday(0L);
		this.setOvertimeTotalWorkday(0L);
	}

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
