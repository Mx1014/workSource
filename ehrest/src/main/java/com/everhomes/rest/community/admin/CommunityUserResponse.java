package com.everhomes.rest.community.admin;

import java.util.List;

import com.everhomes.discover.ItemType;

public class CommunityUserResponse {
	
	private Long nextPageAnchor;
	
	@ItemType(CommunityUserDto.class)
	private List<CommunityUserDto> userCommunities;


	
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<CommunityUserDto> getUserCommunities() {
		return userCommunities;
	}

	public void setUserCommunities(List<CommunityUserDto> userCommunities) {
		this.userCommunities = userCommunities;
	}
	
	
}
