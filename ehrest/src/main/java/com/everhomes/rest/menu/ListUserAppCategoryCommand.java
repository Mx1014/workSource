// @formatter:off
package com.everhomes.rest.menu;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>organizationId: organizationId</li>
 * </ul>
 */
public class ListUserAppCategoryCommand {

	private Long organizationId;

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
