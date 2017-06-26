// @formatter:off
package com.everhomes.salary;

import java.util.List;

public interface SalaryGroupEntityProvider {

	void createSalaryGroupEntity(SalaryGroupEntity salaryGroupEntity);

	void updateSalaryGroupEntity(SalaryGroupEntity salaryGroupEntity);

	SalaryGroupEntity findSalaryGroupEntityById(Long id);

	List<SalaryGroupEntity> listSalaryGroupEntityByGroupId(Long salaryGroupId);

	List<SalaryGroupEntity> listSalaryGroupEntity();
}