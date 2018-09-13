package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: ownerType eq organization</li>
 * <li>ownerId: ownerId 公司id</li>
 * </ul>
 */
public class GetOvertimeInfoCommand {
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
