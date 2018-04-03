// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>salaryGroupEntries: 薪酬组选项</li>
 * </ul>
 */
public class UpdateSalaryGroupResponse {

	private SalaryGroupEntityDTO salaryGroupEntity;

	public UpdateSalaryGroupResponse() {

	}

	public UpdateSalaryGroupResponse(SalaryGroupEntityDTO salaryGroupEntity) {
		super();
		this.salaryGroupEntity = salaryGroupEntity;
	}

	public SalaryGroupEntityDTO getSalaryGroupEntry() {
		return salaryGroupEntity;
	}

	public void setSalaryGroupEntry(SalaryGroupEntityDTO salaryGroupEntity) {
		this.salaryGroupEntity = salaryGroupEntity;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
