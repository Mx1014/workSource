package com.everhomes.rest.techpark.punch.admin;

import com.everhomes.util.StringHelper;

/**
 *
 * <ul>
 * <li>annualLeaveBalanceCorrection: 年假余额调整值 正数加,负数减,单位天</li>
 * <li>annualLeaveBalanceCorrectionDisplay: 将天数换算成“xx天xx小时”用于前端显示</li>
 * <li>overtimeCompensationBalanceCorrection: 调休余额调整值 正数加,负数减,单位天</li>
 * <li>overtimeCompensationBalanceCorrectionDisplay:  将天数换算成“xx天xx小时”用于前端显示</li>
 * <li>annualLeaveBalance: 年假余额,单位天</li>
 * <li>annualLeaveBalanceDisplay: 将天数换算成“xx天xx小时”用于前端显示</li>
 * <li>overtimeCompensationBalance: 调休余额,单位天</li>
 * <li>overtimeCompensationBalanceDisplay: 将天数换算成“xx天xx小时” 用于前端显示</li>
 * <li>description: 备注</li>
 * <li>creatorName: 操作者姓名</li>
 * <li>createTime: 时间</li>
 * </ul>
 */
public class VacationBalanceLogDTO {
	private Double annualLeaveBalanceCorrection;
	private String annualLeaveBalanceCorrectionDisplay;
	private Double overtimeCompensationBalanceCorrection;
	private String overtimeCompensationBalanceCorrectionDisplay;
	private Double annualLeaveBalance;
	private String annualLeaveBalanceDisplay;
	private Double overtimeCompensationBalance;
	private String overtimeCompensationBalanceDisplay;
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

	public String getAnnualLeaveBalanceCorrectionDisplay() {
		return annualLeaveBalanceCorrectionDisplay;
	}

	public void setAnnualLeaveBalanceCorrectionDisplay(String annualLeaveBalanceCorrectionDisplay) {
		this.annualLeaveBalanceCorrectionDisplay = annualLeaveBalanceCorrectionDisplay;
	}

	public String getOvertimeCompensationBalanceCorrectionDisplay() {
		return overtimeCompensationBalanceCorrectionDisplay;
	}

	public void setOvertimeCompensationBalanceCorrectionDisplay(String overtimeCompensationBalanceCorrectionDisplay) {
		this.overtimeCompensationBalanceCorrectionDisplay = overtimeCompensationBalanceCorrectionDisplay;
	}

	public String getAnnualLeaveBalanceDisplay() {
		return annualLeaveBalanceDisplay;
	}

	public void setAnnualLeaveBalanceDisplay(String annualLeaveBalanceDisplay) {
		this.annualLeaveBalanceDisplay = annualLeaveBalanceDisplay;
	}

	public String getOvertimeCompensationBalanceDisplay() {
		return overtimeCompensationBalanceDisplay;
	}

	public void setOvertimeCompensationBalanceDisplay(String overtimeCompensationBalanceDisplay) {
		this.overtimeCompensationBalanceDisplay = overtimeCompensationBalanceDisplay;
	}
}
