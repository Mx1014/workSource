package com.everhomes.rest.organization.pm;

import java.util.List;

import com.everhomes.util.StringHelper;

public class ListApartmentsByMultiStatusCommand {

	private Integer namespaceId;
    private Long communityId;
    private String buildingName;
    private String apartment;
    private List<Byte> livingStatus;
    private Integer pageSize;
    private Long pageAnchor;
    
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
	public String getBuildingName() {
		return buildingName;
	}
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	public String getApartment() {
		return apartment;
	}
	public void setApartment(String apartment) {
		this.apartment = apartment;
	}
	public List<Byte> getLivingStatus() {
		return livingStatus;
	}
	public void setLivingStatus(List<Byte> livingStatus) {
		this.livingStatus = livingStatus;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Long getPageAnchor() {
		return pageAnchor;
	}
	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}
    
    @Override
    public String toString() {
    	return StringHelper.toJsonString(this);
    }
	
}
