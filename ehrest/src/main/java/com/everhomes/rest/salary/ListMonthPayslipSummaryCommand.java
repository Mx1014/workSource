// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>organizationId: 所属id 总公司id</li>
 * <li>ownerId: 分公司id</li>
 * <li>salaryPeriod: 期数:类似201808</li>
 * </ul>
 */
public class ListMonthPayslipSummaryCommand {

	private Long organizationId;

	private Long ownerId;

	private String salaryPeriod;

	public ListMonthPayslipSummaryCommand() {

	}

	public ListMonthPayslipSummaryCommand(Long organizationId, Long ownerId, String salaryPeriod) {
		super();
		this.organizationId = organizationId;
		this.ownerId = ownerId;
		this.salaryPeriod = salaryPeriod;
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

	public String getSalaryPeriod() {
		return salaryPeriod;
	}

	public void setSalaryPeriod(String salaryPeriod) {
		this.salaryPeriod = salaryPeriod;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
