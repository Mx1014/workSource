package com.everhomes.rest.address;

import javax.validation.constraints.NotNull;

public class ListApartmentFloorCommand {
	@NotNull
    private Long communityId;
	@NotNull
    private String buildingName;
    private String keyword;
    private Integer namespaceId;
    
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
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
}
