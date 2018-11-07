package com.everhomes.rest.yellowPage.standard;

import com.everhomes.util.StringHelper;

public class ConfigCommand {
	
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
