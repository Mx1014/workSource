package com.everhomes.rest.community;

import com.everhomes.util.StringHelper;

public class GetFloorRangeCommand {
	
	private Integer namespaceId;
	private Long communityId;
	
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
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
