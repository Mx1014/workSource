package com.everhomes.rest.yellowPage;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>type: 服务联盟类型id</li>
 * <li>ownerId: 归属项目id</li>
 * <li>categoryId: 服务类型</li>
 * <li>currentPMId: 管理公司id</li>
 * <li>currentProjectId: 当前项目id</li>
 * <li>appId: 应用的originId</li>
 * </ul>
 */
public class ListServiceNamesCommand {
	private Long type;
	private Long ownerId;
	private Long categoryId;
	
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
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
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
}
