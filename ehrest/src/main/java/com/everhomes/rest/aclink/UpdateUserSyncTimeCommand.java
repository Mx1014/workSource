package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

public class UpdateUserSyncTimeCommand {
	private Long photoId;

	public Long getPhotoId() {
		return photoId;
	}

	public void setPhotoId(Long photoId) {
		this.photoId = photoId;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
