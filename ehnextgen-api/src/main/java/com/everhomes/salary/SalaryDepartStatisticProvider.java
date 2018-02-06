// @formatter:off
package com.everhomes.salary;

import java.util.List;

public interface SalaryDepartStatisticProvider {

	void createSalaryDepartStatistic(SalaryDepartStatistic salaryDepartStatistic);

	void updateSalaryDepartStatistic(SalaryDepartStatistic salaryDepartStatistic);

	SalaryDepartStatistic findSalaryDepartStatisticById(Long id);

	List<SalaryDepartStatistic> listSalaryDepartStatistic();

}