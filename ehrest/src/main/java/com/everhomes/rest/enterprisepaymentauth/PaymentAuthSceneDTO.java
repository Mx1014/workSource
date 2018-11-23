package com.everhomes.rest.enterprisepaymentauth;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * <ul>
 * <li>sceneAppId: 场景id  </li>
 * <li>sceneAppName: 场景名称</li>
 * <li>authEmployeeCount: 授权人数</li>
 * <li>currentMonthRemainAmount: 当月余额</li>
 * <li>limitAmount: 每月额度</li>
 * <li>usedAmount: 已使用余额</li>
 * </ul>
 */
public class PaymentAuthSceneDTO {
    private Long sceneAppId;
    private String sceneAppName;
    private Integer authEmployeeCount;
    private BigDecimal currentMonthRemainAmount;
    private BigDecimal limitAmount;
    private BigDecimal usedAmount;

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

    public Integer getAuthEmployeeCount() {
        return authEmployeeCount;
    }

    public void setAuthEmployeeCount(Integer authEmployeeCount) {
        this.authEmployeeCount = authEmployeeCount;
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


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public BigDecimal getUsedAmount() {
        return usedAmount;
    }

    public void setUsedAmount(BigDecimal usedAmount) {
        this.usedAmount = usedAmount;
    }
}
