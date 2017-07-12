package com.everhomes.rest.rentalv2.admin;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 查某资源的规则 
 * <li>resourceId: resource id</li>
 * </ul>
 */
public class GetResourceRuleAdminCommand {
	@NotNull
	private Long resourceId;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}
}
