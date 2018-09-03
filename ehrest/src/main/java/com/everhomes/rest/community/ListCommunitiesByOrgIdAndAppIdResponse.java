package com.everhomes.rest.community;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>dtos: dtos园区 {@link CommunityDTO}</li>
 * </ul>
 */
public class ListCommunitiesByOrgIdAndAppIdResponse {


	private List<CommunityDTO> dtos;

	public List<CommunityDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<CommunityDTO> dtos) {
		this.dtos = dtos;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}


}
