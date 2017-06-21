// @formatter:off
package com.everhomes.rest.salary;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>salaryDefaultEntries: 基础字段参考{@link com.everhomes.rest.salary.SalaryDefaultEntityDTO}</li>
 * </ul>
 */
public class ListSalaryDefaultEntitiesResponse {

	@ItemType(SalaryDefaultEntityDTO.class)
	private List<SalaryDefaultEntityDTO> salaryDefaultEntries;

	public ListSalaryDefaultEntitiesResponse() {

	}

	public ListSalaryDefaultEntitiesResponse(List<SalaryDefaultEntityDTO> salaryDefaultEntries) {
		super();
		this.salaryDefaultEntries = salaryDefaultEntries;
	}

	public List<SalaryDefaultEntityDTO> getSalaryDefaultEntries() {
		return salaryDefaultEntries;
	}

	public void setSalaryDefaultEntries(List<SalaryDefaultEntityDTO> salaryDefaultEntries) {
		this.salaryDefaultEntries = salaryDefaultEntries;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
