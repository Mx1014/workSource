//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 *<ul>
 * <li>billGroupId:账单组id</li>
 * <li>billGroupRuleId:账单组收费项目id</li>
 * <li>GroupChargingItemName:账单组收费项目名称</li>
 * <li>chargingItemId:收费项目id</li>
 * <li>chargingStandardId:收费项目id</li>
 * <li>variables:变量集合，参考{@link com.everhomes.rest.asset.ChargingItemVariable}</li>
 * <li>suggestUnitPrice:建议单价</li>
 * <li>area_size_type:计费面积类型,1：合同面积；2.建筑面积；3：使用面积；4：出租面积</li>
 * <li>billItemMonthOffset:收费项产生时间偏离当前月的月数， 若是费用周期最后一天，则不填即可</li>
 * <li>billItemDayOffset:收费项产生时间偏离当前月的日数，若是费用周期最后一天，不填即可，周期最后一天为default值</li>
 * <li>organizationId:管理公司id</li>
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

    private BigDecimal suggestUnitPrice;
    private Byte area_size_type;
    private Integer billItemMonthOffset;
    private Integer billItemDayOffset;


    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;

    private Long organizationId;
    private Long appId;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }


    public Integer getBillItemMonthOffset() {
        return billItemMonthOffset;
    }

    public void setBillItemMonthOffset(Integer billItemMonthOffset) {
        this.billItemMonthOffset = billItemMonthOffset;
    }

    public Integer getBillItemDayOffset() {
        return billItemDayOffset;
    }

    public void setBillItemDayOffset(Integer billItemDayOffset) {
        this.billItemDayOffset = billItemDayOffset;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getBillGroupId() {
        return billGroupId;
    }

    public Byte getArea_size_type() {
        return area_size_type;
    }

    public void setArea_size_type(Byte area_size_type) {
        this.area_size_type = area_size_type;
    }

    public void setBillGroupId(Long billGroupId) {
        this.billGroupId = billGroupId;
    }

    public BigDecimal getSuggestUnitPrice() {
        return suggestUnitPrice;
    }

    public void setSuggestUnitPrice(BigDecimal suggestUnitPrice) {
        this.suggestUnitPrice = suggestUnitPrice;
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

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public AddOrModifyRuleForBillGroupCommand() {

    }
}
