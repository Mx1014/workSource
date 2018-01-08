package com.everhomes.rest.rentalv2.admin;

import javax.validation.constraints.NotNull;

import com.everhomes.rest.rentalv2.RentalV2ResourceType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 查某资源的规则 
 * <li>resourceId: resource id</li>
 * <li>resourceType: resourceType {@link RentalV2ResourceType}</li>
 * </ul>
 */
public class GetResourceRuleAdminCommand {
	@NotNull
	private Long resourceId;

	private String resourceType;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}
}
