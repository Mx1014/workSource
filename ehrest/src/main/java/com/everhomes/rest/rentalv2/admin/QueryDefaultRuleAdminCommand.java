package com.everhomes.rest.rentalv2.admin;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 添加默认规则
 * <li>ownerType: 所有者类型 参考
 * {@link com.everhomes.rest.rentalv2.RentalOwnerType}</li>
 * <li>ownerId: 园区id</li>
 * <li>resourceTypeId: 图标id</li>
 * </ul>
 */
public class QueryDefaultRuleAdminCommand {
	@NotNull
	private String ownerType;
	@NotNull
	private Long ownerId;
	@NotNull
	private Long resourceTypeId;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
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

	public Long getResourceTypeId() {
		return resourceTypeId;
	}

	public void setResourceTypeId(Long resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
	}

}
