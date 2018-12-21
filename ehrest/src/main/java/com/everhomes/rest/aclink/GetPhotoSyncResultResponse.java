// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

public class GetPhotoSyncResultResponse {
	private Byte faceSyncStatus;

	public Byte getFaceSyncStatus() {
		return faceSyncStatus;
	}
	public void setFaceSyncStatus(Byte faceSyncStatus) {
		this.faceSyncStatus = faceSyncStatus;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
