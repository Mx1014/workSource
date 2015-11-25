package com.everhomes.techpark.expansion;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

public class EnterpriseApplyEntryDTO {
	
	private Long id;
	
	private String sourceType; //1:enterprise 2:chuangke space 
	
	private Long sourceId; 
	
	private String enterpriseName;
	
	private Long enterpriseId;
	
	private String applyContact;
	
	private Long applyUserId;
	
	private String applyUserName;
	
	private String applyType;
	
	private String sizeUnit;
	
	private String status;
	
	private Double areaSize;
	
	private String description;
	
	private Timestamp createTime;

	


	public Long getId() {
		return id;
	}




	public void setId(Long id) {
		this.id = id;
	}




	public String getSourceType() {
		return sourceType;
	}




	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
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




	public String getApplyContact() {
		return applyContact;
	}




	public void setApplyContact(String applyContact) {
		this.applyContact = applyContact;
	}




	public Long getApplyUserId() {
		return applyUserId;
	}




	public void setApplyUserId(Long applyUserId) {
		this.applyUserId = applyUserId;
	}




	public String getApplyUserName() {
		return applyUserName;
	}




	public void setApplyUserName(String applyUserName) {
		this.applyUserName = applyUserName;
	}




	public String getApplyType() {
		return applyType;
	}




	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}




	public String getSizeUnit() {
		return sizeUnit;
	}




	public void setSizeUnit(String sizeUnit) {
		this.sizeUnit = sizeUnit;
	}




	public String getStatus() {
		return status;
	}




	public void setStatus(String status) {
		this.status = status;
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




	public Timestamp getCreateTime() {
		return createTime;
	}




	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}




	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
