package com.everhomes.rest.organization.pm;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * <ul>
 *	<li>requests : 欠费家庭列表,详见: {@link com.everhomes.rest.organization.pm.OweFamilyDTO}</li>
 *</ul>
 *
 */

public class ListOweFamilysByConditionsCommandResponse {
	
	@ItemType(OweFamilyDTO.class)
	private List<OweFamilyDTO> requests;

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
