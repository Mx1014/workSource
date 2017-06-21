// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>salaryGroupId: 薪酬批次id</li>
 * <li>organizationId: 公司id</li>
 * <li>attachment: 文件</li>
 * </ul>
 */
public class ImportSalaryGroupCommand {

	private Long salaryGroupId;

	private Long organizationId;


	public ImportSalaryGroupCommand() {

	}

	public ImportSalaryGroupCommand(Long salaryGroupId, Long organizationId) {
		super();
		this.salaryGroupId = salaryGroupId;
		this.organizationId = organizationId;
	}

	public Long getSalaryGroupId() {
		return salaryGroupId;
	}

	public void setSalaryGroupId(Long salaryGroupId) {
		this.salaryGroupId = salaryGroupId;
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
