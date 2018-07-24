//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2017/11/17.
 */

/**
 *<ul>
 * <li>billDuration:账期</li>
 * <li>amountReceivable:应缴金额</li>
 * <li>amountOwed:待缴金额</li>
 * <li>billId:账单ID</li>
 *</ul>
 */
public class BillForClientV2 {
    private String billDuration;
    private String amountReceivable;
    private String amountOwed;
    private String billId;

    public String getBillDuration() {
        return billDuration;
    }

    public void setBillDuration(String billDuration) {
        this.billDuration = billDuration;
    }

    public String getAmountReceivable() {
        return amountReceivable;
    }

    public void setAmountReceivable(String amountReceivable) {
        this.amountReceivable = amountReceivable;
    }

    public String getAmountOwed() {
        return amountOwed;
    }

    public void setAmountOwed(String amountOwed) {
        this.amountOwed = amountOwed;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }
}
