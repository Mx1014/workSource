// @formatter:off
package com.everhomes.salary;

import java.util.List;

public interface SalaryEmployeesFileProvider {

	void createSalaryEmployeesFile(SalaryEmployeesFile salaryEmployeesFile);

	void updateSalaryEmployeesFile(SalaryEmployeesFile salaryEmployeesFile);

	SalaryEmployeesFile findSalaryEmployeesFileById(Long id);

	List<SalaryEmployeesFile> listSalaryEmployeesFile();

	void deleteEmployeesFile(Long ownerId, String month);

	SalaryEmployeesFile findSalaryEmployeesFileByDetailIDAndMonth(Long ownerId, Long detailId, String month);
}