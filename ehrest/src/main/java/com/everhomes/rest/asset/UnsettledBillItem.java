//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 *<ul>
 * <li>billItemId:账单收费项目id</li>
 * <li>defaultOrder:排序</li>
 * <li>dateStr:账期</li>
 * <li>billItemName:收费项目名称</li>
 * <li>targetName:客户名称</li>
 * <li>addressName:楼栋门牌</li>
 * <li>amountReceivable:应收金额</li>
 *</ul>
 */
public class UnsettledBillItem {
    private Long billItemId;
    private Integer defaultOrder;
    private String dateStr;
    private String billItemName;
    private String targetName;
    private String addressName;
    private BigDecimal amountReceivable;

    public Long getBillItemId() {
        return billItemId;
    }

    public void setBillItemId(Long billItemId) {
        this.billItemId = billItemId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Integer getDefaultOrder() {
        return defaultOrder;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public void setDefaultOrder(Integer defaultOrder) {
        this.defaultOrder = defaultOrder;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getBillItemName() {
        return billItemName;
    }

    public void setBillItemName(String billItemName) {
        this.billItemName = billItemName;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public BigDecimal getAmountReceivable() {
        return amountReceivable;
    }

    public void setAmountReceivable(BigDecimal amountReceivable) {
        this.amountReceivable = amountReceivable;
    }

    public UnsettledBillItem() {

    }
}
