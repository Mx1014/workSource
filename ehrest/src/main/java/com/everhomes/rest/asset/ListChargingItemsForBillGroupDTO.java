//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *<ul>
 * <li>billGroupRuleId:账单组收费项目id</li>
 * <li>defaultOrder:排序，数值小着优先在前</li>
 * <li>groupChargingItemName:账单组收费项目名称</li>
 * <li>chargingStandardName:收费标准名称</li>
 * <li>formula:公式</li>
 * <li>variables:变量名称和值的集合，参考{@link com.everhomes.rest.asset.ChargingItemVariable}</li>
 * <li>billingCycle:</li>
 *</ul>
 */
public class ListChargingItemsForBillGroupDTO {
    private Long billGroupRuleId;
    private Long defaultOrder;
    private String groupChargingItemName;
    private String chargingStandardName;
    private String formula;
    @ItemType(ChargingItemVariable.class)
    private List<ChargingItemVariable> variables;
    private Byte billingCycle;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getBillGroupRuleId() {
        return billGroupRuleId;
    }

    public void setBillGroupRuleId(Long billGroupRuleId) {
        this.billGroupRuleId = billGroupRuleId;
    }

    public Long getDefaultOrder() {
        return defaultOrder;
    }

    public void setDefaultOrder(Long defaultOrder) {
        this.defaultOrder = defaultOrder;
    }

    public String getGroupChargingItemName() {
        return groupChargingItemName;
    }

    public void setGroupChargingItemName(String groupChargingItemName) {
        this.groupChargingItemName = groupChargingItemName;
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

    public List<ChargingItemVariable> getVariables() {
        return variables;
    }

    public void setVariables(List<ChargingItemVariable> variables) {
        this.variables = variables;
    }

    public Byte getBillingCycle() {
        return billingCycle;
    }

    public void setBillingCycle(Byte billingCycle) {
        this.billingCycle = billingCycle;
    }

    public ListChargingItemsForBillGroupDTO() {

    }
}
