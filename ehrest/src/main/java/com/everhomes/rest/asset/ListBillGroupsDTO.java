//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>BillGroupId:账单组id</li>
 * <li>BillGroupName:账单组名称</li>
 * <li>defaultOrder:账单组排序，数值小着优先度高</li>
 *</ul>
 */
public class ListBillGroupsDTO {
    private Long BillGroupId;
    private String BillGroupName;
    private Integer defaultOrder;

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

    public void setBillGroupName(String billGroupName) {
        BillGroupName = billGroupName;
    }

    public ListBillGroupsDTO() {

    }
}
