// @formatter:off
package com.everhomes.salary;

import java.util.List;

public interface SalaryEmployeeOriginValProvider {

	void createSalaryEmployeeOriginVal(SalaryEmployeeOriginVal salaryEmployeeOriginVal);

	void updateSalaryEmployeeOriginVal(SalaryEmployeeOriginVal salaryEmployeeOriginVal);

	SalaryEmployeeOriginVal findSalaryEmployeeOriginValById(Long id);

	List<SalaryEmployeeOriginVal> listSalaryEmployeeOriginVal();

	List<SalaryEmployeeOriginVal> listSalaryEmployeeOriginValByUserId(Long userId, String ownerType, Long ownerId);

	void deleteSalaryEmployeeOriginValByGroupId(Long groupId);
    void deleteSalaryEmployeeOriginValByGroupIdUserId(Long groupId, Long userId);

	List<SalaryEmployeeOriginVal> listSalaryEmployeeOriginValByUserId(String ownerType, Long ownerId, Long userId);
	List<Object[]> getRelevantNumbersByGroupId(List<Long> salaryGroupIds);
}