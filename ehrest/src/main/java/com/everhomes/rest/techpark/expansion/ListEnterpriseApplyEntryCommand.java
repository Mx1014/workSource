package com.everhomes.rest.techpark.expansion;

public class ListEnterpriseApplyEntryCommand {
    private Long pageAnchor;
    private Integer pageSize;
   
    private Integer namespaceId;
    
    private Long communityId;
    
    private Byte applyType;
	
	private Byte status; 
	
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
	
	public Byte getApplyType() {
		return applyType;
	}
	public void setApplyType(Byte applyType) {
		this.applyType = applyType;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public Long getPageAnchor() {
        return pageAnchor;
    }
    public void setPageAnchor(Long  pageAnchor) {
        this.pageAnchor = pageAnchor;
    }
    public Integer getPageSize() {
        return pageSize;
    }
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    
}
