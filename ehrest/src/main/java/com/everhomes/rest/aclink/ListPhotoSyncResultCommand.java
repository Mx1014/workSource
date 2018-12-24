// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

public class ListPhotoSyncResultCommand {
	private Long ownerId;
	private Byte ownerType;
	private Long userId;
	private String keyword;
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
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
