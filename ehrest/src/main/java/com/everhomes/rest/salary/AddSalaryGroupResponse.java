// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * 
 * <ul>返回值:
 * <li>salaryGroupEntries: 薪酬组选项</li>
 * </ul>
 */
public class AddSalaryGroupResponse {

	@ItemType(SalaryGroupEntityDTO.class)
	private List<SalaryGroupEntityDTO> salaryGroupEntry;

	public AddSalaryGroupResponse() {

	}

	public List<SalaryGroupEntityDTO> getSalaryGroupEntry() {
		return salaryGroupEntry;
	}

	public void setSalaryGroupEntry(List<SalaryGroupEntityDTO> salaryGroupEntry) {
		this.salaryGroupEntry = salaryGroupEntry;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
