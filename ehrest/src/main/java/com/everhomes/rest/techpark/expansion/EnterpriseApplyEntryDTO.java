package com.everhomes.rest.techpark.expansion;

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
	
	private Byte applyType;
	
	private Byte sizeUnit;
	
	private Byte status;
	
	private Double areaSize;
	
	private String description;
	
	private Timestamp createTime;

	private String sourceName;


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




	public Byte getApplyType() {
		return applyType;
	}




	public void setApplyType(Byte applyType) {
		this.applyType = applyType;
	}




	public Byte getSizeUnit() {
		return sizeUnit;
	}




	public void setSizeUnit(Byte sizeUnit) {
		this.sizeUnit = sizeUnit;
	}




	public Byte getStatus() {
		return status;
	}




	public void setStatus(Byte status) {
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




	public String getSourceName() {
		return sourceName;
	}




	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}




	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
