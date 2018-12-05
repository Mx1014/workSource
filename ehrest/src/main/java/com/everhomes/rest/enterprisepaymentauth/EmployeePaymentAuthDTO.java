package com.everhomes.rest.enterprisepaymentauth;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * <p>员工授权每月额度信息</p>
 * <ul>
 * <li>userId:用户uid</li>
 * <li>detailId: 员工档案id  </li>
 * <li>contactName: 员工姓名</li>
 * <li>departmentId: 所属部门ID</li>
 * <li>departmentName: 部门名称</li>
 * <li>currentMonth: 当前月份，格式yyyyMM</li>
 * <li>limitAmount: 每月额度</li>
 * <li>usedAmount: 本月已使用额度</li>
 * <li>currentMonthRemainAmount: 当月余额</li>
 * <li>sceneString: 支付场景名称</li>
 * </ul>
 */
public class EmployeePaymentAuthDTO {
    private Long userId;
    private Long detailId;
    private String contactName;
    private Long departmentId;
    private String departmentName;
    private String currentMonth;
    private BigDecimal limitAmount;
    private BigDecimal usedAmount;
    private BigDecimal currentMonthRemainAmount;
    private String sceneString;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getCurrentMonth() {
        return currentMonth;
    }

    public void setCurrentMonth(String currentMonth) {
        this.currentMonth = currentMonth;
    }

    public BigDecimal getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(BigDecimal limitAmount) {
        this.limitAmount = limitAmount;
    }

    public BigDecimal getUsedAmount() {
        return usedAmount;
    }

    public void setUsedAmount(BigDecimal usedAmount) {
        this.usedAmount = usedAmount;
    }

    public BigDecimal getCurrentMonthRemainAmount() {
        return currentMonthRemainAmount;
    }

    public void setCurrentMonthRemainAmount(BigDecimal currentMonthRemainAmount) {
        this.currentMonthRemainAmount = currentMonthRemainAmount;
    }

    public String getSceneString() {
        return sceneString;
    }

    public void setSceneString(String sceneString) {
        this.sceneString = sceneString;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


}
