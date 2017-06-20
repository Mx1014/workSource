// @formatter:off
package com.everhomes.salary;

import java.util.List;

public interface SalaryEntryCategoryProvider {

	void createSalaryEntryCategory(SalaryEntryCategory salaryEntryCategory);

	void updateSalaryEntryCategory(SalaryEntryCategory salaryEntryCategory);

	SalaryEntryCategory findSalaryEntryCategoryById(Long id);

	List<SalaryEntryCategory> listSalaryEntryCategory();

}