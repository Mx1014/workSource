// @formatter:off
package com.everhomes.rest.techpark.punch.admin;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>organizationId: 所属id 总公司id</li>
 * </ul>
 */
public class ImportVacationBalancesCommand {

	private Long organizationId;

	public ImportVacationBalancesCommand() {

	}

	public ImportVacationBalancesCommand(Long organizationId) {
		super();
		this.organizationId = organizationId;
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
