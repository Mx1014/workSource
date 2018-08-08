package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerId:所属组织id</li>
 * <li>ownerType:所属组织类型 {@link com.everhomes.rest.aclink.DoorAccessOwnerType}</li>
 * </ul>
 * @author liuyilin
 *
 */
public class ListLocalServerByOrgCommand {
	private Long ownerId;
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
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
