package com.everhomes.rest.openapi;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

public class ListCommunitiesForThirdPartyCommand {
	
	private Integer namespaceId;
	private Long communityId;
	private Long pageAnchor;
	private Integer pageSize;
	//private Timestamp updateTime;
	private Long updateTime;
	
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
	public Long getPageAnchor() {
		return pageAnchor;
	}
	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
