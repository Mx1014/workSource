// @formatter:off
package com.everhomes.salary;

import java.util.List;

public interface SalaryGroupsFileProvider {

	void createSalaryGroupsFile(SalaryGroupsFile salaryGroupsFile);

	void updateSalaryGroupsFile(SalaryGroupsFile salaryGroupsFile);

	SalaryGroupsFile findSalaryGroupsFileById(Long id);

	List<SalaryGroupsFile> listSalaryGroupsFile();

	SalaryGroupsFile findSalaryGroupsFile(Long ownerId, String month);

	void deleteGroupsFile(Long ownerId, String month);
}