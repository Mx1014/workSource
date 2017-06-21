// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>organizationId: 公司id</li>
 * <li>attachment: 文件</li>
 * </ul>
 */
public class ImportPeriodSalaryCommand {

	private Long organizationId;

	public ImportPeriodSalaryCommand() {

	}

	public ImportPeriodSalaryCommand(Long organizationId) {
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
