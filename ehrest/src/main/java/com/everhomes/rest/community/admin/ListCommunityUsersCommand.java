package com.everhomes.rest.community.admin;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;



public class ListCommunityUsersCommand {
	
	private Long pageAnchor;
    
    private Integer pageSize;
	
	@NotNull
	private Integer namespaceId;
	
	private Integer isAuth;
	
	private String keywords;
	
	private Long communityId;
	

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



	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
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

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	
	
}
