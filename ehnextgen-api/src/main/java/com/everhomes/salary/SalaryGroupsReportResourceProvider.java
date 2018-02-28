// @formatter:off
package com.everhomes.salary;

import java.util.List;

public interface SalaryGroupsReportResourceProvider {

	void createSalaryGroupsReportResource(SalaryGroupsReportResource salaryGroupsReportResource);

	void updateSalaryGroupsReportResource(SalaryGroupsReportResource salaryGroupsReportResource);

	SalaryGroupsReportResource findSalaryGroupsReportResourceById(Long id);

	List<SalaryGroupsReportResource> listSalaryGroupsReportResource();

	void deleteSalaryGroupsReportResourceByPeriodAndType(Long ownerId, String salaryPeriod, Byte reportType);

	SalaryGroupsReportResource findSalaryGroupsReportResourceByPeriodAndType(Long ownerId, String month, Byte code);
}