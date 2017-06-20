// @formatter:off
package com.everhomes.salary;

import java.util.List;

public interface SalaryGroupEntryProvider {

	void createSalaryGroupEntry(SalaryGroupEntry salaryGroupEntry);

	void updateSalaryGroupEntry(SalaryGroupEntry salaryGroupEntry);

	SalaryGroupEntry findSalaryGroupEntryById(Long id);

	List<SalaryGroupEntry> listSalaryGroupEntry();

}