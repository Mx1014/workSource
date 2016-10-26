package com.everhomes.rest.energy;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
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

    private Timestamp priceStart;
    private Timestamp priceEnd;

    private Timestamp rateStart;
    private Timestamp rateEnd;

    private Timestamp costFormulaStart;
    private Timestamp costFormulaEnd;

    private Timestamp amountFormulaStart;
    private Timestamp amountFormulaEnd;

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

    public Timestamp getPriceStart() {
        return priceStart;
    }

    public void setPriceStart(Timestamp priceStart) {
        this.priceStart = priceStart;
    }

    public Timestamp getPriceEnd() {
        return priceEnd;
    }

    public void setPriceEnd(Timestamp priceEnd) {
        this.priceEnd = priceEnd;
    }

    public Timestamp getRateStart() {
        return rateStart;
    }

    public void setRateStart(Timestamp rateStart) {
        this.rateStart = rateStart;
    }

    public Timestamp getRateEnd() {
        return rateEnd;
    }

    public void setRateEnd(Timestamp rateEnd) {
        this.rateEnd = rateEnd;
    }

    public Timestamp getCostFormulaStart() {
        return costFormulaStart;
    }

    public void setCostFormulaStart(Timestamp costFormulaStart) {
        this.costFormulaStart = costFormulaStart;
    }

    public Timestamp getCostFormulaEnd() {
        return costFormulaEnd;
    }

    public void setCostFormulaEnd(Timestamp costFormulaEnd) {
        this.costFormulaEnd = costFormulaEnd;
    }

    public Timestamp getAmountFormulaStart() {
        return amountFormulaStart;
    }

    public void setAmountFormulaStart(Timestamp amountFormulaStart) {
        this.amountFormulaStart = amountFormulaStart;
    }

    public Timestamp getAmountFormulaEnd() {
        return amountFormulaEnd;
    }

    public void setAmountFormulaEnd(Timestamp amountFormulaEnd) {
        this.amountFormulaEnd = amountFormulaEnd;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
