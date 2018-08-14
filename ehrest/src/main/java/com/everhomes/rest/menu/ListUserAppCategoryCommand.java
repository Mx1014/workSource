// @formatter:off
package com.everhomes.rest.menu;

import com.everhomes.rest.common.PortalCommand;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>userId: userId</li>
 *     <li>organizationId: organizationId</li>
 * </ul>
 */
public class ListUserAppCategoryCommand {

	private Long userId;

	private Long organizationId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

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
