// @formatter:off
package com.everhomes.salary;

import java.util.List;

public interface SalaryEntityCategoryProvider {

	void createSalaryEntityCategory(SalaryEntityCategory salaryEntryCategory);

	void updateSalaryEntityCategory(SalaryEntityCategory salaryEntryCategory);

	SalaryEntityCategory findSalaryEntityCategoryById(Long id);

	List<SalaryEntityCategory> listSalaryEntityCategory();

}