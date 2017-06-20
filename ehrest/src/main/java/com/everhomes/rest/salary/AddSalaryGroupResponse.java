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

	private SalaryGroupEntriesDTO salaryGroupEntries;

	public AddSalaryGroupResponse() {

	}

	public AddSalaryGroupResponse(SalaryGroupEntriesDTO salaryGroupEntries) {
		super();
		this.salaryGroupEntries = salaryGroupEntries;
	}

	public SalaryGroupEntriesDTO getSalaryGroupEntries() {
		return salaryGroupEntries;
	}

	public void setSalaryGroupEntries(SalaryGroupEntriesDTO salaryGroupEntries) {
		this.salaryGroupEntries = salaryGroupEntries;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
