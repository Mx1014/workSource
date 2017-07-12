package com.everhomes.rest.energy;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * <ul>
 *     <li>organizationId: 组织id</li>
 *     <li>meterId: 表记id</li>
 *     <li>name: 表记名称</li>
 *     <li>meterNumber: 表记编号</li>
 *     <li>rate: 倍率</li>
 *     <li>price: 价格</li>
 *     <li>costFormulaId: 费用计算公式</li>
 *     <li>amountFormulaId: 用量计算公式</li>
 *     <li>startTime: 修改属性时选择的起始时间 </li>
 *     <li>endTime: 修改属性时选择的结束时间 </li>
 *     <li>calculationType: 价格计算方式 参考{@link com.everhomes.rest.energy.PriceCalculationType} </li>
 *     <li>configId: 价格方案id </li>
 * </ul>
 */
public class UpdateEnergyMeterCommand {

    @NotNull private Long organizationId;
    @NotNull private Long meterId;
    @Size(max = 100) private String name;
    @Size(max = 50) private String meterNumber;
    private BigDecimal rate;
    private BigDecimal price;
    private Long costFormulaId;
    private Long amountFormulaId;

    private Long startTime;
    private Long endTime;

    private Byte calculationType;
    private Long configId;

    public Long getMeterId() {
        return meterId;
    }

    public void setMeterId(Long meterId) {
        this.meterId = meterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeterNumber() {
        return meterNumber;
    }

    public void setMeterNumber(String meterNumber) {
        this.meterNumber = meterNumber;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
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
