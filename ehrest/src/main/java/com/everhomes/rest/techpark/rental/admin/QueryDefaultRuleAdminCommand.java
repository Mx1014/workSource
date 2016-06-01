package com.everhomes.rest.techpark.rental.admin;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * 添加默认规则
 * <li>ownerType: 所有者类型 参考
 * {@link com.everhomes.rest.techpark.rental.RentalOwnerType}</li>
 * <li>ownerId: 园区id</li>
 * <li>launchPadItemId: 图标id</li>
 * </ul>
 */
public class QueryDefaultRuleAdminCommand {
	@NotNull
	private String ownerType;
	@NotNull
	private Long ownerId;
	@NotNull
	private Long launchPadItemId;

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

	public Long getLaunchPadItemId() {
		return launchPadItemId;
	}

	public void setLaunchPadItemId(Long launchPadItemId) {
		this.launchPadItemId = launchPadItemId;
	}

}
