package com.everhomes.rest.techpark.punch;

import java.util.List;

import com.everhomes.discover.ItemType; 

/**
 * <ul>
 * 
 * <li>nextPageAnchor： 下页anchor</li>
 * <li>punchLogs：打卡记录列表{@link com.everhomes.rest.techpark.punch.PunchLogDTO}</li>  
 * </ul>
 */
public class ListOrganizationPunchLogsResponse {

	private Long nextPageAnchor;

	@ItemType(PunchLogDTO.class)
	private List<PunchLogDTO> punchLogs;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<PunchLogDTO> getPunchLogs() {
		return punchLogs;
	}

	public void setPunchLogs(List<PunchLogDTO> punchLogs) {
		this.punchLogs = punchLogs;
	}
	
	
}
