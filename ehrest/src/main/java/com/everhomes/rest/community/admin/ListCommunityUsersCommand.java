package com.everhomes.rest.community.admin;




/**
 * <ul>
 * <li>pageAnchor: 分页的锚点，本次开始取的位置</li>
 * <li>pageSize: 每页的数量</li>
 * <li>communityId: 小区id</li>
 * <li>isAuth: 认证状态  1认证  2非认证</li>
 * <li>keywords: 关键字</li>
 * </ul>
 */
public class ListCommunityUsersCommand {
	
	private Long pageAnchor;
    
    private Integer pageSize;
	
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
