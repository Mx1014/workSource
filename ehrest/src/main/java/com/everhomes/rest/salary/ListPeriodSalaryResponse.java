// @formatter:off
package com.everhomes.rest.salary;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>salaryPeriodGroups: 本期薪酬批次列表，参考{@link com.everhomes.rest.salary.SalaryPeriodGroupDTO}</li>
 * </ul>
 */
public class ListPeriodSalaryResponse {

	@ItemType(SalaryPeriodGroupDTO.class)
	private List<SalaryPeriodGroupDTO> salaryPeriodGroups;

	public ListPeriodSalaryResponse() {

	}

	public ListPeriodSalaryResponse(List<SalaryPeriodGroupDTO> salaryPeriodGroups) {
		super();
		this.salaryPeriodGroups = salaryPeriodGroups;
	}

	public List<SalaryPeriodGroupDTO> getSalaryPeriodGroups() {
		return salaryPeriodGroups;
	}

	public void setSalaryPeriodGroups(List<SalaryPeriodGroupDTO> salaryPeriodGroups) {
		this.salaryPeriodGroups = salaryPeriodGroups;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
