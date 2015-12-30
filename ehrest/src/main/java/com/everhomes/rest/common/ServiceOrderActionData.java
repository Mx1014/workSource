package com.everhomes.rest.common;

import com.everhomes.util.StringHelper;

// {"ownerType":"organization","entityTag":"GACW"}
public class ServiceOrderActionData {
	private String ownerType;
	private String entityTag;
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
	public String getEntityTag() {
		return entityTag;
	}
	public void setEntityTag(String entityTag) {
		this.entityTag = entityTag;
	}
	
}
