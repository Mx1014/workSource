package com.everhomes.rest.energy;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * <ul>
 *     <li>communityName: 小区名字</li>
 *     <li>dateStr: 日期</li>
 *     <li>waterReceivableAmount: 水表应收数量</li>
 *     <li>waterReceivableCost: 水表应收费用 </li>
 *     <li>waterBurdenAmount: 水表负担数量 </li>
 *     <li>waterBurdenCost: 水表负担费用</li>
 *     <li>elecReceivableAmount: 电表应收数量</li>
 *     <li>elecReceivableCost: 电表应收费用</li>
 *     <li>elecBurdenAmount: 电表负担数量</li>
 *     <li>elecBurdenCost: 电表负担费用</li>
 *     <li>areaSize: 面积</li>
 *     <li>waterAverageAmount: 每平用水量</li>
 *     <li>elecAverageAmount: 每平用电量</li>
 * </ul>
 */
public class EnergyStatByYearDTO {

    private String communityName;
    private String dateStr;

    private BigDecimal waterReceivableAmount;
    private BigDecimal waterReceivableCost;
    private BigDecimal waterBurdenAmount;
    private BigDecimal waterBurdenCost;

    private BigDecimal elecReceivableAmount;
    private BigDecimal elecReceivableCost;
    private BigDecimal elecBurdenAmount;
    private BigDecimal elecBurdenCost;

    private BigDecimal areaSize;
    private BigDecimal waterAverageAmount;
    private BigDecimal elecAverageAmount;

    public BigDecimal getWaterReceivableAmount() {
        return waterReceivableAmount;
    }

    public void setWaterReceivableAmount(BigDecimal waterReceivableAmount) {
        this.waterReceivableAmount = waterReceivableAmount;
    }

    public BigDecimal getWaterReceivableCost() {
        return waterReceivableCost;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public void setWaterReceivableCost(BigDecimal waterReceivableCost) {
        this.waterReceivableCost = waterReceivableCost;
    }

    public BigDecimal getWaterBurdenAmount() {
        return waterBurdenAmount;
    }

    public void setWaterBurdenAmount(BigDecimal waterBurdenAmount) {
        this.waterBurdenAmount = waterBurdenAmount;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public BigDecimal getWaterBurdenCost() {
        return waterBurdenCost;
    }

    public void setWaterBurdenCost(BigDecimal waterBurdenCost) {
        this.waterBurdenCost = waterBurdenCost;
    }

    public BigDecimal getElecReceivableAmount() {
        return elecReceivableAmount;
    }

    public void setElecReceivableAmount(BigDecimal elecReceivableAmount) {
        this.elecReceivableAmount = elecReceivableAmount;
    }

    public BigDecimal getElecReceivableCost() {
        return elecReceivableCost;
    }

    public void setElecReceivableCost(BigDecimal elecReceivableCost) {
        this.elecReceivableCost = elecReceivableCost;
    }

    public BigDecimal getElecBurdenAmount() {
        return elecBurdenAmount;
    }

    public void setElecBurdenAmount(BigDecimal elecBurdenAmount) {
        this.elecBurdenAmount = elecBurdenAmount;
    }

    public BigDecimal getElecBurdenCost() {
        return elecBurdenCost;
    }

    public void setElecBurdenCost(BigDecimal elecBurdenCost) {
        this.elecBurdenCost = elecBurdenCost;
    }

    public BigDecimal getWaterAverageAmount() {
        return waterAverageAmount;
    }

    public void setWaterAverageAmount(BigDecimal waterAverageAmount) {
        this.waterAverageAmount = waterAverageAmount;
    }

    public BigDecimal getElecAverageAmount() {
        return elecAverageAmount;
    }

    public void setElecAverageAmount(BigDecimal elecAverageAmount) {
        this.elecAverageAmount = elecAverageAmount;
    }

    public BigDecimal getAreaSize() {
        return areaSize;
    }

    public void setAreaSize(BigDecimal areaSize) {
        this.areaSize = areaSize;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
