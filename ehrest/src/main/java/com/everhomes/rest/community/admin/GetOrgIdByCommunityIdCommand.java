package com.everhomes.rest.community.admin;

import com.everhomes.util.StringHelper;

public class GetOrgIdByCommunityIdCommand {
	
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
