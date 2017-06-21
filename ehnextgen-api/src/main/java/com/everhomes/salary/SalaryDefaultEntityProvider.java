// @formatter:off
package com.everhomes.salary;

import java.util.List;

public interface SalaryDefaultEntityProvider {

	void createSalaryDefaultEntity(SalaryDefaultEntity salaryDefaultEntity);

	void updateSalaryDefaultEntity(SalaryDefaultEntity salaryDefaultEntity);
 
	List<SalaryDefaultEntity> listSalaryDefaultEntity();

	SalaryDefaultEntity findSalaryDefaultEntityById(Long id);

}