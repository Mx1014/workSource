//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 *<ul>
 * <li>billItemName:项目名称</li>
 * <li>amountReceivable:应收金额</li>
 * <li>addressName:地址</li>
 *</ul>
 */
public class ShowBillDetailForClientDTO {
    private String billItemName;
    private BigDecimal amountReceivable;
    private String addressName;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getBillItemName() {
        return billItemName;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public void setBillItemName(String billItemName) {
        this.billItemName = billItemName;
    }

    public BigDecimal getAmountReceivable() {
        return amountReceivable;
    }

    public void setAmountReceivable(BigDecimal amountReceivable) {
        this.amountReceivable = amountReceivable;
    }

    public ShowBillDetailForClientDTO() {

    }
}
