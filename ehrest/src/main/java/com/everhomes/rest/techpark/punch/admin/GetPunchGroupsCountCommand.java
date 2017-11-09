// @formatter:off
package com.everhomes.rest.techpark.punch.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType：organization/user</li>
 * <li>ownerId：id</li>
 * </ul>
 */
public class GetPunchGroupsCountCommand {
 
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
