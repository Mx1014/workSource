package com.everhomes.rest.organization.pm.reportForm;

import com.everhomes.util.StringHelper;

/**
 * livingStatus:房源状态{com.everhomes.rest.organization.pm.AddressMappingStatus}
 */

public class ApartmentReportFormDTO {
	
	private Long id;
	private Long communityId;
	private String communityName;
	private Long buildingId;
	private String buildingName;
	private Byte livingStatus;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public Long getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
	}
	public String getBuildingName() {
		return buildingName;
	}
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	public Byte getLivingStatus() {
		return livingStatus;
	}
	public void setLivingStatus(Byte livingStatus) {
		this.livingStatus = livingStatus;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
