package com.everhomes.rest.pmsy;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 *	<li>communityId: 小区ID</li>
 *</ul>
 *
 */
public class GetPmsyPropertyCommand {
	
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
