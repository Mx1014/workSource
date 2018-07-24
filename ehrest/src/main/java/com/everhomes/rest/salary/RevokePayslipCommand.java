// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>organizationId: 所属id 总公司id 必填</li>
 * <li>ownerId: 分公司id 必填</li>
 * <li>payslipId: 工资条Id撤回工资条下所有人 可选</li>
 * <li>payslipDetailId: 工资条详情id 只撤销某一个人的某条工资条详情 可选</li>
 * </ul>
 */
public class RevokePayslipCommand {

	private Long organizationId;

	private Long ownerId;

	private Long payslipId;

	private Long payslipDetailId;

	public RevokePayslipCommand() {

	}

	public RevokePayslipCommand(Long organizationId, Long ownerId, Long payslipId, Long payslipDetailId) {
		super();
		this.organizationId = organizationId;
		this.ownerId = ownerId;
		this.payslipId = payslipId;
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

	public Long getPayslipId() {
		return payslipId;
	}

	public void setPayslipId(Long payslipId) {
		this.payslipId = payslipId;
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
