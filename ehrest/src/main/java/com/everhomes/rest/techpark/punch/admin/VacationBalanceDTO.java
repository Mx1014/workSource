package com.everhomes.rest.techpark.punch.admin;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>contactName:姓名</li>
 * <li>detailId: detailId</li>
 * <li>departName: 部门</li>
 * <li>employeeNo: 工号</li>
 * <li>checkInTime: 入职日期</li>
 * <li>annualLeaveBalance: 年假余额</li>
 * <li>overtimeCompensationBalance: 调休余额</li>
 * </ul>
 */
public class VacationBalanceDTO {
	private String contactName;
	private String contactToken;
	private Long detailId;
	private String departName;
	private String employeeNo;
	private Long checkInTime;
	private Double annualLeaveBalance;
	private Double overtimeCompensationBalance;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public Long getDetailId() {
		return detailId;
	}
	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}
	public String getDepartName() {
		return departName;
	}
	public void setDepartName(String departName) {
		this.departName = departName;
	}
	public String getEmployeeNo() {
		return employeeNo;
	}
	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}
	public Long getCheckInTime() {
		return checkInTime;
	}
	public void setCheckInTime(Long checkInTime) {
		this.checkInTime = checkInTime;
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


	public String getContactToken() {
		return contactToken;
	}

	public void setContactToken(String contactToken) {
		this.contactToken = contactToken;
	}
}
