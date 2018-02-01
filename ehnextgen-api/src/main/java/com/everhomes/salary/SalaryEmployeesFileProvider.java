// @formatter:off
package com.everhomes.salary;

import java.util.List;

public interface SalaryEmployeesFileProvider {

	void createSalaryEmployeesFile(SalaryEmployeesFile salaryEmployeesFile);

	void updateSalaryEmployeesFile(SalaryEmployeesFile salaryEmployeesFile);

	SalaryEmployeesFile findSalaryEmployeesFileById(Long id);

	List<SalaryEmployeesFile> listSalaryEmployeesFile();

}