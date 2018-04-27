// @formatter:off

package com.everhomes.rest.community.admin;

import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>dtos: 园区 参考 {@link  com.everhomes.rest.address.CommunityDTO}</li>
 * </ul>
 */
public class CreateCommunitiesResponse {
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