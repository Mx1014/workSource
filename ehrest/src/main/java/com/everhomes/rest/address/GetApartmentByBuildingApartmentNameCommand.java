package com.everhomes.rest.address;

import javax.validation.constraints.NotNull;

public class GetApartmentByBuildingApartmentNameCommand {
	@NotNull
    private Long communityId;
    
    @NotNull
    private String buildingName;
    
    @NotNull
    private String apartmentName;

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getApartmentName() {
		return apartmentName;
	}

	public void setApartmentName(String apartmentName) {
		this.apartmentName = apartmentName;
	}
    
    
}
