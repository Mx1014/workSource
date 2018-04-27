// @formatter:off
package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>organizationId: 所属id 总公司id 必填</li>
 * <li>ownerId: 分公司id 必填</li>
 * <li>payslipDetailId: 工资条详情id 删除某一个人的某条工资条详情 选填</li>
 * <li>payslipId: 工资条id 删除某一个工资条 选填</li>
 * </ul>
 */
public class DeletePayslipCommand {

	private Long organizationId;

	private Long ownerId;

	private Long payslipDetailId;
	
	private Long payslipId;

	public DeletePayslipCommand() {

	}

	public DeletePayslipCommand(Long organizationId, Long ownerId, Long payslipDetailId) {
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

	public Long getPayslipId() {
		return payslipId;
	}

	public void setPayslipId(Long payslipId) {
		this.payslipId = payslipId;
	}

}
