package com.everhomes.rest.wanke;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.util.StringHelper;

public class ListCommunityResponse {
	private Long nextPageAnchor;
	private Long userId;
    private String contentServer;
    private String loginToken;
    @ItemType(String.class)
    private List<String> accessPoints;
    
	@ItemType(CommunityDTO.class)
    private List<CommunityDTO> communities;
	
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<CommunityDTO> getCommunities() {
		return communities;
	}

	public void setCommunities(List<CommunityDTO> communities) {
		this.communities = communities;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getContentServer() {
		return contentServer;
	}
	public void setContentServer(String contentServer) {
		this.contentServer = contentServer;
	}

	public String getLoginToken() {
		return loginToken;
	}

	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}

	public List<String> getAccessPoints() {
		return accessPoints;
	}

	public void setAccessPoints(List<String> accessPoints) {
		this.accessPoints = accessPoints;
	}
	
}
