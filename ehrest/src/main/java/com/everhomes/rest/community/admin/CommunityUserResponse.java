package com.everhomes.rest.community.admin;

import java.util.List;

import com.everhomes.discover.ItemType;

public class CommunityUserResponse {
	
	private Long nextPageOffset;
	
	@ItemType(CommunityUserDto.class)
	private List<CommunityUserDto> userCommunities;


	public Long getNextPageOffset() {
		return nextPageOffset;
	}

	public void setNextPageOffset(Long nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}

	public List<CommunityUserDto> getUserCommunities() {
		return userCommunities;
	}

	public void setUserCommunities(List<CommunityUserDto> userCommunities) {
		this.userCommunities = userCommunities;
	}
	
	
}
