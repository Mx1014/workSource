package com.everhomes.rest.yellowPage;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li> ownerType: 拥有者类型：现在是comunity</li>
 *  <li> ownerId: 拥有者ID</li>
 * </ul>
 */
public class GetServiceAllianceCommand {

	private String ownerType;
	
	@NotNull
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
