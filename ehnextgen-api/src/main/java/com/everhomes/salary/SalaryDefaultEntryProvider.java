// @formatter:off
package com.everhomes.salary;

import java.util.List;

public interface SalaryDefaultEntryProvider {

	void createSalaryDefaultEntry(SalaryDefaultEntry salaryDefaultEntry);

	void updateSalaryDefaultEntry(SalaryDefaultEntry salaryDefaultEntry);

	SalaryDefaultEntry findSalaryDefaultEntryById(Long id);

	List<SalaryDefaultEntry> listSalaryDefaultEntry();

}