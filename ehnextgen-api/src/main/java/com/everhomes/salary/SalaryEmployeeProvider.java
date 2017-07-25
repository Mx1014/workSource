// @formatter:off
package com.everhomes.salary;

import com.everhomes.listing.CrossShardListingLocator;

import java.util.List;

public interface SalaryEmployeeProvider {

	void createSalaryEmployee(SalaryEmployee salaryEmployee);

	void updateSalaryEmployee(SalaryEmployee salaryEmployee);

	SalaryEmployee findSalaryEmployeeById(Long id);

	List<SalaryEmployee> listSalaryEmployee();

	List<SalaryEmployee> listSalaryEmployeeByPeriodGroupId(Long salaryPeriodGroupId);

	int countUnCheckEmployee(Long salaryPeriodGroupId);

	List<SalaryEmployee> listSalaryEmployees(List<Long> userIds, List<String> periods);

	void updateSalaryEmployeeCheckFlag(List<Long> salaryEmployeeIds, Byte checkFlag);

	List<SalaryEmployee> listSalaryEmployees(Long salaryPeriodGroupId, List<Long> userIds, Byte checkFlag, CrossShardListingLocator locator, int pageSize);

	void deleteSalaryEmployee(Long ownerId, Long detail_id, Long salaryGroupId);

	void deleteSalaryEmployee(SalaryEmployee employee);

	Integer countSalaryEmployeesByStatus(Long salaryPeriodGroupId,  Byte code);

	SalaryEmployee findSalaryEmployeeBySalaryGroupIdAndDetailId(Long salaryGroupId, Long memberId);
}