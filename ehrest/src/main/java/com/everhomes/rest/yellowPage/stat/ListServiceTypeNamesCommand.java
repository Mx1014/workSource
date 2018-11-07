package com.everhomes.rest.yellowPage.stat;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: ownerType</li>
 * <li>ownerId: ownerId</li>
 * <li>type: 服务联盟类型id</li>
 * <li>parentId: parentId 兼容前端</li>
 * <li>currentPMId: 管理公司id</li>
 * <li>currentProjectId: 当前项目id</li>
 * <li>appId: 应用的originId</li>
 * </ul>
 */
public class ListServiceTypeNamesCommand {
	
	private String ownerType;
	private Long ownerId;
	private Long type;
	private Long parentId;
	
	@NotNull
	private Long currentPMId;
	@NotNull
	private Long currentProjectId;
	@NotNull
	private Long appId;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getType() {
		if (null == type) {
			return parentId;
		}
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public Long getCurrentPMId() {
		return currentPMId;
	}

	public void setCurrentPMId(Long currentPMId) {
		this.currentPMId = currentPMId;
	}

	public Long getCurrentProjectId() {
		return currentProjectId;
	}

	public void setCurrentProjectId(Long currentProjectId) {
		this.currentProjectId = currentProjectId;
	}

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
}

