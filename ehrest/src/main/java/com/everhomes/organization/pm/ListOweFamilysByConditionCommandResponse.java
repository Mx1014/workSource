package com.everhomes.organization.pm;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * <ul>
 * 	<li>nextPageOffset : 下一页页码</li>
 *	<li>requests : 欠费家庭列表,详见: {@link com.everhomes.organization.pm.OweFamilyDTO}</li>
 *</ul>
 *
 */

public class ListOweFamilysByConditionCommandResponse {
	
private Long nextPageOffset;

	@ItemType(OweFamilyDTO.class)
	private List<OweFamilyDTO> requests;

	public Long getNextPageOffset() {
		return nextPageOffset;
	}

	public void setNextPageOffset(Long nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}

	public List<OweFamilyDTO> getRequests() {
		return requests;
	}

	public void setRequests(List<OweFamilyDTO> requests) {
		this.requests = requests;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
