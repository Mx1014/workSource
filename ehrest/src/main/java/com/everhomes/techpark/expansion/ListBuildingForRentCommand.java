package com.everhomes.techpark.expansion;



public class ListBuildingForRentCommand {
    private Integer pageAnchor;
    
    private Integer pageSize;
   
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
	public Integer getPageAnchor() {
        return pageAnchor;
    }
    public void setPageAnchor(Integer pageAnchor) {
        this.pageAnchor = pageAnchor;
    }
    public Integer getPageSize() {
        return pageSize;
    }
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    
}
