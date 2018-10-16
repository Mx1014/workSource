package com.everhomes.rest.community.admin;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 *     <li>nextPageAnchor: nextPageAnchor</li>
 *     <li>userCommunities: userCommunities {@link CommunityAllUserDTO}</li>
 * </ul>
 */
public class ListAllCommunityUserResponse {

	private Long nextPageAnchor;

	@ItemType(CommunityAllUserDTO.class)
	private List<CommunityAllUserDTO> userCommunities;


	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<CommunityAllUserDTO> getUserCommunities() {
		return userCommunities;
	}

	public void setUserCommunities(List<CommunityAllUserDTO> userCommunities) {
		this.userCommunities = userCommunities;
	}
}
