package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * 
 * communityId: 园区id
 *
 */
public class ListEnterpriseWithoutVideoConfAccountCommand {
	
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
