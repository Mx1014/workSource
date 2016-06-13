package com.everhomes.rest.launchpad;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>nextPageOffset : 下一页页码</li>
 * <li>requests : launchPadLayout列表</li>
 * </ul>
 */
public class ListLaunchPadLayoutCommandResponse {
	
	private Long nextPageOffset;
	
	@ItemType(LaunchPadLayoutDTO.class)
	private List<LaunchPadLayoutDTO> requests;

	public Long getNextPageOffset() {
		return nextPageOffset;
	}	

	public void setNextPageOffset(Long nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}

	public List<LaunchPadLayoutDTO> getRequests() {
		return requests;
	}

	public void setRequests(List<LaunchPadLayoutDTO> requests) {
		this.requests = requests;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	

}
