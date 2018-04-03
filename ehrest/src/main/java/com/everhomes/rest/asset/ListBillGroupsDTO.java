//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>BillGroupId:账单组id</li>
 * <li>BillGroupName:账单组名称</li>
 * <li>defaultOrder:账单组排序，数值小着优先度高</li>
 * <li>billingCycle:计费周期,1:按天;2:按月;3:按季度;4:按年;</li>
 * <li>billingDay:出账单日</li>
 * <li>dueDay:最晚还款日</li>
 * <li>dueDayType:最晚还款日的单位，1：日；2：月</li>
 *</ul>
 */
public class ListBillGroupsDTO {
    private Long BillGroupId;
    private String BillGroupName;
    private Integer defaultOrder;
    private Byte billingCycle;
    private Integer billingDay;
    private Integer dueDay;
    private Byte dueDayType;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Integer getDefaultOrder() {
        return defaultOrder;
    }

    public void setDefaultOrder(Integer defaultOrder) {
        this.defaultOrder = defaultOrder;
    }

    public Long getBillGroupId() {
        return BillGroupId;
    }

    public void setBillGroupId(Long billGroupId) {
        BillGroupId = billGroupId;
    }

    public String getBillGroupName() {
        return BillGroupName;
    }

    public Byte getDueDayType() {
        return dueDayType;
    }

    public void setDueDayType(Byte dueDayType) {
        this.dueDayType = dueDayType;
    }

    public void setBillGroupName(String billGroupName) {
        BillGroupName = billGroupName;
    }

    public Byte getBillingCycle() {
        return billingCycle;
    }

    public void setBillingCycle(Byte billingCycle) {
        this.billingCycle = billingCycle;
    }

    public Integer getBillingDay() {
        return billingDay;
    }

    public void setBillingDay(Integer billingDay) {
        this.billingDay = billingDay;
    }

    public Integer getDueDay() {
        return dueDay;
    }

    public void setDueDay(Integer dueDay) {
        this.dueDay = dueDay;
    }

    public ListBillGroupsDTO() {

    }
}
