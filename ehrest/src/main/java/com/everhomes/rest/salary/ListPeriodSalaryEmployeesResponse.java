// @formatter:off
package com.everhomes.rest.salary;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>salaryGroupEntitys: 字段项列表 参考{@link com.everhomes.rest.salary.SalaryGroupEntityDTO}</li>
 * <li>salaryPeriodEmployees: 某薪酬批次的员工核算列表参考{@link com.everhomes.rest.salary.SalaryPeriodEmployeeDTO}</li>
 * </ul>
 */
public class ListPeriodSalaryEmployeesResponse {


	@ItemType(SalaryGroupEntityDTO.class)
	private List<SalaryGroupEntityDTO> salaryGroupEntitys;
	
	@ItemType(SalaryPeriodEmployeeDTO.class)
	private List<SalaryPeriodEmployeeDTO> salaryPeriodEmployees;

	public ListPeriodSalaryEmployeesResponse() {

	}

	public ListPeriodSalaryEmployeesResponse(List<SalaryPeriodEmployeeDTO> salaryPeriodEmployees) {
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

	public List<SalaryGroupEntityDTO> getSalaryGroupEntitys() {
		return salaryGroupEntitys;
	}

	public void setSalaryGroupEntitys(List<SalaryGroupEntityDTO> salaryGroupEntitys) {
		this.salaryGroupEntitys = salaryGroupEntitys;
	}

}
