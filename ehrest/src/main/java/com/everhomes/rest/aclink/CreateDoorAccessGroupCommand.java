// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>groupName:门禁组名称</li>
 * </ul>
 */
public class CreateDoorAccessGroupCommand {
	@NotNull
	private String groupName;
	@NotNull
	private Long ownerId;
	@NotNull
	private Byte ownerType;

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Byte getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(Byte ownerType) {
		this.ownerType = ownerType;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
}
