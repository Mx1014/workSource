//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *<ul>
 * <li>chargingStandardId: 收费标准id</li>
 * <li>chargingStandardName:收费标准名称</li>
 * <li>formula:公式</li>
 * <li>formulaType:公式类型,1:固定金额;2:普通公式;3:斜面公式;4:梯度公式</li>
 * <li>BillingCycle:计费周期,1:按天;2:按月;3:按季度;4:按年</li>
 * <li>variables:变量列表，参考{@link com.everhomes.rest.asset.PaymentVariable}</li>
 *</ul>
 */
public class ListChargingStandardsDTO {
    private Long chargingStandardId;
    private String chargingStandardName;
    private String formula;
    private Byte formulaType;
    private Byte BillingCycle;
    @ItemType(String.class)
    private List<PaymentVariable> variables;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getChargingStandardId() {
        return chargingStandardId;
    }

    public Byte getFormulaType() {
        return formulaType;
    }

    public void setFormulaType(Byte formulaType) {
        this.formulaType = formulaType;
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
        return BillingCycle;
    }

    public void setBillingCycle(Byte billingCycle) {
        BillingCycle = billingCycle;
    }

    public List<PaymentVariable> getVariables() {
        return variables;
    }

    public void setVariables(List<PaymentVariable> variables) {
        this.variables = variables;
    }

    public ListChargingStandardsDTO() {

    }
}
