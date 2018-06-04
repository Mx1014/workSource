// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>organizationId: 所属id 总公司id 必填</li>
 * <li>ownerId: 分公司id 必填</li>
 * <li>payslipYear: 年度 必填</li>
 * </ul>
 */
public class ListYearPayslipSummaryCommand {

	private Long organizationId;

	private Long ownerId;

	private String payslipYear;

	public ListYearPayslipSummaryCommand() {

	}

	public ListYearPayslipSummaryCommand(Long organizationId, Long ownerId, String payslipYear) {
		super();
		this.organizationId = organizationId;
		this.ownerId = ownerId;
		this.payslipYear = payslipYear;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getPayslipYear() {
		return payslipYear;
	}

	public void setPayslipYear(String payslipYear) {
		this.payslipYear = payslipYear;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
