// @formatter:off
package com.everhomes.salary;

import java.util.List;

public interface SalaryEmployeeOriginValProvider {

	void createSalaryEmployeeOriginVal(SalaryEmployeeOriginVal salaryEmployeeOriginVal);

	void updateSalaryEmployeeOriginVal(SalaryEmployeeOriginVal salaryEmployeeOriginVal);

	SalaryEmployeeOriginVal findSalaryEmployeeOriginValById(Long id);

	List<SalaryEmployeeOriginVal> listSalaryEmployeeOriginVal();

	List<SalaryEmployeeOriginVal> listSalaryEmployeeOriginValByDetailId(Long userId, String ownerType, Long ownerId);

	void deleteSalaryEmployeeOriginValByGroupId(Long groupId);
    void deleteSalaryEmployeeOriginValByGroupIdDetailId(Long groupId, Long userId, String ownerType, Long ownerId);

	List<SalaryEmployeeOriginVal> listSalaryEmployeeOriginValByDetailId(String ownerType, Long ownerId, Long detailId);
	List<Object[]> getRelevantNumbersByGroupId(List<Long> salaryGroupIds);

	void deleteSalaryEmployeeValsByGroupIdNotInOriginIds(Long salaryGroupId, List<Long> entityIds);
	List<Object[]> listSalaryEmployeeWagesDetails();
}