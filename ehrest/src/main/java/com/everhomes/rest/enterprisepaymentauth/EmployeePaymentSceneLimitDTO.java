package com.everhomes.rest.enterprisepaymentauth;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * <ul>
 * <li>userId: 用户id，可能为null或者0(未激活用户)</li>
 * <li>detailId: detailId  </li>
 * <li>sceneAppId: 支付应用场景id</li>
 * <li>sceneAppName: 支付应用场景名称</li>
 * <li>contactName: 姓名</li>
 * <li>departmentId: 部门id</li>
 * <li>departmentName: 部门</li>
 * <li>currentMonthRemainAmount: 当月余额</li>
 * <li>limitAmount: 月额度</li>
 * <li>usedAmount: 已使用额度</li>
 * </ul>
 */
public class EmployeePaymentSceneLimitDTO {
    private Long userId;
    private Long detailId;
    private Long sceneAppId;
    private String sceneAppName;
    private String contactName;
    private Long departmentId;
    private String departmentName;
    private BigDecimal currentMonthRemainAmount;
    private BigDecimal limitAmount;
    private BigDecimal usedAmount;

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

    public Long getSceneAppId() {
        return sceneAppId;
    }

    public void setSceneAppId(Long sceneAppId) {
        this.sceneAppId = sceneAppId;
    }

    public String getSceneAppName() {
        return sceneAppName;
    }

    public void setSceneAppName(String sceneAppName) {
        this.sceneAppName = sceneAppName;
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

    public BigDecimal getCurrentMonthRemainAmount() {
        return currentMonthRemainAmount;
    }

    public void setCurrentMonthRemainAmount(BigDecimal currentMonthRemainAmount) {
        this.currentMonthRemainAmount = currentMonthRemainAmount;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
