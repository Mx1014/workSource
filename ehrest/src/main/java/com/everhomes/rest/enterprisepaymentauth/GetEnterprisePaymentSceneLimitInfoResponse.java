// @formatter:off
package com.everhomes.rest.enterprisepaymentauth;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * <ul>
 * <li>sceneAppId: 场景id</li>
 * <li>sceneAppName: 场景名称</li>
 * <li>sceneAppDescription: 场景描述 可为空</li>
 * <li>currentMonthRemainAmount: 本月余额 </li>
 * <li>monthLimitAmount: 每月总额 </li>
 * <li>usedAmount: 已使用额度</li>
 * <li>historicalTotalPayAmount: 历史累计支付金额</li>
 * <li>historicalPayCount: 历史累计支付笔数</li>
 * <li>totalEmployeeAuthCount: 已授权用户数</li>
 * </ul>
 */
public class GetEnterprisePaymentSceneLimitInfoResponse {
    private Long sceneAppId;
    private String sceneAppName;
    private String sceneAppDescription;
    private BigDecimal currentMonthRemainAmount;
    private BigDecimal limitAmount;
    private BigDecimal usedAmount;
    private BigDecimal historicalTotalPayAmount;
    private Integer historicalPayCount;
    private Integer totalEmployeeAuthCount;

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

    public String getSceneAppDescription() {
        return sceneAppDescription;
    }

    public void setSceneAppDescription(String sceneAppDescription) {
        this.sceneAppDescription = sceneAppDescription;
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

    public BigDecimal getHistoricalTotalPayAmount() {
        return historicalTotalPayAmount;
    }

    public void setHistoricalTotalPayAmount(BigDecimal historicalTotalPayAmount) {
        this.historicalTotalPayAmount = historicalTotalPayAmount;
    }

    public Integer getHistoricalPayCount() {
        return historicalPayCount;
    }

    public void setHistoricalPayCount(Integer historicalPayCount) {
        this.historicalPayCount = historicalPayCount;
    }

    public Integer getTotalEmployeeAuthCount() {
        return totalEmployeeAuthCount;
    }

    public void setTotalEmployeeAuthCount(Integer totalEmployeeAuthCount) {
        this.totalEmployeeAuthCount = totalEmployeeAuthCount;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
