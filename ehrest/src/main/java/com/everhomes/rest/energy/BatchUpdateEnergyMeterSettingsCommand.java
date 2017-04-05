package com.everhomes.rest.energy;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * <ul>
 *     <li>organizationId: 组织id</li>
 *     <li>meterIds: 表记id列表</li>
 *     <li>price: 价格参数</li>
 *     <li>rate: 倍率参数</li>
 *     <li>amountFormulaId: 用量计算公式 </li>
 *     <li>costFormulaId: 费用计算公式 </li>
 *     <li>rateStart: 倍率开始时间 </li>
 *     <li>rateEnd: 倍率结束时间 </li>
 *     <li>priceStart: 价格开始时间 </li>
 *     <li>priceEnd: 介个结束时间 </li>
 *     <li>costFormulaStart: 费用计算公式开始时间 </li>
 *     <li>costFormulaEnd: 费用计算公式结束时间 </li>
 *     <li>amountFormulaStart: 用量计算公式开始时间</li>
 *     <li>amountFormulaEnd: 用量计算公式结束时间</li>
 *     <li>calculationType: 价格计算方式 参考{@link com.everhomes.rest.energy.PriceCalculationType} </li>
 *     <li>configId: 价格方案id </li>
 * </ul>
 */
public class BatchUpdateEnergyMeterSettingsCommand {

    @NotNull
    private Long organizationId;
    @ItemType(Long.class)
    private List<Long> meterIds;

    private BigDecimal price;
    private BigDecimal rate;
    private Long costFormulaId;
    private Long amountFormulaId;

    private Long priceStart;
    private Long priceEnd;

    private Long rateStart;
    private Long rateEnd;

    private Long costFormulaStart;
    private Long costFormulaEnd;

    private Long amountFormulaStart;
    private Long amountFormulaEnd;

    private Byte calculationType;
    private Long configId;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public List<Long> getMeterIds() {
        return meterIds;
    }

    public void setMeterIds(List<Long> meterIds) {
        this.meterIds = meterIds;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Long getCostFormulaId() {
        return costFormulaId;
    }

    public void setCostFormulaId(Long costFormulaId) {
        this.costFormulaId = costFormulaId;
    }

    public Long getAmountFormulaId() {
        return amountFormulaId;
    }

    public void setAmountFormulaId(Long amountFormulaId) {
        this.amountFormulaId = amountFormulaId;
    }

    public Long getPriceStart() {
        return priceStart;
    }

    public void setPriceStart(Long priceStart) {
        this.priceStart = priceStart;
    }

    public Long getPriceEnd() {
        return priceEnd;
    }

    public void setPriceEnd(Long priceEnd) {
        this.priceEnd = priceEnd;
    }

    public Long getRateStart() {
        return rateStart;
    }

    public void setRateStart(Long rateStart) {
        this.rateStart = rateStart;
    }

    public Long getRateEnd() {
        return rateEnd;
    }

    public void setRateEnd(Long rateEnd) {
        this.rateEnd = rateEnd;
    }

    public Long getCostFormulaStart() {
        return costFormulaStart;
    }

    public void setCostFormulaStart(Long costFormulaStart) {
        this.costFormulaStart = costFormulaStart;
    }

    public Long getCostFormulaEnd() {
        return costFormulaEnd;
    }

    public void setCostFormulaEnd(Long costFormulaEnd) {
        this.costFormulaEnd = costFormulaEnd;
    }

    public Long getAmountFormulaStart() {
        return amountFormulaStart;
    }

    public void setAmountFormulaStart(Long amountFormulaStart) {
        this.amountFormulaStart = amountFormulaStart;
    }

    public Long getAmountFormulaEnd() {
        return amountFormulaEnd;
    }

    public void setAmountFormulaEnd(Long amountFormulaEnd) {
        this.amountFormulaEnd = amountFormulaEnd;
    }

    public Byte getCalculationType() {
        return calculationType;
    }

    public void setCalculationType(Byte calculationType) {
        this.calculationType = calculationType;
    }

    public Long getConfigId() {
        return configId;
    }

    public void setConfigId(Long configId) {
        this.configId = configId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
