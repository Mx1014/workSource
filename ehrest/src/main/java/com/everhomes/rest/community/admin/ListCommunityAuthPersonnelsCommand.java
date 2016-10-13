package com.everhomes.rest.community.admin;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>communityId：园区id</li>
 * <li>isSignedup: 是否左邻注册用户</li>
 * <li>status: 状态:1-待认证 3-已同意 0-已拒绝</li>
 * <li>pageAnchor: 页码</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */ 
public class ListCommunityAuthPersonnelsCommand {
	@NotNull
	private Long    communityId;
	private Byte isSignedup;
	private Byte status;
	private Integer pageOffset;
	private Long pageAnchor; 
	private Integer pageSize;
	
	private String keywords;

	 
	public Byte getIsSignedup() {
		return isSignedup;
	}

	public void setIsSignedup(Byte isSignedup) {
		this.isSignedup = isSignedup;
	}

	public Integer getPageOffset() {
		return pageOffset;
	}

	public void setPageOffset(Integer pageOffset) {
		this.pageOffset = pageOffset;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Long getPageAnchor() {
		return pageAnchor;
	}


	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}


	public String getKeywords() {
		return keywords;
	}


	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


	public Byte getStatus() {
		return status;
	}


	public void setStatus(Byte status) {
		this.status = status;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
}
