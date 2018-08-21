package com.everhomes.rest.techpark.punch.admin;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * <ul>
 * <li>contactName:姓名</li>
 * <li>detailId: detailId</li>
 * <li>departName: 部门</li>
 * <li>employeeNo: 工号</li>
 * <li>checkInTime: 入职日期</li>
 * <li>annualLeaveBalance: 年假余额，单位天</li>
 * <li>annualLeaveBalanceDisplay: 年假余额显示，格式为xx天xx小时</li>
 * <li>annualLeaveHistoryCount: 已请年假总计，单位天</li>
 * <li>annualLeaveHistoryCountDisplay: 已请年假总计显示，格式为xx天xx小时</li>
 * <li>overtimeCompensationBalance: 调休余额，单位天</li>
 * <li>overtimeCompensationBalanceDisplay:  调休余额显示，格式为xx天xx小时</li>
 * <li>overtimeCompensationHistoryCount: 已请调休总计，单位天</li>
 * <li>overtimeCompensationHistoryCountDisplay:  已请调休总计显示，格式为xx天xx小时</li>
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
    private String annualLeaveBalanceDisplay;
    private BigDecimal annualLeaveHistoryCount;
    private String annualLeaveHistoryCountDisplay;
    private Double overtimeCompensationBalance;
    private String overtimeCompensationBalanceDisplay;
    private BigDecimal overtimeCompensationHistoryCount;
    private String overtimeCompensationHistoryCountDisplay;

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

    public BigDecimal getAnnualLeaveHistoryCount() {
        return annualLeaveHistoryCount;
    }

    public void setAnnualLeaveHistoryCount(BigDecimal annualLeaveHistoryCount) {
        this.annualLeaveHistoryCount = annualLeaveHistoryCount;
    }

    public BigDecimal getOvertimeCompensationHistoryCount() {
        return overtimeCompensationHistoryCount;
    }

    public void setOvertimeCompensationHistoryCount(BigDecimal overtimeCompensationHistoryCount) {
        this.overtimeCompensationHistoryCount = overtimeCompensationHistoryCount;
    }

    public String getAnnualLeaveBalanceDisplay() {
        return annualLeaveBalanceDisplay;
    }

    public void setAnnualLeaveBalanceDisplay(String annualLeaveBalanceDisplay) {
        this.annualLeaveBalanceDisplay = annualLeaveBalanceDisplay;
    }

    public String getAnnualLeaveHistoryCountDisplay() {
        return annualLeaveHistoryCountDisplay;
    }

    public void setAnnualLeaveHistoryCountDisplay(String annualLeaveHistoryCountDisplay) {
        this.annualLeaveHistoryCountDisplay = annualLeaveHistoryCountDisplay;
    }

    public String getOvertimeCompensationBalanceDisplay() {
        return overtimeCompensationBalanceDisplay;
    }

    public void setOvertimeCompensationBalanceDisplay(String overtimeCompensationBalanceDisplay) {
        this.overtimeCompensationBalanceDisplay = overtimeCompensationBalanceDisplay;
    }

    public String getOvertimeCompensationHistoryCountDisplay() {
        return overtimeCompensationHistoryCountDisplay;
    }

    public void setOvertimeCompensationHistoryCountDisplay(String overtimeCompensationHistoryCountDisplay) {
        this.overtimeCompensationHistoryCountDisplay = overtimeCompensationHistoryCountDisplay;
    }
}
