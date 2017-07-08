// @formatter:off
package com.everhomes.salary;

import java.sql.Timestamp;
import java.util.List;

public interface SalaryGroupProvider {

	void createSalaryGroup(SalaryGroup salaryGroup);

	void updateSalaryGroup(SalaryGroup salaryGroup);

	SalaryGroup findSalaryGroupById(Long id);

	List<SalaryGroup> listSalaryGroup();

	List<SalaryGroup> listSalaryGroup(String ownerType, Long ownerId, String period,
			List<Byte> status);

	List<SalaryGroup> listSalaryGroup(Byte code, Timestamp date);

	void updateSalaryGroupEmailContent(String ownerType, Long ownerId, String emailContent);

	void deleteSalaryGroup(Long organizationGroupId, String salaryPeriod);

	SalaryGroup findSalaryGroupByOrgId(Long id, String lastPeriod);


	List<SalaryGroup>  listUnsendSalaryGroup(Long salaryGroupId);

	void deleteSalaryGroup(SalaryGroup salaryGroup);
}