// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>获取摄像头列表
 * <li> nextPageAnchor: 下一页锚点 </li>
 * <li> aclinkCameras: ipad列表，参考{@link com.everhomes.rest.aclink.AclinkCameraDTO}</li>
 * </ul>
 */
public class ListLocalCamerasResponse {
	private Long nextPageAnchor;
	
	@ItemType(AclinkCameraDTO.class)
	private List<AclinkCameraDTO> aclinkCameras;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<AclinkCameraDTO> getAclinkCameras() {
		return aclinkCameras;
	}

	public void setAclinkCameras(List<AclinkCameraDTO> aclinkCameras) {
		this.aclinkCameras = aclinkCameras;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
