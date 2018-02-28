// @formatter:off
package com.everhomes.salary;

import java.util.List;

public interface SalaryGroupsReportResourceProvider {

	void createSalaryGroupsReportResource(SalaryGroupsReportResource salaryGroupsReportResource);

	void updateSalaryGroupsReportResource(SalaryGroupsReportResource salaryGroupsReportResource);

	SalaryGroupsReportResource findSalaryGroupsReportResourceById(Long id);

	List<SalaryGroupsReportResource> listSalaryGroupsReportResource();

}