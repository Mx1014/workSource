package com.everhomes.community.admin;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;



public class ListCommunityUsersCommand {
	
	private Integer pageOffset;
    
    private Integer pageSize;
	
	@NotNull
	private Long communityId;
	
	private Integer isAuth;
	
	private String keywords;
	
	

	public Integer getPageOffset() {
		if(null == this.pageOffset){
			return 1;
		}
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

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}


	public Integer getIsAuth() {
		if(null == this.isAuth){
			return 0;
		}
		return isAuth;
	}

	public void setIsAuth(Integer isAuth) {
		this.isAuth = isAuth;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	
	
}
