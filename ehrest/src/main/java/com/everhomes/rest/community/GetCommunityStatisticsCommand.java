package com.everhomes.rest.community;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>communityId: 园区id</li>
 * </ul>
 */
public class GetCommunityStatisticsCommand {
	
	private Long communityId;

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
