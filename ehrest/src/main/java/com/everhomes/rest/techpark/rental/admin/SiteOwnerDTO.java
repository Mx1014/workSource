package com.everhomes.rest.techpark.rental.admin;

/**
 * <ul>
 * 列出资源列表返回值(根据图标和园区)
 * <li>ownerType: 所属者类型， community， organization</li>
 * <li>ownerId: 所属者id</li>
 * </ul>
 */
public class SiteOwnerDTO {
	private String ownerType;
	private String ownerId;

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

}