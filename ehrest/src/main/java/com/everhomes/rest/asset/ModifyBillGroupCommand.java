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

    public ModifyBillGroupCommand() {

    }
}
