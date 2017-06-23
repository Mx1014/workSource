// @formatter:off
package com.everhomes.salary;

import java.util.List;

public interface SalaryGroupProvider {

	void createSalaryGroup(SalaryGroup salaryGroup);

	void updateSalaryGroup(SalaryGroup salaryGroup);

	SalaryGroup findSalaryGroupById(Long id);

	List<SalaryGroup> listSalaryGroup();

	List<SalaryGroup> listSalaryGroup(String ownerType, Long ownerId, String period,
			List<Byte> status);

}