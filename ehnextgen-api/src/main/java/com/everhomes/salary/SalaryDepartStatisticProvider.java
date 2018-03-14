// @formatter:off
package com.everhomes.salary;

import java.util.List;

public interface SalaryDepartStatisticProvider {

	void createSalaryDepartStatistic(SalaryDepartStatistic salaryDepartStatistic);

	void updateSalaryDepartStatistic(SalaryDepartStatistic salaryDepartStatistic);

	SalaryDepartStatistic findSalaryDepartStatisticById(Long id);

	List<SalaryDepartStatistic> listSalaryDepartStatistic();

	void deleteSalaryDepartStatistic(Long ownerId, byte isFile, String month);

	SalaryDepartStatistic findSalaryDepartStatisticByDptAndMonth(Long id, String format);

	List<SalaryDepartStatistic> listSalaryDepartStatistic(Long ownerId, String month);
}