package com.everhomes.yellowPage;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.user.RequestAttachments;
import com.everhomes.util.StringHelper;

public class ServiceAllianceRequestsFields {

	private String name;
	
	private String mobile;
	
	private String organizationName;
	
	private String cityName;
	
	private String industry;
	
	private String financingStage;
	
	private BigDecimal financingAmount;
	
	private Double transferShares;
	
	private String projectDesc;
	
	private Timestamp createTime;
	
	@ItemType(RequestAttachments.class)
	private List<RequestAttachments> attachments;

	public List<RequestAttachments> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<RequestAttachments> attachments) {
		this.attachments = attachments;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getFinancingStage() {
		return financingStage;
	}

	public void setFinancingStage(String financingStage) {
		this.financingStage = financingStage;
	}

	public BigDecimal getFinancingAmount() {
		return financingAmount;
	}

	public void setFinancingAmount(BigDecimal financingAmount) {
		this.financingAmount = financingAmount;
	}

	public Double getTransferShares() {
		return transferShares;
	}

	public void setTransferShares(Double transferShares) {
		this.transferShares = transferShares;
	}

	public String getProjectDesc() {
		return projectDesc;
	}

	public void setProjectDesc(String projectDesc) {
		this.projectDesc = projectDesc;
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
