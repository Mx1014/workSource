// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerId:所属组织Id</li>
 * <li>ownerType：所属组织类型 0小区 1企业 2家庭{@link com.everhomes.rest.aclink.DoorAccessOwnerType}</li>
 * <li>groupId：门禁组Id</li>
 * </ul>
 *
 */
public class ListDoorAccessByGroupIdCommand {
	private Long ownerId;
	private Byte ownerType;
	private Long groupId;
	
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

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
