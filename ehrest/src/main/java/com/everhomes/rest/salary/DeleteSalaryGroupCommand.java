// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>salaryGroupId: 薪酬批次id</li>
 * </ul>
 */
public class DeleteSalaryGroupCommand {

	private Long salaryGroupId;

	public DeleteSalaryGroupCommand() {

	}

	public DeleteSalaryGroupCommand(Long salaryGroupId) {
		super();
		this.salaryGroupId = salaryGroupId;
	}

	public Long getSalaryGroupId() {
		return salaryGroupId;
	}

	public void setSalaryGroupId(Long salaryGroupId) {
		this.salaryGroupId = salaryGroupId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
