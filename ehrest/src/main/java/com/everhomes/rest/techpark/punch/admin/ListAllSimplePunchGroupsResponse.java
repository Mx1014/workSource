package com.everhomes.rest.techpark.punch.admin;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>punchGroups: 结果{@link com.everhomes.rest.techpark.punch.admin.PunchGroupDTO}</li>
 * </ul>
 */
public class ListAllSimplePunchGroupsResponse {
	@ItemType(PunchGroupDTO.class)
	private List<PunchGroupDTO> punchGroups;


	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public List<PunchGroupDTO> getPunchGroups() {
		return punchGroups;
	}

	public void setPunchGroups(List<PunchGroupDTO> punchGroups) {
		this.punchGroups = punchGroups;
	}


 
 

}
