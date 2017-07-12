package com.everhomes.rest.energy;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>communityId: 项目id</li>
 *     <li>name: 表记名称</li>
 *     <li>meterNumber: 表记号码</li>
 *     <li>meterType: 表记类型</li>
 *     <li>billCategory: 项目</li>
 *     <li>serviceCategory: 分类</li>
 *     <li>maxReading: 最大量程</li>
 *     <li>startReading: 起始读数</li>
 *     <li>rate: 倍率</li>
 *     <li>price: 单价</li>
 *     <li>status: 状态 {@link com.everhomes.rest.energy.EnergyMeterStatus}</li>
 *     <li>costFormula: 费用公式 {@link com.everhomes.rest.energy.EnergyMeterFormulaDTO}</li>
 *     <li>amountFormula: 用量公式 {@link com.everhomes.rest.energy.EnergyMeterFormulaDTO}</li>
 *     <li>lastReadTime: 最后一次读表时间</li>
 *     <li>lastReading: 最后一次读数值</li>
 *     <li>todayReadStatus: 今日读表状态{@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 *     <li>dayPrompt: 每日读表提示</li>
 *     <li>monthPrompt: 每月读表提示</li>
 *     <li>calculationType: 价格计算方式 参考{@link com.everhomes.rest.energy.PriceCalculationType}</li>
 *     <li>priceConfig: 梯度价格方案 参考{@link com.everhomes.rest.energy.EnergyMeterPriceConfigDTO}</li>
 * </ul>
 */
public class EnergyMeterDTO {

    private Long id;
    private Long communityId;
    private String name;
    private String meterNumber;
    private String meterType;
    private String billCategory;
    private String serviceCategory;
    private BigDecimal maxReading;
    private BigDecimal startReading;
    private BigDecimal rate;
    private BigDecimal price;
    private EnergyMeterFormulaDTO costFormula;
    private EnergyMeterFormulaDTO amountFormula;
    private String status;
    private Timestamp lastReadTime;
    private BigDecimal lastReading;

    private Byte todayReadStatus;
    private BigDecimal dayPrompt;
    private BigDecimal monthPrompt;

    private Byte calculationType;
    private EnergyMeterPriceConfigDTO priceConfig;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeterType() {
        return meterType;
    }

    public void setMeterType(String meterType) {
        this.meterType = meterType;
    }

    public String getMeterNumber() {
        return meterNumber;
    }

    public Byte getTodayReadStatus() {
        return todayReadStatus;
    }

    public void setTodayReadStatus(Byte todayReadStatus) {
        this.todayReadStatus = todayReadStatus;
    }

    public BigDecimal getDayPrompt() {
        return dayPrompt;
    }

    public void setDayPrompt(BigDecimal dayPrompt) {
        this.dayPrompt = dayPrompt;
    }

    public BigDecimal getMonthPrompt() {
        return monthPrompt;
    }

    public void setMonthPrompt(BigDecimal monthPrompt) {
        this.monthPrompt = monthPrompt;
    }

    public void setMeterNumber(String meterNumber) {
        this.meterNumber = meterNumber;
    }

    public String getBillCategory() {
        return billCategory;
    }

    public void setBillCategory(String billCategory) {
        this.billCategory = billCategory;
    }

    public String getServiceCategory() {
        return serviceCategory;
    }

    public void setServiceCategory(String serviceCategory) {
        this.serviceCategory = serviceCategory;
    }

    public BigDecimal getMaxReading() {
        return maxReading;
    }

    public void setMaxReading(BigDecimal maxReading) {
        this.maxReading = maxReading;
    }

    public BigDecimal getStartReading() {
        return startReading;
    }

    public void setStartReading(BigDecimal startReading) {
        this.startReading = startReading;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public EnergyMeterFormulaDTO getCostFormula() {
        return costFormula;
    }

    public void setCostFormula(EnergyMeterFormulaDTO costFormula) {
        this.costFormula = costFormula;
    }

    public EnergyMeterFormulaDTO getAmountFormula() {
        return amountFormula;
    }

    public void setAmountFormula(EnergyMeterFormulaDTO amountFormula) {
        this.amountFormula = amountFormula;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getLastReadTime() {
        return lastReadTime;
    }

    public void setLastReadTime(Timestamp lastReadTime) {
        this.lastReadTime = lastReadTime;
    }

    public BigDecimal getLastReading() {
        return lastReading;
    }

    public void setLastReading(BigDecimal lastReading) {
        this.lastReading = lastReading;
    }

    public Byte getCalculationType() {
        return calculationType;
    }

    public void setCalculationType(Byte calculationType) {
        this.calculationType = calculationType;
    }

    public EnergyMeterPriceConfigDTO getPriceConfig() {
        return priceConfig;
    }

    public void setPriceConfig(EnergyMeterPriceConfigDTO priceConfig) {
        this.priceConfig = priceConfig;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
