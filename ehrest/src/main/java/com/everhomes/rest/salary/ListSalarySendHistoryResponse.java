// @formatter:off
package com.everhomes.rest.salary;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>salaryPeriodEmployees: 某薪酬批次的员工核算列表参考{@link com.everhomes.rest.salary.SalaryPeriodEmployeeDTO}</li>
 * </ul>
 */
public class ListSalarySendHistoryResponse {

	@ItemType(SalaryPeriodEmployeeDTO.class)
	private List<SalaryPeriodEmployeeDTO> salaryPeriodEmployees;

	public ListSalarySendHistoryResponse() {

	}

	public ListSalarySendHistoryResponse(List<SalaryPeriodEmployeeDTO> salaryPeriodEmployees) {
		super();
		this.salaryPeriodEmployees = salaryPeriodEmployees;
	}

	public List<SalaryPeriodEmployeeDTO> getSalaryPeriodEmployees() {
		return salaryPeriodEmployees;
	}

	public void setSalaryPeriodEmployees(List<SalaryPeriodEmployeeDTO> salaryPeriodEmployees) {
		this.salaryPeriodEmployees = salaryPeriodEmployees;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
