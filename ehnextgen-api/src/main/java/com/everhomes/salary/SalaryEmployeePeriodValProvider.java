// @formatter:off
package com.everhomes.salary;

import java.util.List;

public interface SalaryEmployeePeriodValProvider {

	void createSalaryEmployeePeriodVal(SalaryEmployeePeriodVal salaryEmployeePeriodVal);

	void updateSalaryEmployeePeriodVal(SalaryEmployeePeriodVal salaryEmployeePeriodVal);

	SalaryEmployeePeriodVal findSalaryEmployeePeriodValById(Long id);

	List<SalaryEmployeePeriodVal> listSalaryEmployeePeriodVal();

	Integer countSalaryEmployeePeriodsByPeriodAndEntity(String ownerType, Long ownerId,
			String period, Long entityIdShifa);

}