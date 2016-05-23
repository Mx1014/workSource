package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

public class SearchOrganizationCommand {
    private Integer namespaceId;
    private String keyword;
    
    private Long communityId;
    
    private Long pageAnchor;
    private Integer pageSize;
    
    private String organizationType;
    
    public Integer getNamespaceId() {
        return namespaceId;
    }
    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }
    public String getKeyword() {
        return keyword;
    }
    public void setKeyword(String keyword) {
        this.keyword = keyword;
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
    public Long getCommunityId() {
        return communityId;
    }
    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }
    

    public String getOrganizationType() {
		return organizationType;
	}
	public void setOrganizationType(String organizationType) {
		this.organizationType = organizationType;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
