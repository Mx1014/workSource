package com.everhomes.rest.yellowPage;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li> ownerType: 拥有者类型：现在是comunity</li>
 *  <li> ownerId: 拥有者ID</li>
 *  <li> type:类型  </li>
 * </ul>
 */
public class GetServiceAllianceCommand {

	private String ownerType;
	
	@NotNull
	private Long ownerId;
	
	private Long type;
	
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

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
