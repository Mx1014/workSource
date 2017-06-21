// @formatter:off
package com.everhomes.salary;

import java.util.List;

public interface SalaryDefaultEntityProvider {

	void createSalaryDefaultEntity(SalaryDefaultEntity salaryDefaultEntity);

	void updateSalaryDefaultEntity(SalaryDefaultEntity salaryDefaultEntity);

	SalaryDefaultEntity findSalaryDefaultEntryById(Long id);

	List<SalaryDefaultEntity> listSalaryDefaultEntry();

}