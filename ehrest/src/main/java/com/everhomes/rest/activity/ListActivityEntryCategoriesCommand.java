package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 *<li>ownerType:community</li>
 *<li>ownerId: 园区id</li>
 *</ul>
 */
public class ListActivityEntryCategoriesCommand {

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
