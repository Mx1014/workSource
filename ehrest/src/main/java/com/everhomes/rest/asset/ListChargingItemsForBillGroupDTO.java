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
 * <li>billingCycle:计费周期，2：按月；3：按季度；4：按年</li>
 * <li>billItemGenerationMonth: 费项产生的月数</li>
 * <li>billItemGenerationDay: 费项产生的日数</li>
 * <li>billItemGenerationType: 1: 费项产生项第几个月份；2:费项产色黄姑娘第几个月份第几号；3：费用周期最后一天产生</li>
 * <li>chargingItemId: 费项ID</li>
 *</ul>
 */
public class ListChargingItemsForBillGroupDTO {
    private Long billGroupRuleId;
    private Long defaultOrder;
    private String projectChargingItemName;
    private String chargingStandardName;
    private String formula;
    @ItemType(ChargingItemVariable.class)
    private List<ChargingItemVariable> variables;
    private Byte billingCycle;
    private Integer billItemGenerationMonth;
    private Integer billItemGenerationDay;
    private Byte billItemGenerationType;
    private Long chargingItemId;

    public Byte getBillItemGenerationType() {
        return billItemGenerationType;
    }

    public void setBillItemGenerationType(Byte billItemGenerationType) {
        this.billItemGenerationType = billItemGenerationType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getBillGroupRuleId() {
        return billGroupRuleId;
    }

    public Integer getBillItemGenerationMonth() {
        return billItemGenerationMonth;
    }

    public void setBillItemGenerationMonth(Integer billItemGenerationMonth) {
        this.billItemGenerationMonth = billItemGenerationMonth;
    }

    public Integer getBillItemGenerationDay() {
        return billItemGenerationDay;
    }

    public void setBillItemGenerationDay(Integer billItemGenerationDay) {
        this.billItemGenerationDay = billItemGenerationDay;
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

    public String getProjectChargingItemName() {
        return projectChargingItemName;
    }

    public void setProjectChargingItemName(String projectChargingItemName) {
        this.projectChargingItemName = projectChargingItemName;
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

	public Long getChargingItemId() {
		return chargingItemId;
	}

	public void setChargingItemId(Long chargingItemId) {
		this.chargingItemId = chargingItemId;
	}
}
