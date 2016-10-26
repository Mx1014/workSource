package com.everhomes.rest.energy;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.util.List;

/**
 * <ul>
 *     <li>meterList: 表记读数列表 {@link com.everhomes.rest.energy.EnergyStatMeterDTO}</li>
 *     <li>serviceList: 性质列表 {@link com.everhomes.rest.energy.EnergyStatMeterCategoryServiceDTO}</li>
 *     <li>billList: 项目列表 {@link com.everhomes.rest.energy.EnergyStatMeterCategoryBillDTO}</li>
 *     <li>burdenAmountList: 负担量列表</li>
 *     <li>burdenCostList: 负担费用列表</li>
 * </ul>
 */
public class EnergyStatDTO {

    @ItemType(EnergyStatMeterDTO.class)
    private List<EnergyStatMeterDTO> meterList;

    @ItemType(EnergyStatMeterCategoryServiceDTO.class)
    private List<EnergyStatMeterCategoryServiceDTO> serviceList;

    @ItemType(EnergyStatMeterCategoryBillDTO.class)
    private List<EnergyStatMeterCategoryBillDTO> billList;

    @ItemType(BigDecimal.class)
    private List<BigDecimal> burdenAmountList;
    @ItemType(BigDecimal.class)
    private List<BigDecimal> burdenCostList;

    public List<EnergyStatMeterDTO> getMeterList() {
        return meterList;
    }

    public void setMeterList(List<EnergyStatMeterDTO> meterList) {
        this.meterList = meterList;
    }

    public List<EnergyStatMeterCategoryServiceDTO> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<EnergyStatMeterCategoryServiceDTO> serviceList) {
        this.serviceList = serviceList;
    }

    public List<EnergyStatMeterCategoryBillDTO> getBillList() {
        return billList;
    }

    public void setBillList(List<EnergyStatMeterCategoryBillDTO> billList) {
        this.billList = billList;
    }

    public List<BigDecimal> getBurdenAmountList() {
        return burdenAmountList;
    }

    public void setBurdenAmountList(List<BigDecimal> burdenAmountList) {
        this.burdenAmountList = burdenAmountList;
    }

    public List<BigDecimal> getBurdenCostList() {
        return burdenCostList;
    }

    public void setBurdenCostList(List<BigDecimal> burdenCostList) {
        this.burdenCostList = burdenCostList;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
