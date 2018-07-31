// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>同步失败的设备列表
 * <li>listDoorAccess：门禁列表 {@link com.everhomes.rest.aclink.DoorAccessDTO}</li>
 * <li>listCamera：摄像头列表 {@link com.everhomes.rest.aclink.AclinkCameraDTO}</li>
 * <li>listIpad：ipad列表 {@link com.everhomes.rest.aclink.AclinkIPadDTO}</li>
 * </ul>
 */
public class SyncLocalServerResponse {
	private List<DoorAccessDTO> listDoorAccess;
	private List<AclinkCameraDTO> listCamera;
	private List<AclinkIPadDTO> listIpad;
	
	public List<DoorAccessDTO> getListDoorAccess() {
		return listDoorAccess;
	}

	public void setListDoorAccess(List<DoorAccessDTO> listDoorAccess) {
		this.listDoorAccess = listDoorAccess;
	}

	public List<AclinkCameraDTO> getListCamera() {
		return listCamera;
	}

	public void setListCamera(List<AclinkCameraDTO> listCamera) {
		this.listCamera = listCamera;
	}

	public List<AclinkIPadDTO> getListIpad() {
		return listIpad;
	}

	public void setListIpad(List<AclinkIPadDTO> listIpad) {
		this.listIpad = listIpad;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
