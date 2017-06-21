// @formatter:off
package com.everhomes.rest.salary;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>salaryDefaultEntries: 基础字段参考{@link com.everhomes.rest.salary.SalaryDefaultEntriesDTO}</li>
 * </ul>
 */
public class ListSalaryDefaultEntriesResponse {

	@ItemType(SalaryDefaultEntriesDTO.class)
	private List<SalaryDefaultEntriesDTO> salaryDefaultEntries;

	public ListSalaryDefaultEntriesResponse() {

	}

	public ListSalaryDefaultEntriesResponse(List<SalaryDefaultEntriesDTO> salaryDefaultEntries) {
		super();
		this.salaryDefaultEntries = salaryDefaultEntries;
	}

	public List<SalaryDefaultEntriesDTO> getSalaryDefaultEntries() {
		return salaryDefaultEntries;
	}

	public void setSalaryDefaultEntries(List<SalaryDefaultEntriesDTO> salaryDefaultEntries) {
		this.salaryDefaultEntries = salaryDefaultEntries;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
