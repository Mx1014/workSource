// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <li>doorId[Long]: 门禁id,必填</li>
 * <li>serverId[Long]: 服务器id,必填</li>
 * <li>internalCameraIds: 入口摄像头id列表</li>
 * <li>externalCameraIds: 出口摄像头id列表</li>
 * <li>internalIpadIds: 入口Ipadid列表</li>
 * <li>externalIpadIds: 出口Ipadid列表</li>
 */
public class UpdateCameraIpadBatchCommand {
	private Long doorId;
	private Long serverId;
	private List<Long> internalCameraIds;
	private List<Long> externalCameraIds;
	private List<Long> internalIpadIds;
	private List<Long> externalIpadIds;
	public Long getDoorId() {
		return doorId;
	}
	public void setDoorId(Long doorId) {
		this.doorId = doorId;
	}
	public Long getServerId() {
		return serverId;
	}
	public void setServerId(Long serverId) {
		this.serverId = serverId;
	}
	public List<Long> getInternalCameraIds() {
		return internalCameraIds;
	}
	public void setInternalCameraIds(List<Long> internalCameraIds) {
		this.internalCameraIds = internalCameraIds;
	}
	public List<Long> getExternalCameraIds() {
		return externalCameraIds;
	}
	public void setExternalCameraIds(List<Long> externalCameraIds) {
		this.externalCameraIds = externalCameraIds;
	}
	public List<Long> getInternalIpadIds() {
		return internalIpadIds;
	}
	public void setInternalIpadIds(List<Long> internalIpadIds) {
		this.internalIpadIds = internalIpadIds;
	}
	public List<Long> getExternalIpadIds() {
		return externalIpadIds;
	}
	public void setExternalIpadIds(List<Long> externalIpadIds) {
		this.externalIpadIds = externalIpadIds;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
