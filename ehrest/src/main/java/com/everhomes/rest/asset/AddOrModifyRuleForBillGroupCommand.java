//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *<ul>
 * <li>billGroupId:账单组id</li>
 * <li>billGroupRuleId:账单组收费项目id</li>
 * <li>GroupChargingItemName:账单组收费项目名称</li>
 * <li>chargingItemId:收费项目id</li>
 * <li>chargingStandardId:收费项目id</li>
 * <li>variables:变量集合，参考{@link com.everhomes.rest.asset.ChargingItemVariable}</li>
 *</ul>
 */
public class AddOrModifyRuleForBillGroupCommand {
    @NotNull
    private Long billGroupId;

    private Long billGroupRuleId;
    @NotNull
    private String GroupChargingItemName;
    private Long chargingItemId;
    private Long chargingStandardId;
    @ItemType(ChargingItemVariable.class)
    private List<ChargingItemVariable> variables;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getBillGroupId() {
        return billGroupId;
    }

    public void setBillGroupId(Long billGroupId) {
        this.billGroupId = billGroupId;
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

    public List<ChargingItemVariable> getVariables() {
        return variables;
    }

    public void setVariables(List<ChargingItemVariable> variables) {
        this.variables = variables;
    }

    public AddOrModifyRuleForBillGroupCommand() {

    }
}
