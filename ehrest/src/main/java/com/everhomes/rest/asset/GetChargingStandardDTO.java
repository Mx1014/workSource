//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>chargingStandardId:收费标准id</li>
 * <li>chargingStandardName:收费标准名称</li>
 * <li>formula:公式</li>
 * <li>billingCycle:计费周期,1:按天;2:按月;3:按季度;4:按年;</li>
 * <li>priceUnitType:价格单位类型,1:日单价;2:月单价;</li>
 * <li>balanceDateType:应收日期类型，1:计费开始周期所在月2:计费结束周期所在月;</li>
 *</ul>
 */
public class GetChargingStandardDTO {
    private Long chargingStandardId;
    private String chargingStandardName;
    private String formula;
    private Byte billingCycle;
    private Byte priceUnitType;
    private Byte balanceDateType;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getChargingStandardId() {
        return chargingStandardId;
    }

    public void setChargingStandardId(Long chargingStandardId) {
        this.chargingStandardId = chargingStandardId;
    }

    public String getChargingStandardName() {
        return chargingStandardName;
    }

    public void setChargingStandardName(String chargingStandardName) {
        this.chargingStandardName = chargingStandardName;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public Byte getBillingCycle() {
        return billingCycle;
    }

    public void setBillingCycle(Byte billingCycle) {
        this.billingCycle = billingCycle;
    }

    public Byte getPriceUnitType() {
        return priceUnitType;
    }

    public void setPriceUnitType(Byte priceUnitType) {
        this.priceUnitType = priceUnitType;
    }

    public Byte getBalanceDateType() {
        return balanceDateType;
    }

    public void setBalanceDateType(Byte balanceDateType) {
        this.balanceDateType = balanceDateType;
    }

    public GetChargingStandardDTO() {

    }
}
