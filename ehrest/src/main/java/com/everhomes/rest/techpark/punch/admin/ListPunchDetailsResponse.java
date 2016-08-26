package com.everhomes.rest.techpark.punch.admin;

import java.util.List;

import com.everhomes.discover.ItemType;

/**
 * <ul>
 * 
 * <li>nextPageAnchor： 下页anchor</li>
 * <li>punchDayDetails：详情列表{@link com.everhomes.rest.techpark.punch.admin.PunchDayDetailDTO}</li>  
 * </ul>
 */
public class ListPunchDetailsResponse {

	private Long nextPageAnchor;

	@ItemType(PunchDayDetailDTO.class)
	private List<PunchDayDetailDTO> punchDayDetails;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<PunchDayDetailDTO> getPunchDayDetails() {
		return punchDayDetails;
	}

	public void setPunchDayDetails(List<PunchDayDetailDTO> punchDayDetails) {
		this.punchDayDetails = punchDayDetails;
	}
}
