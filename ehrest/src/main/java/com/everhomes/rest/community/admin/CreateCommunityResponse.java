// @formatter:off

package com.everhomes.rest.community.admin;

import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityDTO: 小区信息，{@link com.everhomes.rest.address.CommunityDTO}</li>
 * </ul>
 */
public class CreateCommunityResponse {
	private CommunityDTO communityDTO;

	public CommunityDTO getCommunityDTO() {
		return communityDTO;
	}

	public void setCommunityDTO(CommunityDTO communityDTO) {
		this.communityDTO = communityDTO;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}