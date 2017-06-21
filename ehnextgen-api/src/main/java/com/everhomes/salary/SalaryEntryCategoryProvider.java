// @formatter:off
package com.everhomes.salary;

import java.util.List;

public interface SalaryEntryCategoryProvider {

	void createSalaryEntryCategory(SalaryEntityCategory salaryEntryCategory);

	void updateSalaryEntryCategory(SalaryEntityCategory salaryEntryCategory);

	SalaryEntityCategory findSalaryEntryCategoryById(Long id);

	List<SalaryEntityCategory> listSalaryEntryCategory();

}