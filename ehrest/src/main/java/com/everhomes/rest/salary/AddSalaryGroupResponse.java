// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>salaryGroupEntries: 薪酬组选项</li>
 * </ul>
 */
public class AddSalaryGroupResponse {

	private SalaryGroupEntityDTO salaryGroupEntry;

	public AddSalaryGroupResponse() {

	}

	public AddSalaryGroupResponse(SalaryGroupEntityDTO salaryGroupEntry) {
		super();
		this.salaryGroupEntry = salaryGroupEntry;
	}

	public SalaryGroupEntityDTO getSalaryGroupEntry() {
		return salaryGroupEntry;
	}

	public void setSalaryGroupEntry(SalaryGroupEntityDTO salaryGroupEntry) {
		this.salaryGroupEntry = salaryGroupEntry;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
