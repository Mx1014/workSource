package com.everhomes.rest.techpark.punch.admin;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>annualLeaveBalanceCorrection: 年假余额调整值 正数加,负数减</li>
 * <li>overtimeCompensationBalanceCorrection: 调休余额调整值 正数加,负数减</li>
 * <li>annualLeaveBalance: 年假余额</li>
 * <li>overtimeCompensationBalance: 调休余额</li>
 * <li>description: 备注</li>
 * <li>creatorName: 操作者姓名</li>
 * <li>createTime: 时间</li>
 * </ul>
 */
public class VacationBalanceLogDTO {
	private Double annualLeaveBalanceCorrection;
	private Double overtimeCompensationBalanceCorrection;
	private Double annualLeaveBalance;
	private Double overtimeCompensationBalance;
	private String description;
	private String creatorName;
	private Long createTime;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
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
	public Double getAnnualLeaveBalance() {
		return annualLeaveBalance;
	}
	public void setAnnualLeaveBalance(Double annualLeaveBalance) {
		this.annualLeaveBalance = annualLeaveBalance;
	}
	public Double getOvertimeCompensationBalance() {
		return overtimeCompensationBalance;
	}
	public void setOvertimeCompensationBalance(Double overtimeCompensationBalance) {
		this.overtimeCompensationBalance = overtimeCompensationBalance;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	
}
