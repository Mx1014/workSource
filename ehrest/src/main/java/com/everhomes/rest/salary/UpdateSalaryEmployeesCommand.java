// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * 
 * <ul>参数:
 * <li>employeeOriginVal: 个人批次设定</li>
 * </ul>
 */
public class UpdateSalaryEmployeesCommand {

	private List<SalaryEmployeeOriginValDTO> employeeOriginVal;


	public UpdateSalaryEmployeesCommand() {

	}

	public List<SalaryEmployeeOriginValDTO> getEmployeeOriginVal() {
		return employeeOriginVal;
	}

	public void setEmployeeOriginVal(List<SalaryEmployeeOriginValDTO> employeeOriginVal) {
		this.employeeOriginVal = employeeOriginVal;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
