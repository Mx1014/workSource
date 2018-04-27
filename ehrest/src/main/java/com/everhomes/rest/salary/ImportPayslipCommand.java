// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>organizationId: 所属id 总公司id 必填</li>
 * <li>ownerId: 分公司id 必填</li>
 * <li>salaryPeriod: 期数:类似201808 必填</li>
 * <li>payslipName: 工资表名称(excel名) 必填</li>
 * </ul>
 */
public class ImportPayslipCommand {

	private Long organizationId;

	private Long ownerId;

	private String salaryPeriod;

	private String payslipName;
	
	public ImportPayslipCommand() {

	}

	public ImportPayslipCommand(Long organizationId, Long ownerId, String salaryPeriod) {
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

	public String getPayslipName() {
		return payslipName;
	}

	public void setPayslipName(String payslipName) {
		this.payslipName = payslipName;
	}

}
