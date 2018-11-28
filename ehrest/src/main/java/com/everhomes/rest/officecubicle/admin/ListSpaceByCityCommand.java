package com.everhomes.rest.officecubicle.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * </ul>
 */
public class ListSpaceByCityCommand {
	private Integer namespaceId;
	private Long ownerId;
	private String ownerType;
	private Long cityId;
	private String cityName;
	private Long pageAnchor;
    
	private Integer pageSize;

	
	public Long getPageAnchor() {
		return pageAnchor;
	}




	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}




	public Integer getPageSize() {
		return pageSize;
	}




	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}




	public Long getCityId() {
		return cityId;
	}




	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}




	public String getCityName() {
		return cityName;
	}




	public void setCityName(String cityName) {
		this.cityName = cityName;
	}




	public Integer getNamespaceId() {
		return namespaceId;
	}




	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}




	public Long getOwnerId() {
		return ownerId;
	}




	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}




	public String getOwnerType() {
		return ownerType;
	}




	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}




	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
