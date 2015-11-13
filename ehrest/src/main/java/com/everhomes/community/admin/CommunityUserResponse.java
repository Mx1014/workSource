package com.everhomes.community.admin;

import java.util.List;

import com.everhomes.discover.ItemType;

public class CommunityUserResponse {
	
	private Integer nextPageOffset;
	
	@ItemType(CommunityUserDto.class)
	private List<CommunityUserDto> userCommunities;

	public Integer getNextPageOffset() {
		return nextPageOffset;
	}

	public void setNextPageOffset(Integer nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}

	public List<CommunityUserDto> getUserCommunities() {
		return userCommunities;
	}

	public void setUserCommunities(List<CommunityUserDto> userCommunities) {
		this.userCommunities = userCommunities;
	}
	
	
}
