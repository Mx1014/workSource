// @formatter:off
package com.everhomes.salary;

import com.everhomes.listing.CrossShardListingLocator;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

public interface SalaryPayslipDetailProvider {

	void createSalaryPayslipDetail(SalaryPayslipDetail salaryPayslipDetail);

	void updateSalaryPayslipDetail(SalaryPayslipDetail salaryPayslipDetail);

	SalaryPayslipDetail findSalaryPayslipDetailById(Long id);

	List<SalaryPayslipDetail> listSalaryPayslipDetail();

	Integer countSend(Long payslipId);

	Integer countConfirm(Long payslipId);

	Integer countView(Long payslipId);

	Integer countRevoke(Long payslipId);

	List<SalaryPayslipDetail> listSalaryPayslipDetail(Long organizationId, Long ownerId,
													  Long payslipId, String name, Byte status, CrossShardListingLocator locator, Integer pageSize);

	List<SalaryPayslipDetail> listSalaryPayslipDetailBypayslipId(Long payslipId);

	void deleteSalaryPayslipDetail(SalaryPayslipDetail spd);

	List<SalaryPayslipDetail> listUserSalaryPayslipDetail(Long userId, Long organizationId);

	void confirmSalaryPayslipDetailBeforeDate(Timestamp timestamp);
}