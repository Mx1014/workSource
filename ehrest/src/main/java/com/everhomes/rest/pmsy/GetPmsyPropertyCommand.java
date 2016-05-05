package com.everhomes.rest.pmsy;

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
	
}
