//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 *<ul>
 * <li>BillGroupId:账单组id</li>
 * <li>billGroupName:账单组名称</li>
 * <li>billingCycle:生成账单周期,2:按月;3:按季度;4:按年</li>
 * <li>billDay:出账单日</li>
 * <li>dueDay:最晚还款日</li>
 * <li>dueDayType:最晚还款日的单位类型，1:日; 2:月</li>
 *</ul>
 */
public class ModifyBillGroupCommand {
    @NotNull
    private Long BillGroupId;
    @NotNull
    private String billGroupName;
    @NotNull
    private Byte billingCycle;
    @NotNull
    private Integer billDay;
    @NotNull
    private Integer dueDay;
    @NotNull
    private Byte dueDayType;

    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;

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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getBillGroupId() {
        return BillGroupId;
    }

    public void setBillGroupId(Long billGroupId) {
        BillGroupId = billGroupId;
    }

    public String getBillGroupName() {
        return billGroupName;
    }

    public void setBillGroupName(String billGroupName) {
        this.billGroupName = billGroupName;
    }

    public Byte getBillingCycle() {
        return billingCycle;
    }

    public void setBillingCycle(Byte billingCycle) {
        this.billingCycle = billingCycle;
    }

    public Integer getBillDay() {
        return billDay;
    }

    public void setBillDay(Integer billDay) {
        this.billDay = billDay;
    }

    public Integer getDueDay() {
        return dueDay;
    }

    public void setDueDay(Integer dueDay) {
        this.dueDay = dueDay;
    }

    public Byte getDueDayType() {
        return dueDayType;
    }

    public void setDueDayType(Byte dueDayType) {
        this.dueDayType = dueDayType;
    }

    public ModifyBillGroupCommand() {

    }
}
