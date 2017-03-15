package com.everhomes.rest.techpark.expansion;


import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>

 * <li>sourceId：来源ID：黄页ID</li>
 * <li>sourceType：来源类型building /market_zone /for_rent/office_cubicle(工位)的 参考{@link com.everhomes.rest.techpark.expansion.ApplyEntrySourceType}}</li>
 * <li>enterpriseName：申请公司名</li>
 * <li>enterpriseId：申请公司ID</li>
 * <li>applyUserName：申请人 </li>
 * <li>applyType：申请类型  APPLY(1):申请 EXPANSION(2): 扩租 RENEW(3):续租 参考{@link com.everhomes.rest.techpark.expansion.ApplyEntryApplyType}}</li>
 * <li>sizeUnit：size的单位：SINGLETON(1)一整个？  SQUARE_METERS(2)平方米 参考{@link com.everhomes.rest.techpark.expansion.ApplyEntrySizeUnit}}</li>
 * <li>areaSize：地点大小，选择平方米估计就有的选</li>
 * <li>communityId：园区ID</li>
 * <li>namespaceId：命名空间</li>
 * <li>description：随便写一点什么</li> 
 * <li>contractId：合同id-如果有的话</li> 
 * </ul>
 */
public class EnterpriseApplyEntryCommand {
	
	private Long sourceId ; 
	
	private String sourceType;
	
	private String enterpriseName;
	
	private Long enterpriseId;
	
	private String applyUserName;
	
	private String contactPhone;
	
	@NotNull
	private Byte applyType;
	 
	private Byte sizeUnit;
	
	private Double areaSize;
	
	private Long communityId;
	
	private Integer namespaceId;

	private String description;

	private Long contractId;

	private Byte issuerType;

	public String getSourceType() {
		return sourceType;
	}



	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}



	public Byte getApplyType() {
		return applyType;
	}



	public void setApplyType(Byte applyType) {
		this.applyType = applyType;
	}



	public Long getCommunityId() {
		return communityId;
	}



	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}



	public Integer getNamespaceId() {
		return namespaceId;
	}



	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}



	public Long getSourceId() {
		return sourceId;
	}



	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}



	public String getEnterpriseName() {
		return enterpriseName;
	}



	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}




	public Long getEnterpriseId() {
		return enterpriseId;
	}



	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}



	public String getApplyUserName() {
		return applyUserName;
	}



	public void setApplyUserName(String applyUserName) {
		this.applyUserName = applyUserName;
	}



	public Byte getSizeUnit() {
		return sizeUnit;
	}



	public void setSizeUnit(Byte sizeUnit) {
		this.sizeUnit = sizeUnit;
	}



	public Double getAreaSize() {
		return areaSize;
	}



	public void setAreaSize(Double areaSize) {
		this.areaSize = areaSize;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}





	public String getContactPhone() {
		return contactPhone;
	}



	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}



	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }



	public Long getContractId() {
		return contractId;
	}



	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}

}
