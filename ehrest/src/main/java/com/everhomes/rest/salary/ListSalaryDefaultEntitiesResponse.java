// @formatter:off
package com.everhomes.rest.salary;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>salaryDefaultEntities: 基础字段参考{@link com.everhomes.rest.salary.SalaryDefaultEntityDTO}</li>
 * </ul>
 */
public class ListSalaryDefaultEntitiesResponse {

	@ItemType(SalaryDefaultEntityDTO.class)
	private List<SalaryDefaultEntityDTO> salaryDefaultEntities;

	public ListSalaryDefaultEntitiesResponse() {

	}

	public ListSalaryDefaultEntitiesResponse(List<SalaryDefaultEntityDTO> salaryDefaultEntities) {
		super();
		this.salaryDefaultEntities = salaryDefaultEntities;
	}

	public List<SalaryDefaultEntityDTO> getSalaryDefaultEntities() {
		return salaryDefaultEntities;
	}

	public void setSalaryDefaultEntities(List<SalaryDefaultEntityDTO> salaryDefaultEntities) {
		this.salaryDefaultEntities = salaryDefaultEntities;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
