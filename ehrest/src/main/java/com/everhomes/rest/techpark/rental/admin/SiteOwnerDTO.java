package com.everhomes.rest.techpark.rental.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 列出资源列表返回值(根据图标和园区)
 * <li>ownerType: 所属者类型， community， organization</li>
 * <li>ownerId: 所属者id</li>
 * </ul>
 */
public class SiteOwnerDTO {
	private String ownerType;
	private Long ownerId;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

}