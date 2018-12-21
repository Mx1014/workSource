// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.util.StringHelper;

public class ListPhotoSyncResultResponse {
	private List<PhotoSyncResultDTO> photoSyncResultList;

	public List<PhotoSyncResultDTO> getPhotoSyncResultList() {
		return photoSyncResultList;
	}

	public void setPhotoSyncResultList(List<PhotoSyncResultDTO> photoSyncResultList) {
		this.photoSyncResultList = photoSyncResultList;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
