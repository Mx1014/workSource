// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>listDoorAuth:授权列表</li>
 * <li>listPhotos:照片列表</li>
 * </ul>
 *
 */
public class SyncLocalVistorDataResponse {
	private List<DoorAuthDTO> listDoorAuth;
	private List<FaceRecognitionPhotoDTO> listPhotos;
	public List<DoorAuthDTO> getListDoorAuth() {
		return listDoorAuth;
	}
	public void setListDoorAuth(List<DoorAuthDTO> listDoorAuth) {
		this.listDoorAuth = listDoorAuth;
	}
	public List<FaceRecognitionPhotoDTO> getListPhotos() {
		return listPhotos;
	}
	public void setListPhotos(List<FaceRecognitionPhotoDTO> listPhotos) {
		this.listPhotos = listPhotos;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
