// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>userId:用户Id</li>
 * <li>ownerId:所属机构Id</li>
 * <li>ownerType:所属机构类型 0小区 1企业 2家庭 {@link com.everhomes.rest.aclink.DoorAccessOwnerType.java}</li>
 * </ul>
 *
 */
public class SyncLocalPhotoByUserIdCommand {
	private Long userId;
	private Long ownerId;
	private Byte ownerType;
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
