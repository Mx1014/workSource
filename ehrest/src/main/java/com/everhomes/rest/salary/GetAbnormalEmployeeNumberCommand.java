// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>ownerType: 所属类型:Organization</li>
 * <li>ownerId: 所属id</li>
 * </ul>
 */
public class GetAbnormalEmployeeNumberCommand {

	private String ownerType;

	private Long ownerId;

	public GetAbnormalEmployeeNumberCommand() {

	}

	public GetAbnormalEmployeeNumberCommand(String ownerType, Long ownerId) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
