package com.everhomes.rest.techpark.expansion;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>pageAnchor：锚点</li>
 * <li>pageSize：每页数量</li>
 * <li>namespaceId：命名空间 </li>
 * <li>communityId：小区id</li>
 * <li>applyType：申请类型  APPLY(1):申请 EXPANSION(2): 扩租 RENEW(3)</li>
 * <li>sourceType：招租类型{@link com.everhomes.rest.techpark.expansion.ApplyEntrySourceType}</li> 
 * <li>status：  参考{@link com.everhomes.rest.techpark.expansion.ApplyEntryStatus}}</li>
 * <li>buildingId： 楼栋id-如果为空就是搜全部</li>
 * <li>issuerType：发布人类型  {@link com.everhomes.rest.techpark.expansion.LeaseIssuerType  NORMAL_USER：普通用户或公司，ORGANIZATION：物业公司}</li>
 * <li>LeaseIssuerId： 发布人id</li>
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
    
	private Long buildingId;

	private String issuerType;

	private Long LeaseIssuerId;

	public String getIssuerType() {
		return issuerType;
	}

	public void setIssuerType(String issuerType) {
		this.issuerType = issuerType;
	}

	public Long getLeaseIssuerId() {
		return LeaseIssuerId;
	}

	public void setLeaseIssuerId(Long leaseIssuerId) {
		LeaseIssuerId = leaseIssuerId;
	}

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
	public Long getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
