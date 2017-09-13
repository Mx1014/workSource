//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 *<ul>
 * <li>ownerId:所属者id</li>
 * <li>ownerType:所属者类型</li>
 * <li>chargingItemId:收费项目id</li>
 * <li>chargingStandardName:收费标准名称</li>
 * <li>formulaType:公式类型,1:固定金额;2:普通公式;3:斜率跟着变量区间总体变化;4:斜率在不同变量区间取值不同;</li>
 * <li>billingCycle:计费周期,1:按天;2:按月;3:按季度;4:按年;</li>
 * <li>priceUnitType:价格单位类型,1:日单价;2:月单价;</li>
 * <li>formula:公式</li>
 *</ul>
 */
public class CreateChargingStandardCommand {
    @NotNull
    private Long ownerId;
    @NotNull
    private String ownerType;
    @NotNull
    private String chargingStandardName;
    @NotNull
    private Long chargingItemId;
    @NotNull
    private Byte formulaType;
    @NotNull
    private Byte billingCycle;
    @NotNull
    private Byte priceUnitType;
    @NotNull
    private String formula;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getChargingItemId() {
        return chargingItemId;
    }

    public void setChargingItemId(Long chargingItemId) {
        this.chargingItemId = chargingItemId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getChargingStandardName() {
        return chargingStandardName;
    }

    public void setChargingStandardName(String chargingStandardName) {
        this.chargingStandardName = chargingStandardName;
    }

    public Byte getFormulaType() {
        return formulaType;
    }

    public void setFormulaType(Byte formulaType) {
        this.formulaType = formulaType;
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

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public CreateChargingStandardCommand() {

    }
}
