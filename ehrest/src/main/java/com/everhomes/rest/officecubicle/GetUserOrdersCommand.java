// @formatter:off
package com.everhomes.rest.officecubicle;

import com.everhomes.util.StringHelper;

public class GetUserOrdersCommand {
	private String ownerType;
	private Long ownerId;
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
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
