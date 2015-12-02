package com.everhomes.techpark.expansion;

public class ListEnterpriseApplyEntryCommand {
    private Integer pageAnchor;
    private Integer pageSize;
   
    private Integer namespaceId;
    
    private Long communityId;
    
    private String applyType;
	
	private String status; 
	
	private String sourceType; //1:enterprise 2:chuangke space 
    
    
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
	
	public String getApplyType() {
		return applyType;
	}
	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
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
