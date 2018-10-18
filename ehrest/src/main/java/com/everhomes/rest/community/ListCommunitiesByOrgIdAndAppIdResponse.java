package com.everhomes.rest.community;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.acl.ProjectDTO;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>dtos: 项目列表 {@link ProjectDTO}</li>
 * </ul>
 */
public class ListCommunitiesByOrgIdAndAppIdResponse {


	private List<ProjectDTO> dtos;

	public List<ProjectDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<ProjectDTO> dtos) {
		this.dtos = dtos;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}


}
