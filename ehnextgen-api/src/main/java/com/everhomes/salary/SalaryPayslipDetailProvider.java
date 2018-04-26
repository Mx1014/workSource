// @formatter:off
package com.everhomes.salary;

import java.util.List;

public interface SalaryPayslipDetailProvider {

	void createSalaryPayslipDetail(SalaryPayslipDetail salaryPayslipDetail);

	void updateSalaryPayslipDetail(SalaryPayslipDetail salaryPayslipDetail);

	SalaryPayslipDetail findSalaryPayslipDetailById(Long id);

	List<SalaryPayslipDetail> listSalaryPayslipDetail();

	Integer countSend(Long payslipId);

}