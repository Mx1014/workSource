// @formatter:off
package com.everhomes.salary;

import java.util.List;

public interface SalaryEmployeeOriginValProvider {

	void createSalaryEmployeeOriginVal(SalaryEmployeeOriginVal salaryEmployeeOriginVal);

	void updateSalaryEmployeeOriginVal(SalaryEmployeeOriginVal salaryEmployeeOriginVal);

	SalaryEmployeeOriginVal findSalaryEmployeeOriginValById(Long id);

	List<SalaryEmployeeOriginVal> listSalaryEmployeeOriginVal();

	List<SalaryEmployeeOriginVal> listSalaryEmployeeOriginValByUserId(Long userId);

	void deleteSalaryEmployeeOriginValByGroupId(Long groupId);
    void deleteSalaryEmployeeOriginValByGroupIdUserId(Long groupId, Long userId);
}