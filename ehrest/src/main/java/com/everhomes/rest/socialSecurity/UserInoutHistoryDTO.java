package com.everhomes.rest.socialSecurity;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>UserInoutHistoryDTO :
 * <li>month: 月份 YYYYMM</li>
 * <li>logs: 增减员记录string字符串 </li>
 * </ul>
 */
public class UserInoutHistoryDTO {
	private String month;
	private String logs;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getLogs() {
		return logs;
	}

	public void setLogs(String logs) {
		this.logs = logs;
	}

}
