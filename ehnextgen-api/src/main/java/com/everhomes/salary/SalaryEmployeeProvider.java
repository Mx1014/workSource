// @formatter:off
package com.everhomes.salary;

import java.util.List;

public interface SalaryEmployeeProvider {

	void createSalaryEmployee(SalaryEmployee salaryEmployee);

	void updateSalaryEmployee(SalaryEmployee salaryEmployee);

	SalaryEmployee findSalaryEmployeeById(Long id);

	List<SalaryEmployee> listSalaryEmployee();

	List<SalaryEmployee> listSalaryEmployeeByPeriodGroupId(Long salaryPeriodGroupId);

}