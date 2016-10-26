package com.everhomes.rest.energy;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.util.List;

/**
 * <ul>
 *     <li>meter: 表记信息 {@link com.everhomes.rest.energy.EnergyMeterDTO}</li>
 *     <li>costList: 费用列表</li>
 *     <li>amountList: 用量列表</li>
 * </ul>
 */
public class EnergyStatMeterDTO {

    private EnergyMeterDTO meter;
    @ItemType(BigDecimal.class)
    private List<BigDecimal> amountList;
    @ItemType(BigDecimal.class)
    private List<BigDecimal> costList;

    public EnergyMeterDTO getMeter() {
        return meter;
    }

    public void setMeter(EnergyMeterDTO meter) {
        this.meter = meter;
    }

    public List<BigDecimal> getAmountList() {
        return amountList;
    }

    public void setAmountList(List<BigDecimal> amountList) {
        this.amountList = amountList;
    }

    public List<BigDecimal> getCostList() {
        return costList;
    }

    public void setCostList(List<BigDecimal> costList) {
        this.costList = costList;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
