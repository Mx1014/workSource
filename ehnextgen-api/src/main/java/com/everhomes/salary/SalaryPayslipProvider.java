// @formatter:off
package com.everhomes.salary;

import com.everhomes.listing.CrossShardListingLocator;

import java.util.List;

public interface SalaryPayslipProvider {

	void createSalaryPayslip(SalaryPayslip salaryPayslip);

	void updateSalaryPayslip(SalaryPayslip salaryPayslip);

	SalaryPayslip findSalaryPayslipById(Long id);

	List<SalaryPayslip> listSalaryPayslip();

	List<SalaryPayslip> listSalaryPayslip(Long ownerId, Long organizationId, String payslipYear, String Name, CrossShardListingLocator locator, Integer pageSize);

	void deleteSalaryPayslip(Long payslipId);

}