package com.everhomes.techpark.expansion;


import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class EnterpriseApplyEntryCommand {
	
	private Long sourceId ; 
	
	private String sourceType;
	
	private String enterpriseName;
	
	private Long enterpriseId;
	
	private String applyUserName;
	
	private String contactPhone;
	
	@NotNull
	private Byte applyType;
	
	@NotNull
	private Byte sizeUnit;
	
	private Double areaSize;
	
	private Long communityId;
	
	private Integer namespaceId;

	private String description;


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

}
