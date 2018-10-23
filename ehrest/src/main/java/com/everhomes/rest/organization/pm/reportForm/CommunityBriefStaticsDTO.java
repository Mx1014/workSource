package com.everhomes.rest.organization.pm.reportForm;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;

public class CommunityBriefStaticsDTO {
	
	private Long communityId;
	private String communityName;
	private String category;
	private Integer buildingCount;
	private BigDecimal areaSize;
	
	public Long getCommunityId() {
		return communityId;
	}
	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	public String getCommunityName() {
		return communityName;
	}
	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Integer getBuildingCount() {
		return buildingCount;
	}
	public void setBuildingCount(Integer buildingCount) {
		this.buildingCount = buildingCount;
	}
	public BigDecimal getAreaSize() {
		return areaSize;
	}
	public void setAreaSize(BigDecimal areaSize) {
		this.areaSize = areaSize;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	

}
