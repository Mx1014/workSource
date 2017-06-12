// @formatter:off
package com.everhomes.rest.talent;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>organizationId: 管理公司id</li>
 * </ul>
 */
public class ListTalentQueryHistoryCommand {
	@NotNull
	private Long organizationId;

	public ListTalentQueryHistoryCommand() {

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
