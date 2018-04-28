// @formatter:off
package com.everhomes.rest.techpark.punch.admin;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>organizationId: 所属id 总公司id</li>
 * <li>detailId: 员工detailid</li>
 * <li>annualLeaveBalanceCorrection: 年假余额调整值 正数加,负数减</li>
 * <li>overtimeCompensationBalanceCorrection: 调休余额调整值 正数加,负数减</li>
 * <li>description: 备注</li>
 * </ul>
 */
public class UpdateVacationBalancesCommand {

	private Long organizationId;

	private Long detailId;

	private Double annualLeaveBalanceCorrection;

	private Double overtimeCompensationBalanceCorrection;

	private String description;
	private Byte isBatch;

	public UpdateVacationBalancesCommand() {

	}

	public UpdateVacationBalancesCommand(Long organizationId, Long detailId, Double annualLeaveBalanceCorrection, Double overtimeCompensationBalanceCorrection, String description) {
		super();
		this.organizationId = organizationId;
		this.detailId = detailId;
		this.annualLeaveBalanceCorrection = annualLeaveBalanceCorrection;
		this.overtimeCompensationBalanceCorrection = overtimeCompensationBalanceCorrection;
		this.description = description;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Long getDetailId() {
		return detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	public Double getAnnualLeaveBalanceCorrection() {
		return annualLeaveBalanceCorrection;
	}

	public void setAnnualLeaveBalanceCorrection(Double annualLeaveBalanceCorrection) {
		this.annualLeaveBalanceCorrection = annualLeaveBalanceCorrection;
	}

	public Double getOvertimeCompensationBalanceCorrection() {
		return overtimeCompensationBalanceCorrection;
	}

	public void setOvertimeCompensationBalanceCorrection(Double overtimeCompensationBalanceCorrection) {
		this.overtimeCompensationBalanceCorrection = overtimeCompensationBalanceCorrection;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Byte getIsBatch() {
		return isBatch;
	}

	public void setIsBatch(Byte isBatch) {
		this.isBatch = isBatch;
	}
}
