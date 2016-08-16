package com.everhomes.rest.techpark.punch.admin;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.techpark.punch.PunchLogsDay;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  
 * <li>userLogs: 每个人当月的考勤状态  {@link com.everhomes.rest.techpark.punch.admin.UserMonthLogsDTO} </li>
 * <li>nextPageAnchor: 下页anchor</li>
 * </ul>
 */
public class ListPunchMonthLogsResponse {
 
	@ItemType(UserMonthLogsDTO.class)
	private List<UserMonthLogsDTO> userLogs;

	private Long nextPageAnchor;


	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
 
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<UserMonthLogsDTO> getUserLogs() {
		return userLogs;
	}

	public void setUserLogs(List<UserMonthLogsDTO> userLogs) {
		this.userLogs = userLogs;
	}
	
}
