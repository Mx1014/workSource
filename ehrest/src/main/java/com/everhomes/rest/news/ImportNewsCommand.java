// @formatter:off

package com.everhomes.rest.news;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 参数
 * <li>ownerType: 所属类型，参考{@link com.everhomes.rest.news.NewsOwnerType}</li>
 * <li>ownerId: 所属类型ID</li>
 * <li>categoryId: 新闻类型ID</li>
 * <li>currentPMId: 当前管理公司ID</li>
 * <li>currentProjectId: 当前选中项目Id，如果是全部则不传</li>
 * <li>appId: 应用id</li>
 * </ul>
 */
public class ImportNewsCommand {
	@NotNull
	private String ownerType;
	@NotNull
	private Long ownerId;
	private Long categoryId;
	private Long currentPMId;
	@NotNull
	private Long currentProjectId;
	@NotNull
	private Long appId;

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

}