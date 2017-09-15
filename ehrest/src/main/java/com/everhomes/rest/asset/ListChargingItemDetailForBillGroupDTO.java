//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *<ul>
 * <li>billGroupRuleId:账单组收费项目id</li>
 * <li>GroupChargingItemName:账单组收费项目名称</li>
 * <li>chargingItemId:收费项目id</li>
 * <li>chargingStandardId:收费项目id</li>
 * <li>formula:公式</li>
 * <li>variables:变量集合，参考{@link com.everhomes.rest.asset.ChargingItemVariable}</li>
 *</ul>
 */
public class ListChargingItemDetailForBillGroupDTO {
    private Long billGroupRuleId;
    private String GroupChargingItemName;
    private Long chargingItemId;
    private Long chargingStandardId;
    private String formula;
    @ItemType(ChargingItemVariable.class)
    private List<ChargingItemVariable> variables;

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

    public String getGroupChargingItemName() {
        return GroupChargingItemName;
    }

    public void setGroupChargingItemName(String groupChargingItemName) {
        GroupChargingItemName = groupChargingItemName;
    }

    public Long getChargingItemId() {
        return chargingItemId;
    }

    public void setChargingItemId(Long chargingItemId) {
        this.chargingItemId = chargingItemId;
    }

    public Long getChargingStandardId() {
        return chargingStandardId;
    }

    public void setChargingStandardId(Long chargingStandardId) {
        this.chargingStandardId = chargingStandardId;
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

    public ListChargingItemDetailForBillGroupDTO() {

    }
}
