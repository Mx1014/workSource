//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 *<ul>
 * <li>valueOfX:x轴名称</li>
 * <li>amountReceivable:应缴金额</li>
 * <li>amountReceived:实收金额</li>
 * <li>amountOwed:欠收金额</li>
 *</ul>
 */
public class BillStaticsDTO {
    private String valueOfX;
    private BigDecimal amountReceivable;
    private BigDecimal amountReceived;
    private BigDecimal amountOwed;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getValueOfX() {
        return valueOfX;
    }

    public void setValueOfX(String valueOfX) {
        this.valueOfX = valueOfX;
    }

    public BigDecimal getAmountReceivable() {
        return amountReceivable;
    }

    public void setAmountReceivable(BigDecimal amountReceivable) {
        this.amountReceivable = amountReceivable;
    }

    public BigDecimal getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(BigDecimal amountReceived) {
        this.amountReceived = amountReceived;
    }

    public BigDecimal getAmountOwed() {
        return amountOwed;
    }

    public void setAmountOwed(BigDecimal amountOwed) {
        this.amountOwed = amountOwed;
    }

    public BillStaticsDTO() {

    }
}
