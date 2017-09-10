//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 *<ul>
 * <li>billItemName:项目名称</li>
 * <li>amountOwed:待缴金额</li>
 * <li>addressName:地址</li>
 * <li>status:清账状态</li>
 *</ul>
 */
public class ShowBillDetailForClientDTO {
    private String billItemName;
    private BigDecimal amountOwed;
    private String addressName;
    private String status;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public void setBillItemName(String billItemName) {
        this.billItemName = billItemName;
    }

    public BigDecimal getAmountOwed() {
        return amountOwed;
    }

    public void setAmountOwed(BigDecimal amountOwed) {
        this.amountOwed = amountOwed;
    }

    public ShowBillDetailForClientDTO() {

    }
}
