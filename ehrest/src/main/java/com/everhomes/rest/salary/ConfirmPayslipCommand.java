// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>organizationId: 所属id 总公司id 必填</li>
 * <li>ownerId: 分公司id 可选</li>
 * <li>payslipDetailId: 某条工资条的id 必填</li>
 * </ul>
 */
public class ConfirmPayslipCommand {

	private Long organizationId;

	private Long ownerId;

	private Long payslipDetailId;

	public ConfirmPayslipCommand() {

	}

	public ConfirmPayslipCommand(Long organizationId, Long ownerId, Long payslipDetailId) {
		super();
		this.organizationId = organizationId;
		this.ownerId = ownerId;
		this.payslipDetailId = payslipDetailId;
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

	public Long getPayslipDetailId() {
		return payslipDetailId;
	}

	public void setPayslipDetailId(Long payslipDetailId) {
		this.payslipDetailId = payslipDetailId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
