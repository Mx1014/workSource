package com.everhomes.rest.techpark.expansion;


/**
 * <ul>
 * <li>pageAnchor：锚点</li>
 * <li>pageSize：每页数量</li>
 * <li>namespaceId：命名空间 </li>
 * <li>communityId：小区id</li>
 * <li>applyType：不知道是什么,原来怎么样就怎么样吧</li>
 * <li>sourceType：招租类型{@link com.everhomes.rest.techpark.expansion.ApplyEntrySourceType}</li> 
 * <li>status：  参考{@link com.everhomes.rest.techpark.expansion.LeasePromotionStatus}}</li> 
 * </ul>
 */
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
